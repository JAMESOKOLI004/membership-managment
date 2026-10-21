package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CheckInRecord
import com.example.model.GymMember
import com.example.model.MemberStatus
import com.example.model.PaymentRecord
import com.example.ui.components.ExpiryCountdownChip
import com.example.ui.components.MemberAvatar
import com.example.ui.components.StatusBadge
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

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MemberProfileScreen(
  member: GymMember,
  payments: List<PaymentRecord>,
  checkIns: List<CheckInRecord>,
  onBack: () -> Unit,
  onEdit: () -> Unit,
  onRenew: () -> Unit,
  onCheckIn: () -> Unit,
  onSendReminder: () -> Unit
) {
  var selectedTab by remember { mutableStateOf(0) } // 0 = Payment History, 1 = Check-In History
  val status = member.getCalculatedStatus("2026-10-21")
  val daysRemaining = member.getDaysUntilExpiry("2026-10-21")

  val memberPayments = payments.filter { it.memberId == member.id }
  val memberCheckIns = checkIns.filter { it.memberId == member.id }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Member Profile", fontWeight = FontWeight.Bold, color = Color.White) },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
        },
        actions = {
          IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Member", tint = ElectricLime500)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Charcoal900)
      )
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Color(0xFFF9FAFB)),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Photo Header Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Charcoal900),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              MemberAvatar(
                name = member.name,
                colorHex = member.avatarColorHex,
                sizeDp = 68
              )

              Spacer(modifier = Modifier.width(16.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = member.name,
                  color = Color.White,
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Black
                )
                Text(
                  text = "Member ID: ${member.id}",
                  color = Color(0xFF9CA3AF),
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                StatusBadge(status = status)
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Info Row
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Charcoal800)
                .padding(12.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = ElectricLime500, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(member.phone, color = Color.White, fontSize = 13.sp)
              }
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = null, tint = ElectricLime500, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(member.email.ifBlank { "No email recorded" }, color = Color(0xFFD1D5DB), fontSize = 13.sp)
              }
              if (!member.assignedTrainer.isNullOrBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = ElectricLime500, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Trainer: ${member.assignedTrainer}", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
              }
              if (!member.emergencyContact.isNullOrBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Warning, contentDescription = null, tint = WarningAmber500, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Emergency: ${member.emergencyContact}", color = Color(0xFFE5E7EB), fontSize = 12.sp)
                }
              }
            }
          }
        }
      }

      // Plan Details & Expiry Countdown Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "MEMBERSHIP PLAN & STATUS",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF6B7280),
              letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = member.planName,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Black,
                  color = Charcoal900
                )
                Text(
                  text = "Start: ${member.startDate} • End: ${member.expiryDate}",
                  fontSize = 12.sp,
                  color = Color(0xFF4B5563)
                )
              }

              Text(
                text = "${member.checkInCount} Visited",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricLime900,
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(ElectricLime100)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Expiry countdown banner
            val bannerBg = when (status) {
              MemberStatus.ACTIVE -> Color(0xFFF3F4F6)
              MemberStatus.EXPIRING_SOON -> WarningAmber100
              MemberStatus.EXPIRED -> AlertRed100
            }
            val bannerText = when (status) {
              MemberStatus.ACTIVE -> "Membership in good standing • $daysRemaining days remaining"
              MemberStatus.EXPIRING_SOON -> "⚠️ Attention: Membership expires in $daysRemaining days!"
              MemberStatus.EXPIRED -> "⛔ MEMBERSHIP EXPIRED ${-daysRemaining} DAYS AGO. Access blocked."
            }
            val bannerTextColor = when (status) {
              MemberStatus.ACTIVE -> Charcoal900
              MemberStatus.EXPIRING_SOON -> Color(0xFF78350F)
              MemberStatus.EXPIRED -> AlertRed800
            }

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(bannerBg)
                .padding(12.dp)
            ) {
              Text(
                text = bannerText,
                color = bannerTextColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Prominent Action Buttons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Button(
                onClick = onRenew,
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (status == MemberStatus.EXPIRED) AlertRed500 else ElectricLime500,
                  contentColor = if (status == MemberStatus.EXPIRED) Color.White else Charcoal900
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                  .weight(1.2f)
                  .height(48.dp)
                  .testTag("renew_membership_btn")
              ) {
                Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Renew Membership", fontSize = 13.sp, fontWeight = FontWeight.Black)
              }

              Button(
                onClick = onCheckIn,
                colors = ButtonDefaults.buttonColors(
                  containerColor = Charcoal900,
                  contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                  .weight(1f)
                  .height(48.dp)
                  .testTag("log_check_in_btn")
              ) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(18.dp), tint = ElectricLime500)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Check In", fontSize = 13.sp, fontWeight = FontWeight.Bold)
              }
            }

            if (status == MemberStatus.EXPIRING_SOON || status == MemberStatus.EXPIRED) {
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedButton(
                onClick = onSendReminder,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(42.dp)
                  .testTag("send_profile_reminder_btn")
              ) {
                Text(
                  text = if (member.reminderSentAt != null) "✓ Reminder Sent (${member.reminderSentAt})" else "📱 Send Expiry Reminder via SMS",
                  color = Charcoal900,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }
        }
      }

      // History Tabs
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
        ) {
          Column {
            TabRow(
              selectedTabIndex = selectedTab,
              containerColor = Color.White,
              contentColor = Charcoal900,
              indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                  modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                  color = Charcoal900
                )
              }
            ) {
              Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Payment History (${memberPayments.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
              )
              Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Check-In Logs (${memberCheckIns.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
              )
            }

            Box(modifier = Modifier.padding(14.dp)) {
              if (selectedTab == 0) {
                // Payment History Tab
                if (memberPayments.isEmpty()) {
                  Text(
                    text = "No previous payment records on file.",
                    color = Color(0xFF6B7280),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                  )
                } else {
                  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    memberPayments.forEach { pay ->
                      Row(
                        modifier = Modifier
                          .fillMaxWidth()
                          .clip(RoundedCornerShape(8.dp))
                          .background(Color(0xFFF9FAFB))
                          .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Column {
                          Text(pay.planName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Charcoal900)
                          Text("${pay.date} • ${pay.paymentMethod}", fontSize = 11.sp, color = Color(0xFF6B7280))
                          Text("Receipt: ${pay.receiptNumber}", fontSize = 10.sp, color = Color(0xFF9CA3AF))
                        }
                        Text(
                          text = "$${String.format("%.2f", pay.amount)}",
                          fontWeight = FontWeight.Black,
                          fontSize = 15.sp,
                          color = ElectricLime900
                        )
                      }
                    }
                  }
                }
              } else {
                // Check-In History Tab
                if (memberCheckIns.isEmpty()) {
                  Text(
                    text = "No check-ins logged for this member yet.",
                    color = Color(0xFF6B7280),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                  )
                } else {
                  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    memberCheckIns.forEach { check ->
                      Row(
                        modifier = Modifier
                          .fillMaxWidth()
                          .clip(RoundedCornerShape(8.dp))
                          .background(Color(0xFFF9FAFB))
                          .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Icon(
                          imageVector = if (check.isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                          contentDescription = null,
                          tint = if (check.isSuccess) ElectricLime900 else AlertRed500,
                          modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                          Text(
                            text = "${check.dateFormatted} at ${check.timeFormatted}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal900
                          )
                          Text(
                            text = check.message,
                            fontSize = 11.sp,
                            color = if (check.isSuccess) Color(0xFF4B5563) else AlertRed500
                          )
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
