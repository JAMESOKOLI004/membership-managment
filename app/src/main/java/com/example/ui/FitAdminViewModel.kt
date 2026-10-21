package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FitAdminRepository
import com.example.model.CheckInRecord
import com.example.model.GymMember
import com.example.model.MemberStatus
import com.example.model.MembershipPlan
import com.example.model.PaymentRecord
import com.example.model.StaffUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
  object Login : Screen()
  object Dashboard : Screen()
  object MembersList : Screen()
  data class AddEditMember(val memberId: String? = null) : Screen()
  data class MemberProfile(val memberId: String) : Screen()
  object PlansSetup : Screen()
  data class RecordPayment(val preselectedMemberId: String? = null, val preselectedPlanId: String? = null) : Screen()
  object CheckIn : Screen()
  object ExpiringSoon : Screen()
}

data class DashboardStats(
  val activeMembersCount: Int,
  val expiringThisWeekCount: Int,
  val expiredMembersCount: Int,
  val revenueThisMonth: Double,
  val checkInsTodayCount: Int,
  val blockedCheckInsTodayCount: Int
)

data class CheckInResultModal(
  val member: GymMember,
  val isSuccess: Boolean,
  val message: String,
  val daysRemaining: Long
)

class FitAdminViewModel(
  private val repository: FitAdminRepository = FitAdminRepository.getInstance()
) : ViewModel() {

  private val _currentScreen = MutableStateFlow<Screen>(Screen.Dashboard)
  val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

  val members: StateFlow<List<GymMember>> = repository.members
  val plans: StateFlow<List<MembershipPlan>> = repository.plans
  val checkIns: StateFlow<List<CheckInRecord>> = repository.checkIns
  val payments: StateFlow<List<PaymentRecord>> = repository.payments
  val currentStaff: StateFlow<StaffUser> = repository.currentStaff
  val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn
  val staffList: List<StaffUser> = repository.staffList

  // Search & Filters
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _statusFilter = MutableStateFlow<MemberStatus?>(null) // null = all
  val statusFilter: StateFlow<MemberStatus?> = _statusFilter.asStateFlow()

  // Modal feedback for Check-In
  private val _checkInModal = MutableStateFlow<CheckInResultModal?>(null)
  val checkInModal: StateFlow<CheckInResultModal?> = _checkInModal.asStateFlow()

  // Global Toast / Snackbar notification message
  private val _userMessage = MutableStateFlow<String?>(null)
  val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

  // Computed filtered members
  val filteredMembers: StateFlow<List<GymMember>> = combine(
    repository.members,
    _searchQuery,
    _statusFilter
  ) { list, query, filter ->
    list.filter { member ->
      val matchesQuery = query.isBlank() ||
          member.name.contains(query, ignoreCase = true) ||
          member.phone.contains(query, ignoreCase = true) ||
          member.id.contains(query, ignoreCase = true) ||
          member.email.contains(query, ignoreCase = true)

      val memberStatus = member.getCalculatedStatus(repository.todayIsoDate)
      val matchesFilter = filter == null || memberStatus == filter

      matchesQuery && matchesFilter
    }
  }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  // Expiring members list (0 <= days <= 7, plus overdue within 14 days)
  val expiringMembers: StateFlow<List<GymMember>> = repository.members.combine(
    _searchQuery
  ) { list, query ->
    list.filter { member ->
      val days = member.getDaysUntilExpiry(repository.todayIsoDate)
      val isExpiringSoon = days in -14..7
      val matchesQuery = query.isBlank() ||
          member.name.contains(query, ignoreCase = true) ||
          member.id.contains(query, ignoreCase = true)
      isExpiringSoon && matchesQuery
    }.sortedBy { it.getDaysUntilExpiry(repository.todayIsoDate) }
  }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  // Dashboard Stats computation
  val dashboardStats: StateFlow<DashboardStats> = combine(
    repository.members,
    repository.payments,
    repository.checkIns
  ) { mems, pays, checks ->
    val active = mems.count { it.getCalculatedStatus(repository.todayIsoDate) == MemberStatus.ACTIVE }
    val expiring = mems.count { it.getCalculatedStatus(repository.todayIsoDate) == MemberStatus.EXPIRING_SOON }
    val expired = mems.count { it.getCalculatedStatus(repository.todayIsoDate) == MemberStatus.EXPIRED }

    // Revenue for current month (Oct 2026)
    val octRevenue = pays.filter { it.date.startsWith("2026-10") }.sumOf { it.amount }

    // Check-ins today
    val todayChecks = checks.filter { it.dateFormatted == repository.todayIsoDate }
    val successToday = todayChecks.count { it.isSuccess }
    val blockedToday = todayChecks.count { !it.isSuccess }

    DashboardStats(
      activeMembersCount = active,
      expiringThisWeekCount = expiring,
      expiredMembersCount = expired,
      revenueThisMonth = octRevenue,
      checkInsTodayCount = successToday,
      blockedCheckInsTodayCount = blockedToday
    )
  }.stateIn(
    viewModelScope,
    SharingStarted.Lazily,
    DashboardStats(0, 0, 0, 0.0, 0, 0)
  )

  fun navigateTo(screen: Screen) {
    _currentScreen.value = screen
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setStatusFilter(status: MemberStatus?) {
    _statusFilter.value = status
  }

  fun clearUserMessage() {
    _userMessage.value = null
  }

  fun dismissCheckInModal() {
    _checkInModal.value = null
  }

  fun login(staffId: String, pin: String): Boolean {
    val success = repository.login(staffId, pin)
    if (success) {
      _currentScreen.value = Screen.Dashboard
    }
    return success
  }

  fun switchStaff(staff: StaffUser) {
    repository.switchStaff(staff)
    _userMessage.value = "Shift switched to ${staff.name} (${staff.role})"
  }

  fun logout() {
    repository.logout()
    _currentScreen.value = Screen.Login
  }

  fun checkInMember(memberId: String) {
    val member = repository.getMember(memberId)
    if (member == null) {
      _userMessage.value = "Member $memberId not found."
      return
    }

    val (isSuccess, record) = repository.performCheckIn(memberId)
    val days = member.getDaysUntilExpiry(repository.todayIsoDate)

    _checkInModal.value = CheckInResultModal(
      member = member,
      isSuccess = isSuccess,
      message = record.message,
      daysRemaining = days
    )
  }

  fun recordPayment(
    memberId: String,
    planId: String,
    amount: Double,
    paymentMethod: String,
    autoExtend: Boolean,
    notes: String?
  ) {
    val payRecord = repository.recordPayment(
      memberId = memberId,
      planId = planId,
      amount = amount,
      paymentMethod = paymentMethod,
      autoExtend = autoExtend,
      notes = notes
    )
    val member = repository.getMember(memberId)
    val extMsg = if (autoExtend && payRecord.newExpiryDate != null) " (Membership extended to ${payRecord.newExpiryDate})" else ""
    _userMessage.value = "Payment of $${String.format("%.2f", amount)} recorded for ${member?.name ?: memberId}$extMsg. Receipt: ${payRecord.receiptNumber}"
  }

  fun saveMember(
    existingId: String?,
    name: String,
    phone: String,
    email: String,
    planId: String,
    startDate: String,
    assignedTrainer: String?,
    emergencyContact: String?,
    notes: String?
  ) {
    if (existingId != null) {
      val existing = repository.getMember(existingId)
      if (existing != null) {
        val plan = plans.value.find { it.id == planId } ?: plans.value.first()
        val updated = existing.copy(
          name = name,
          phone = phone,
          email = email,
          planId = plan.id,
          planName = plan.name,
          assignedTrainer = if (assignedTrainer.isNullOrBlank() || assignedTrainer == "None") null else assignedTrainer,
          emergencyContact = emergencyContact,
          notes = notes
        )
        repository.updateMember(updated)
        _userMessage.value = "Member ${name} profile updated successfully."
        _currentScreen.value = Screen.MemberProfile(existingId)
        return
      }
    }

    // New member
    val newMember = repository.addMember(
      name = name,
      phone = phone,
      email = email,
      planId = planId,
      startDate = startDate,
      assignedTrainer = assignedTrainer,
      emergencyContact = emergencyContact,
      notes = notes
    )
    _userMessage.value = "New member ${name} created (ID: ${newMember.id})."
    _currentScreen.value = Screen.MemberProfile(newMember.id)
  }

  fun addPlan(name: String, durationDays: Int, price: Double, description: String, features: List<String>, isPopular: Boolean) {
    val plan = repository.addPlan(name, durationDays, price, description, features, isPopular)
    _userMessage.value = "Membership plan '${plan.name}' added successfully."
  }

  fun sendExpiryReminder(memberId: String) {
    val member = repository.getMember(memberId)
    if (member != null) {
      repository.sendReminder(memberId)
      val days = member.getDaysUntilExpiry(repository.todayIsoDate)
      val urgency = if (days < 0) "already expired" else "expires in $days days"
      _userMessage.value = "Reminder sent to ${member.name} via SMS/WhatsApp: 'FitAdmin alert: Your pass $urgency. Renew at desk today!'"
    }
  }
}
