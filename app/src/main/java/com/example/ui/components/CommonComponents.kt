package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.GymMember
import com.example.model.MemberStatus
import com.example.model.StaffUser
import com.example.ui.CheckInResultModal
import com.example.ui.Screen
import com.example.ui.theme.AlertRed100
import com.example.ui.theme.AlertRed500
import com.example.ui.theme.AlertRed800
import com.example.ui.theme.BorderLight
import com.example.ui.theme.Charcoal800
import com.example.ui.theme.Charcoal900
import com.example.ui.theme.ElectricLime100
import com.example.ui.theme.ElectricLime500
import com.example.ui.theme.ElectricLime900
import com.example.ui.theme.WarningAmber100
import com.example.ui.theme.WarningAmber500
import com.example.ui.theme.WarningAmber900

@Composable
fun FitAdminTopHeader(
  currentStaff: StaffUser,
  staffList: List<StaffUser>,
  onSwitchStaff: (StaffUser) -> Unit,
  onLogout: () -> Unit,
  onNavigateToExpiring: () -> Unit,
  expiringCount: Int
) {
  var menuExpanded by remember { mutableStateOf(false) }

  Surface(
    color = Charcoal900,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(ElectricLime500),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.FitnessCenter,
            contentDescription = "FitAdmin Brand Icon",
            tint = Charcoal900,
            modifier = Modifier.size(24.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "FIT",
              color = Color.White,
              fontWeight = FontWeight.Black,
              fontSize = 18.sp,
              letterSpacing = 1.sp
            )
            Text(
              text = "ADMIN",
              color = ElectricLime500,
              fontWeight = FontWeight.Black,
              fontSize = 18.sp,
              letterSpacing = 1.sp
            )
          }
          Text(
            text = "IronCore Gym • Desk Kiosk",
            color = Color(0xFF9CA3AF),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        // Expiring Soon quick badge
        if (expiringCount > 0) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(WarningAmber500.copy(alpha = 0.2f))
              .border(1.dp, WarningAmber500, RoundedCornerShape(20.dp))
              .clickable { onNavigateToExpiring() }
              .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = "Expiring Soon",
                tint = WarningAmber500,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "$expiringCount Due",
                color = WarningAmber500,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
          Spacer(modifier = Modifier.width(8.dp))
        }

        // Staff profile pill & switcher
        Box {
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(24.dp))
              .background(Charcoal800)
              .border(1.dp, Color(0xFF374151), RoundedCornerShape(24.dp))
              .clickable { menuExpanded = true }
              .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(ElectricLime500),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = currentStaff.avatarInitials,
                color = Charcoal900,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Text(
                text = currentStaff.name.split(" ").first(),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "Shift Active",
                color = ElectricLime500,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }

          DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier.background(Charcoal800)
          ) {
            Text(
              text = "SWITCH SHIFT / DESK STAFF",
              color = Color(0xFF9CA3AF),
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            staffList.forEach { staff ->
              DropdownMenuItem(
                text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (staff.id == currentStaff.id) ElectricLime500 else Color(0xFF4B5563)),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = staff.avatarInitials,
                        color = if (staff.id == currentStaff.id) Charcoal900 else Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(
                        text = staff.name,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = if (staff.id == currentStaff.id) FontWeight.Bold else FontWeight.Normal
                      )
                      Text(
                        text = staff.role,
                        color = Color(0xFF9CA3AF),
                        fontSize = 11.sp
                      )
                    }
                  }
                },
                onClick = {
                  menuExpanded = false
                  onSwitchStaff(staff)
                }
              )
            }
            DropdownMenuItem(
              text = {
                Text(
                  text = "Lock Terminal / Logout",
                  color = AlertRed500,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
              },
              onClick = {
                menuExpanded = false
                onLogout()
              }
            )
          }
        }
      }
    }
  }
}

@Composable
fun StatusBadge(status: MemberStatus) {
  val (bgColor, textColor, borderC) = when (status) {
    MemberStatus.ACTIVE -> Triple(ElectricLime100, ElectricLime900, ElectricLime500)
    MemberStatus.EXPIRING_SOON -> Triple(WarningAmber100, WarningAmber900, WarningAmber500)
    MemberStatus.EXPIRED -> Triple(AlertRed100, AlertRed800, AlertRed500)
  }

  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(bgColor)
      .border(1.dp, borderC.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
      .padding(horizontal = 8.dp, vertical = 3.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = status.label.uppercase(),
      color = textColor,
      fontSize = 10.sp,
      fontWeight = FontWeight.ExtraBold,
      letterSpacing = 0.5.sp
    )
  }
}

