package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.CheckInResultModal
import com.example.ui.FitAdminViewModel
import com.example.ui.Screen
import com.example.ui.components.CheckInResultDialog
import com.example.ui.components.FitAdminBottomNavBar
import com.example.ui.components.FitAdminTopHeader
import com.example.ui.screens.AddEditMemberScreen
import com.example.ui.screens.CheckInScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ExpiringSoonScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MemberProfileScreen
import com.example.ui.screens.MembersListScreen
import com.example.ui.screens.PlansSetupScreen
import com.example.ui.screens.RecordPaymentScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        FitAdminApp()
      }
    }
  }
}

@Composable
fun FitAdminApp(viewModel: FitAdminViewModel = viewModel()) {
  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
  val currentStaff by viewModel.currentStaff.collectAsStateWithLifecycle()
  val staffList = viewModel.staffList

  val dashboardStats by viewModel.dashboardStats.collectAsStateWithLifecycle()
  val members by viewModel.filteredMembers.collectAsStateWithLifecycle()
  val allMembers by viewModel.members.collectAsStateWithLifecycle()
  val plans by viewModel.plans.collectAsStateWithLifecycle()
  val checkIns by viewModel.checkIns.collectAsStateWithLifecycle()
  val payments by viewModel.payments.collectAsStateWithLifecycle()
  val expiringMembers by viewModel.expiringMembers.collectAsStateWithLifecycle()

  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val statusFilter by viewModel.statusFilter.collectAsStateWithLifecycle()
  val checkInModal by viewModel.checkInModal.collectAsStateWithLifecycle()
  val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(userMessage) {
    userMessage?.let { msg ->
      snackbarHostState.showSnackbar(
        message = msg,
        duration = SnackbarDuration.Short
      )
      viewModel.clearUserMessage()
    }
  }

  // Handle hardware and gesture back navigation
  BackHandler(enabled = currentScreen != Screen.Dashboard && isLoggedIn) {
    when (currentScreen) {
      is Screen.MemberProfile, is Screen.AddEditMember -> viewModel.navigateTo(Screen.MembersList)
      else -> viewModel.navigateTo(Screen.Dashboard)
    }
  }

  if (!isLoggedIn || currentScreen is Screen.Login) {
    LoginScreen(
      staffList = staffList,
      onLogin = { staffId, pin -> viewModel.login(staffId, pin) },
      onQuickEnter = { staff -> viewModel.switchStaff(staff) }
    )
  } else {
    Scaffold(
      topBar = {
        FitAdminTopHeader(
          currentStaff = currentStaff,
          staffList = staffList,
          onSwitchStaff = { viewModel.switchStaff(it) },
          onLogout = { viewModel.logout() },
          onNavigateToExpiring = { viewModel.navigateTo(Screen.ExpiringSoon) },
          expiringCount = dashboardStats.expiringThisWeekCount
        )
      },
      bottomBar = {
        FitAdminBottomNavBar(
          currentScreen = currentScreen,
          onNavigate = { viewModel.navigateTo(it) },
          expiringCount = dashboardStats.expiringThisWeekCount
        )
      },
      snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        when (val screen = currentScreen) {
          is Screen.Dashboard -> {
            DashboardScreen(
              stats = dashboardStats,
              recentCheckIns = checkIns,
              onNavigate = { viewModel.navigateTo(it) },
              onQuickSearchSubmit = { query ->
                viewModel.setSearchQuery(query)
                viewModel.navigateTo(Screen.MembersList)
              }
            )
          }

          is Screen.MembersList -> {
            MembersListScreen(
              members = members,
              searchQuery = searchQuery,
              selectedStatus = statusFilter,
              onSearchChange = { viewModel.setSearchQuery(it) },
              onStatusSelect = { viewModel.setStatusFilter(it) },
              onMemberClick = { memberId -> viewModel.navigateTo(Screen.MemberProfile(memberId)) },
              onCheckInMember = { memberId -> viewModel.checkInMember(memberId) },
              onRenewMember = { memberId ->
                val m = allMembers.find { it.id == memberId }
                viewModel.navigateTo(Screen.RecordPayment(preselectedMemberId = memberId, preselectedPlanId = m?.planId))
              },
              onAddNewMember = { viewModel.navigateTo(Screen.AddEditMember()) }
            )
          }

          is Screen.AddEditMember -> {
            val existing = allMembers.find { it.id == screen.memberId }
            AddEditMemberScreen(
              existingMember = existing,
              plans = plans,
              onBack = { viewModel.navigateTo(Screen.MembersList) },
              onSave = { id, name, phone, email, planId, start, trainer, emergency, notes ->
                viewModel.saveMember(id, name, phone, email, planId, start, trainer, emergency, notes)
              }
            )
          }

          is Screen.MemberProfile -> {
            val member = allMembers.find { it.id == screen.memberId } ?: allMembers.first()
            MemberProfileScreen(
              member = member,
              payments = payments,
              checkIns = checkIns,
              onBack = { viewModel.navigateTo(Screen.MembersList) },
              onEdit = { viewModel.navigateTo(Screen.AddEditMember(member.id)) },
              onRenew = { viewModel.navigateTo(Screen.RecordPayment(preselectedMemberId = member.id, preselectedPlanId = member.planId)) },
              onCheckIn = { viewModel.checkInMember(member.id) },
              onSendReminder = { viewModel.sendExpiryReminder(member.id) }
            )
          }

          is Screen.PlansSetup -> {
            PlansSetupScreen(
              plans = plans,
              members = allMembers,
              onAddPlan = { name, days, price, desc, feats, popular ->
                viewModel.addPlan(name, days, price, desc, feats, popular)
              }
            )
          }

          is Screen.RecordPayment -> {
            RecordPaymentScreen(
              members = allMembers,
              plans = plans,
              preselectedMemberId = screen.preselectedMemberId,
              preselectedPlanId = screen.preselectedPlanId,
              onBack = { viewModel.navigateTo(Screen.Dashboard) },
              onSavePayment = { mId, pId, amt, method, extend, notes ->
                viewModel.recordPayment(mId, pId, amt, method, extend, notes)
              }
            )
          }

          is Screen.CheckIn -> {
            CheckInScreen(
              members = allMembers,
              onCheckIn = { memberId -> viewModel.checkInMember(memberId) },
              onRenewMember = { memberId ->
                val m = allMembers.find { it.id == memberId }
                viewModel.navigateTo(Screen.RecordPayment(preselectedMemberId = memberId, preselectedPlanId = m?.planId))
              }
            )
          }

          is Screen.ExpiringSoon -> {
            ExpiringSoonScreen(
              expiringMembers = expiringMembers,
              onBack = { viewModel.navigateTo(Screen.Dashboard) },
              onMemberClick = { memberId -> viewModel.navigateTo(Screen.MemberProfile(memberId)) },
              onSendReminder = { memberId -> viewModel.sendExpiryReminder(memberId) },
              onRenewMember = { memberId ->
                val m = allMembers.find { it.id == memberId }
                viewModel.navigateTo(Screen.RecordPayment(preselectedMemberId = memberId, preselectedPlanId = m?.planId))
              },
              onSendReminderToAll = {
                expiringMembers.forEach { viewModel.sendExpiryReminder(it.id) }
              }
            )
          }

          is Screen.Login -> {
            // Handled above
          }
        }

        // Check-in animated result dialog
        checkInModal?.let { modal ->
          CheckInResultDialog(
            modal = modal,
            onDismiss = { viewModel.dismissCheckInModal() },
            onRenewMembership = { memberId ->
              val m = allMembers.find { it.id == memberId }
              viewModel.navigateTo(Screen.RecordPayment(preselectedMemberId = memberId, preselectedPlanId = m?.planId))
            }
          )
        }
      }
    }
  }
}
