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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.example.ui.DashboardStats
import com.example.ui.Screen
import com.example.ui.components.FitAdminStatCard
import com.example.ui.theme.AlertRed500
import com.example.ui.theme.BorderLight
import com.example.ui.theme.Charcoal800
import com.example.ui.theme.Charcoal900
import com.example.ui.theme.ElectricLime500
import com.example.ui.theme.ElectricLime900
import com.example.ui.theme.WarningAmber500

@Composable
fun DashboardScreen(
  stats: DashboardStats,
  recentCheckIns: List<CheckInRecord>,
  onNavigate: (Screen) -> Unit,
  onQuickSearchSubmit: (String) -> Unit
) {
  var quickSearchText by remember { mutableStateOf("") }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF9FAFB)),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Hero Banner with Giant Prominent Quick "Check-In" Button
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Charcoal900),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "FRONT-DESK TERMINAL",
                color = ElectricLime500,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
              )
              Text(
                text = "Live Floor Control",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF1F2937))
                .border(1.dp, Color(0xFF374151), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(ElectricLime500)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Turnstiles Online",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Big Energetic Check-In CTA Button
          Button(
            onClick = { onNavigate(Screen.CheckIn) },
            colors = ButtonDefaults.buttonColors(
              containerColor = ElectricLime500,
              contentColor = Charcoal900
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(56.dp)
              .testTag("prominent_check_in_btn")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Charcoal900
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "⚡ QUICK SCAN / CHECK-IN",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
              )
            }
          }
        }
      }
    }

    // 4 Stat Cards Grid (2x2)
    item {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          FitAdminStatCard(
            title = "Active Members",
            value = stats.activeMembersCount.toString(),
            subtitle = "${stats.activeMembersCount + stats.expiringThisWeekCount} in good standing",
            icon = Icons.Default.People,
            accentColor = ElectricLime500,
            onClick = { onNavigate(Screen.MembersList) },
            modifier = Modifier.weight(1f).testTag("stat_active_members")
          )

          FitAdminStatCard(
            title = "Expiring This Week",
            value = stats.expiringThisWeekCount.toString(),
            subtitle = if (stats.expiringThisWeekCount > 0) "⚠️ Urgent review" else "All clear",
            icon = Icons.Default.Warning,
            accentColor = WarningAmber500,
            onClick = { onNavigate(Screen.ExpiringSoon) },
            modifier = Modifier.weight(1f).testTag("stat_expiring_week")
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          FitAdminStatCard(
            title = "Oct Revenue",
            value = "$${String.format("%,.0f", stats.revenueThisMonth)}",
            subtitle = "+8.4% vs last mo",
            icon = Icons.Default.AttachMoney,
            accentColor = Color(0xFF10B981),
            onClick = { onNavigate(Screen.RecordPayment()) },
            modifier = Modifier.weight(1f).testTag("stat_revenue_month")
          )

          FitAdminStatCard(
            title = "Check-ins Today",
            value = stats.checkInsTodayCount.toString(),
            subtitle = if (stats.blockedCheckInsTodayCount > 0) "${stats.blockedCheckInsTodayCount} blocked" else "All verified",
            icon = Icons.Default.CheckCircle,
            accentColor = Charcoal900,
            onClick = { onNavigate(Screen.CheckIn) },
            modifier = Modifier.weight(1f).testTag("stat_checkins_today")
          )
        }
      }
    }

    // Quick Action Bar
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = { onNavigate(Screen.AddEditMember()) },
          colors = ButtonDefaults.buttonColors(
            containerColor = Charcoal900,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("quick_add_member_btn")
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("+ Add Member", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = { onNavigate(Screen.RecordPayment()) },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Charcoal900
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("quick_record_payment_btn")
        ) {
          Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Record Pay", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = { onNavigate(Screen.ExpiringSoon) },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFEF3C7),
            contentColor = Color(0xFF78350F)
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, WarningAmber500.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("quick_expiring_soon_btn")
        ) {
          Icon(Icons.Default.EventNote, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Expiring", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    // Recent Check-in Feed Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "TODAY'S CHECK-IN LOG",
          color = Charcoal900,
          fontSize = 13.sp,
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.5.sp
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clickable { onNavigate(Screen.CheckIn) }
            .padding(4.dp)
        ) {
          Text(
            text = "Open Kiosk",
            color = Charcoal900,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = Charcoal900,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }

    // Recent Check-ins List
    items(recentCheckIns.take(6)) { record ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(if (record.isSuccess) Color(0xFFECFCCB) else Color(0xFFFEE2E2)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (record.isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
              contentDescription = null,
              tint = if (record.isSuccess) ElectricLime900 else AlertRed500,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = record.memberName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Charcoal900
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "(${record.memberId})",
                fontSize = 11.sp,
                color = Color(0xFF6B7280)
              )
            }
            Text(
              text = record.message,
              fontSize = 12.sp,
              color = if (record.isSuccess) Color(0xFF4B5563) else AlertRed500,
              fontWeight = if (record.isSuccess) FontWeight.Normal else FontWeight.SemiBold
            )
          }

          Text(
            text = record.timeFormatted,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6B7280)
          )
        }
      }
    }
  }
}