@Composable
fun MemberAvatar(
  name: String,
  colorHex: Long,
  sizeDp: Int = 42
) {
  val initials = name.split(" ")
    .mapNotNull { it.firstOrNull()?.toString() }
    .take(2)
    .joinToString("")
    .uppercase()

  Box(
    modifier = Modifier
      .size(sizeDp.dp)
      .clip(CircleShape)
      .background(Color(colorHex)),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = initials.ifBlank { "M" },
      color = Color.White,
      fontSize = (sizeDp * 0.38).sp,
      fontWeight = FontWeight.Bold
    )
  }
}

@Composable
fun ExpiryCountdownChip(
  daysRemaining: Long,
  modifier: Modifier = Modifier
) {
  val (color, text) = when {
    daysRemaining < 0 -> Pair(AlertRed500, "Expired ${-daysRemaining}d ago")
    daysRemaining == 0L -> Pair(AlertRed500, "Expires Today!")
    daysRemaining == 1L -> Pair(WarningAmber500, "Expires Tomorrow")
    daysRemaining <= 7L -> Pair(WarningAmber500, "$daysRemaining days left")
    else -> Pair(ElectricLime900, "$daysRemaining days left")
  }

  Text(
    text = text,
    color = color,
    fontSize = 11.sp,
    fontWeight = FontWeight.SemiBold,
    modifier = modifier
  )
}

@Composable
fun FitAdminStatCard(
  title: String,
  value: String,
  subtitle: String,
  icon: ImageVector,
  accentColor: Color,
  onClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title.uppercase(),
          color = Color(0xFF6B7280),
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(accentColor.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = accentColor,
            modifier = Modifier.size(18.dp)
          )
        }
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = value,
        color = Charcoal900,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = subtitle,
        color = Color(0xFF4B5563),
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
      )
    }
  }
}

