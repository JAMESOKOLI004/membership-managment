package com.example.data

import com.example.model.CheckInRecord
import com.example.model.GymMember
import com.example.model.MembershipPlan
import com.example.model.PaymentRecord
import com.example.model.StaffUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class FitAdminRepository private constructor() {

  private val _members = MutableStateFlow<List<GymMember>>(SampleData.defaultMembers)
  val members: StateFlow<List<GymMember>> = _members.asStateFlow()

  private val _plans = MutableStateFlow<List<MembershipPlan>>(SampleData.defaultPlans)
  val plans: StateFlow<List<MembershipPlan>> = _plans.asStateFlow()

  private val _checkIns = MutableStateFlow<List<CheckInRecord>>(SampleData.defaultCheckIns)
  val checkIns: StateFlow<List<CheckInRecord>> = _checkIns.asStateFlow()

  private val _payments = MutableStateFlow<List<PaymentRecord>>(SampleData.defaultPayments)
  val payments: StateFlow<List<PaymentRecord>> = _payments.asStateFlow()

  private val _currentStaff = MutableStateFlow<StaffUser>(SampleData.defaultStaff.first())
  val currentStaff: StateFlow<StaffUser> = _currentStaff.asStateFlow()

  private val _isLoggedIn = MutableStateFlow(true) // Start authenticated for immediate front-desk kiosk preview
  val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

  val staffList: List<StaffUser> = SampleData.defaultStaff

  val todayIsoDate = "2026-10-21"

  fun login(staffId: String, pin: String): Boolean {
    val staff = staffList.find { it.id == staffId && it.pin == pin }
    return if (staff != null) {
      _currentStaff.value = staff
      _isLoggedIn.value = true
      true
    } else {
      false
    }
  }

  fun switchStaff(staff: StaffUser) {
    _currentStaff.value = staff
    _isLoggedIn.value = true
  }

  fun logout() {
    _isLoggedIn.value = false
  }

  fun getMember(id: String): GymMember? {
    return _members.value.find { it.id.equals(id, ignoreCase = true) }
  }

  fun addMember(
    name: String,
    phone: String,
    email: String,
    planId: String,
    startDate: String,
    assignedTrainer: String?,
    emergencyContact: String?,
    notes: String?
  ): GymMember {
    val plan = _plans.value.find { it.id == planId } ?: _plans.value.first()
    val expiryDate = calculateNewExpiryDate(startDate, plan.durationDays)
    val nextIdNum = 1000 + _members.value.size + 1
    val newMember = GymMember(
      id = "M-$nextIdNum",
      name = name,
      phone = phone,
      email = email,
      planId = plan.id,
      planName = plan.name,
      startDate = startDate,
      expiryDate = expiryDate,
      avatarColorHex = getRandomAvatarColor(),
      assignedTrainer = if (assignedTrainer.isNullOrBlank() || assignedTrainer == "None") null else assignedTrainer,
      emergencyContact = emergencyContact,
      checkInCount = 0,
      notes = notes
    )
    _members.value = listOf(newMember) + _members.value
    return newMember
  }

  fun updateMember(member: GymMember) {
    _members.value = _members.value.map {
      if (it.id == member.id) member else it
    }
  }

  fun performCheckIn(memberId: String): Pair<Boolean, CheckInRecord> {
    val member = getMember(memberId)
      ?: return Pair(
        false,
        CheckInRecord(
          id = "CK-${System.currentTimeMillis() % 10000}",
          memberId = memberId,
          memberName = "Unknown Member",
          planName = "Unknown",
          timestamp = System.currentTimeMillis(),
          timeFormatted = getCurrentTimeFormatted(),
          dateFormatted = todayIsoDate,
          isSuccess = false,
          message = "Member ID not found",
          staffName = _currentStaff.value.name
        )
      )

    val daysRemaining = member.getDaysUntilExpiry(todayIsoDate)
    val isExpired = daysRemaining < 0

    val recordId = "CK-${Random.nextInt(600, 999)}"
    val record = if (isExpired) {
      CheckInRecord(
        id = recordId,
        memberId = member.id,
        memberName = member.name,
        planName = member.planName,
        timestamp = System.currentTimeMillis(),
        timeFormatted = getCurrentTimeFormatted(),
        dateFormatted = todayIsoDate,
        isSuccess = false,
        message = "BLOCKED: Membership Expired (${-daysRemaining}d ago)",
        staffName = _currentStaff.value.name
      )
    } else {
      // Increment check-in count
      val updatedMember = member.copy(checkInCount = member.checkInCount + 1)
      updateMember(updatedMember)

      CheckInRecord(
        id = recordId,
        memberId = member.id,
        memberName = member.name,
        planName = member.planName,
        timestamp = System.currentTimeMillis(),
        timeFormatted = getCurrentTimeFormatted(),
        dateFormatted = todayIsoDate,
        isSuccess = true,
        message = if (daysRemaining <= 7) "Access Granted (${daysRemaining}d left - Alert Desk)" else "Access Granted - Turnstile #1",
        staffName = _currentStaff.value.name
      )
    }

    _checkIns.value = listOf(record) + _checkIns.value
    return Pair(!isExpired, record)
  }

  fun recordPayment(
    memberId: String,
    planId: String,
    amount: Double,
    paymentMethod: String,
    autoExtend: Boolean,
    notes: String?
  ): PaymentRecord {
    val member = getMember(memberId)
    val plan = _plans.value.find { it.id == planId } ?: _plans.value.first()
    val memberName = member?.name ?: "Guest"

    var newExpiry: String? = null
    var extendedDays = 0

    if (autoExtend && member != null) {
      val daysLeft = member.getDaysUntilExpiry(todayIsoDate)
      val baseDate = if (daysLeft > 0) member.expiryDate else todayIsoDate
      newExpiry = calculateNewExpiryDate(baseDate, plan.durationDays)
      extendedDays = plan.durationDays

      val updatedMember = member.copy(
        planId = plan.id,
        planName = plan.name,
        expiryDate = newExpiry
      )
      updateMember(updatedMember)
    }

    val receiptNum = "REC-${Random.nextInt(8850, 9999)}"
    val payRecord = PaymentRecord(
      id = "PAY-${Random.nextInt(910, 999)}",
      receiptNumber = receiptNum,
      memberId = memberId,
      memberName = memberName,
      planId = plan.id,
      planName = plan.name,
      amount = amount,
      date = todayIsoDate,
      paymentMethod = paymentMethod,
      autoExtendedDays = extendedDays,
      newExpiryDate = newExpiry,
      notes = notes ?: "Processed at front desk by ${_currentStaff.value.name}"
    )

    _payments.value = listOf(payRecord) + _payments.value
    return payRecord
  }

  fun addPlan(
    name: String,
    durationDays: Int,
    price: Double,
    description: String,
    features: List<String>,
    isPopular: Boolean = false
  ): MembershipPlan {
    val newId = "PLAN-${_plans.value.size + 1}"
    val plan = MembershipPlan(
      id = newId,
      name = name,
      durationDays = durationDays,
      price = price,
      description = description,
      features = features,
      isPopular = isPopular,
      badgeColorHex = if (isPopular) 0xFF84CC16 else 0xFF3B82F6
    )
    _plans.value = _plans.value + plan
    return plan
  }

  fun sendReminder(memberId: String): Boolean {
    val member = getMember(memberId) ?: return false
    val updated = member.copy(reminderSentAt = "Sent ${getCurrentTimeFormatted()}")
    updateMember(updated)
    return true
  }

  private fun calculateNewExpiryDate(baseDateIso: String, daysToAdd: Int): String {
    return try {
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
      val date = sdf.parse(baseDateIso) ?: Date()
      val calendar = Calendar.getInstance()
      calendar.time = date
      calendar.add(Calendar.DAY_OF_YEAR, daysToAdd)
      sdf.format(calendar.time)
    } catch (e: Exception) {
      "2026-11-20"
    }
  }

  private fun getCurrentTimeFormatted(): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.US)
    return sdf.format(Date())
  }

  private fun getRandomAvatarColor(): Long {
    val colors = listOf(
      0xFF1E293B, 0xFF065F46, 0xFF831843, 0xFF4338CA, 0xFF7C2D12, 0xFF14532D, 0xFF581C87, 0xFF0E7490
    )
    return colors[Random.nextInt(colors.size)]
  }

  companion object {
    @Volatile
    private var instance: FitAdminRepository? = null

    fun getInstance(): FitAdminRepository {
      return instance ?: synchronized(this) {
        instance ?: FitAdminRepository().also { instance = it }
      }
    }
  }
}
