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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GymMember
import com.example.model.MemberStatus
import com.example.ui.components.ExpiryCountdownChip
import com.example.ui.components.MemberAvatar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed500
import com.example.ui.theme.BorderLight
import com.example.ui.theme.Charcoal900
import com.example.ui.theme.ElectricLime500
import com.example.ui.theme.ElectricLime900
import com.example.ui.theme.WarningAmber100
import com.example.ui.theme.WarningAmber500

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ExpiringSoonScreen(
  expiringMembers: List<GymMember>,
  onBack: () -> Unit,
  onMemberClick: (String) -> Unit,
  onSendReminder: (String) -> Unit,
  onRenewMember: (String) -> Unit,
  onSendReminderToAll: () -> Unit
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Expiring Memberships", fontWeight = FontWeight.Bold, color = Color.White) },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
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
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Banner Alert
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = WarningAmber100),
          border = androidx.compose.foundation.BorderStroke(1.dp, WarningAmber500)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Warning, contentDescription = null, tint = WarningAmber500, modifier = Modifier.size(24.dp))
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "${expiringMembers.size} MEMBERS DUE FOR RENEWAL",
                  fontWeight = FontWeight.Black,
                  fontSize = 14.sp,
                  color = Color(0xFF78350F)
                )
                Text(
                  text = "Memberships expiring within 7 days or recently lapsed.",
                  fontSize = 12.sp,
                  color = Color(0xFF92400E)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = onSendReminderToAll,
              colors = ButtonDefaults.buttonColors(
                containerColor = Charcoal900,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("send_all_reminders_btn")
            ) {
              Icon(Icons.Default.Send, contentDescription = null, tint = ElectricLime500, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Send Automated SMS Reminders to All", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        }
      }

      // Expiring Members List
      if (expiringMembers.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            contentAlignment = Alignment.Center
          ) {
            Text("No members expiring within 7 days!", fontWeight = FontWeight.Bold, color = Color(0xFF6B7280))
          }
        }
      } else {
        items(expiringMembers) { member ->
          ExpiringMemberCard(
            member = member,
            onClick = { onMemberClick(member.id) },
            onSendReminder = { onSendReminder(member.id) },
            onRenew = { onRenewMember(member.id) }
          )
        }
      }
    }
  }
}

@Composable
fun ExpiringMemberCard(
  member: GymMember,
  onClick: () -> Unit,
  onSendReminder: () -> Unit,
  onRenew: () -> Unit
) {
  val daysRemaining = member.getDaysUntilExpiry("2026-10-21")
  val status = member.getCalculatedStatus("2026-10-21")

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("expiring_member_${member.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        MemberAvatar(name = member.name, colorHex = member.avatarColorHex, sizeDp = 46)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(member.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Charcoal900)
            Spacer(modifier = Modifier.width(6.dp))
            Text(member.id, fontSize = 11.sp, color = Color(0xFF6B7280))
          }
          Text(
            text = "${member.planName} • ${member.phone}",
            fontSize = 12.sp,
            color = Color(0xFF4B5563)
          )
        }

        StatusBadge(status = status)
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Expiry details strip
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFF9FAFB))
          .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text("EXPIRES: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9CA3AF))
          Text(member.expiryDate, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Charcoal900)
        }
        ExpiryCountdownChip(daysRemaining = daysRemaining)
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Action Row: Send Reminder & Renew
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = onSendReminder,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .testTag("send_reminder_btn_${member.id}")
        ) {
          Icon(
            imageVector = if (member.reminderSentAt != null) Icons.Default.Check else Icons.Default.Send,
            contentDescription = null,
            tint = if (member.reminderSentAt != null) ElectricLime900 else Charcoal900,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (member.reminderSentAt != null) "Sent" else "Send Reminder",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Charcoal900
          )
        }

        Button(
          onClick = onRenew,
          colors = ButtonDefaults.buttonColors(
            containerColor = Charcoal900,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .testTag("renew_btn_${member.id}")
        ) {
          Icon(Icons.Default.CreditCard, contentDescription = null, tint = ElectricLime500, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Renew Pass", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