@Composable
fun CheckInResultDialog(
  modal: CheckInResultModal,
  onDismiss: () -> Unit,
  onRenewMembership: (String) -> Unit
) {
  val scaleAnim = remember { Animatable(0.7f) }

  LaunchedEffect(modal) {
    scaleAnim.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
    )
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .scale(scaleAnim.value)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (modal.isSuccess) {
          // Success State - Electric Lime
          Box(
            modifier = Modifier
              .size(76.dp)
              .clip(CircleShape)
              .background(ElectricLime100)
              .border(3.dp, ElectricLime500, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Success Check In",
              tint = ElectricLime500,
              modifier = Modifier.size(46.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "CHECK-IN SUCCESSFUL",
            color = Charcoal900,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "Turnstile #1 Unlocked • Access Granted",
            color = ElectricLime900,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )

          Spacer(modifier = Modifier.height(18.dp))

          // Member Info Card
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              MemberAvatar(
                name = modal.member.name,
                colorHex = modal.member.avatarColorHex,
                sizeDp = 46
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = modal.member.name,
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = Charcoal900
                )
                Text(
                  text = "${modal.member.planName} • ID: ${modal.member.id}",
                  fontSize = 12.sp,
                  color = Color(0xFF4B5563)
                )
                Text(
                  text = "${modal.member.checkInCount} check-ins logged this cycle",
                  fontSize = 11.sp,
                  color = ElectricLime900,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }

          if (modal.daysRemaining in 0..7) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(WarningAmber100)
                .padding(10.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Warning,
                  contentDescription = "Notice",
                  tint = WarningAmber900,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Note for desk: Membership expires in ${modal.daysRemaining} days (${modal.member.expiryDate}).",
                  color = WarningAmber900,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(
              containerColor = Charcoal900,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("dismiss_check_in_btn")
          ) {
            Text("Done (Ready for Next)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          }

        } else {
          // Blocked Expired State - High alert Red
          Box(
            modifier = Modifier
              .size(76.dp)
              .clip(CircleShape)
              .background(AlertRed100)
              .border(3.dp, AlertRed500, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Block,
              contentDescription = "Blocked Check In",
              tint = AlertRed500,
              modifier = Modifier.size(46.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "CHECK-IN BLOCKED",
            color = AlertRed800,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "MEMBERSHIP HAS EXPIRED",
            color = AlertRed500,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(18.dp))

          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = AlertRed100.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                MemberAvatar(
                  name = modal.member.name,
                  colorHex = modal.member.avatarColorHex,
                  sizeDp = 44
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = modal.member.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Charcoal900
                  )
                  Text(
                    text = "ID: ${modal.member.id} • ${modal.member.phone}",
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "Last Plan: ${modal.member.planName}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Charcoal900
              )
              Text(
                text = "Expired on: ${modal.member.expiryDate} (${-modal.daysRemaining} days overdue)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AlertRed800
              )
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = {
              onDismiss()
              onRenewMembership(modal.member.id)
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = AlertRed500,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("renew_expired_member_btn")
          ) {
            Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Renew Membership Now", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
          ) {
            Text("Dismiss Entry", color = Charcoal900, fontWeight = FontWeight.Medium)
          }
        }
      }
    }
  }
}

@Composable
fun FitAdminBottomNavBar(
  currentScreen: Screen,
  onNavigate: (Screen) -> Unit,
  expiringCount: Int
) {
  val selectedTab = when (currentScreen) {
    is Screen.Dashboard -> 0
    is Screen.CheckIn -> 1
    is Screen.MembersList, is Screen.MemberProfile, is Screen.AddEditMember -> 2
    is Screen.RecordPayment -> 3
    is Screen.PlansSetup, is Screen.ExpiringSoon -> 4
    else -> 0
  }

  NavigationBar(
    containerColor = Charcoal900,
    contentColor = Color.White
  ) {
    NavigationBarItem(
      icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
      label = { Text("Dashboard", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
      selected = selectedTab == 0,
      onClick = { onNavigate(Screen.Dashboard) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Charcoal900,
        selectedTextColor = ElectricLime500,
        indicatorColor = ElectricLime500,
        unselectedIconColor = Color(0xFF9CA3AF),
        unselectedTextColor = Color(0xFF9CA3AF)
      )
    )

    NavigationBarItem(
      icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = "Check-In") },
      label = { Text("Check-In", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
      selected = selectedTab == 1,
      onClick = { onNavigate(Screen.CheckIn) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Charcoal900,
        selectedTextColor = ElectricLime500,
        indicatorColor = ElectricLime500,
        unselectedIconColor = Color(0xFF9CA3AF),
        unselectedTextColor = Color(0xFF9CA3AF)
      )
    )

    NavigationBarItem(
      icon = { Icon(Icons.Default.People, contentDescription = "Members") },
      label = { Text("Members", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
      selected = selectedTab == 2,
      onClick = { onNavigate(Screen.MembersList) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Charcoal900,
        selectedTextColor = ElectricLime500,
        indicatorColor = ElectricLime500,
        unselectedIconColor = Color(0xFF9CA3AF),
        unselectedTextColor = Color(0xFF9CA3AF)
      )
    )

    NavigationBarItem(
      icon = { Icon(Icons.Default.CreditCard, contentDescription = "Payments") },
      label = { Text("Payments", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
      selected = selectedTab == 3,
      onClick = { onNavigate(Screen.RecordPayment()) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Charcoal900,
        selectedTextColor = ElectricLime500,
        indicatorColor = ElectricLime500,
        unselectedIconColor = Color(0xFF9CA3AF),
        unselectedTextColor = Color(0xFF9CA3AF)
      )
    )

    NavigationBarItem(
      icon = {
        Box {
          Icon(Icons.Default.FitnessCenter, contentDescription = "Plans")
          if (expiringCount > 0) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(WarningAmber500)
                .align(Alignment.TopEnd)
            )
          }
        }
      },
      label = { Text("Plans", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
      selected = selectedTab == 4,
      onClick = { onNavigate(Screen.PlansSetup) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Charcoal900,
        selectedTextColor = ElectricLime500,
        indicatorColor = ElectricLime500,
        unselectedIconColor = Color(0xFF9CA3AF),
        unselectedTextColor = Color(0xFF9CA3AF)
      )
    )
  }
}
