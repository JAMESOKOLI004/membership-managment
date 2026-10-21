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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import com.example.model.GymMember
import com.example.model.MemberStatus
import com.example.ui.components.ExpiryCountdownChip
import com.example.ui.components.MemberAvatar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed500
import com.example.ui.theme.BorderLight
import com.example.ui.theme.Charcoal800
import com.example.ui.theme.Charcoal900
import com.example.ui.theme.ElectricLime500
import com.example.ui.theme.ElectricLime900

@Composable
fun CheckInScreen(
  members: List<GymMember>,
  onCheckIn: (String) -> Unit,
  onRenewMember: (String) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedMember by remember { mutableStateOf<GymMember?>(members.firstOrNull()) }

  val filteredMembers = remember(searchQuery, members) {
    if (searchQuery.isBlank()) emptyList()
    else {
      members.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
            it.phone.contains(searchQuery, ignoreCase = true) ||
            it.id.contains(searchQuery, ignoreCase = true)
      }
    }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF9FAFB)),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Charcoal900)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "TURNSTILE VERIFICATION",
            color = ElectricLime500,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
          )
          Text(
            text = "Desk Member Check-In",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Scan RFID tag, barcode, or lookup member to unlock turnstile gate.",
            color = Color(0xFF9CA3AF),
            fontSize = 12.sp
          )
        }
      }
    }

    // Search / Scanner Bar
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "SEARCH OR SCAN MEMBER",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6B7280),
            letterSpacing = 0.5.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search name, phone, or ID (e.g. M-1001)...") },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = "Search", tint = Charcoal900)
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                  Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
              }
            },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("check_in_search_input"),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color(0xFFF9FAFB),
              unfocusedContainerColor = Color(0xFFF9FAFB),
              focusedBorderColor = Charcoal900,
              unfocusedBorderColor = BorderLight
            )
          )

          // Dropdown matches
          if (filteredMembers.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, BorderLight, RoundedCornerShape(8.dp))
                .background(Color(0xFFF9FAFB))
            ) {
              filteredMembers.take(4).forEach { m ->
                val status = m.getCalculatedStatus("2026-10-21")
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                      selectedMember = m
                      searchQuery = ""
                    }
                    .padding(10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  MemberAvatar(name = m.name, colorHex = m.avatarColorHex, sizeDp = 34)
                  Spacer(modifier = Modifier.width(10.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text(m.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Charcoal900)
                    Text("${m.id} • ${m.planName}", fontSize = 11.sp, color = Color(0xFF6B7280))
                  }
                  StatusBadge(status = status)
                }
              }
            }
          }
        }
      }
    }

    // Selected Member Focus Card
    item {
      if (selectedMember != null) {
        val m = selectedMember!!
        val status = m.getCalculatedStatus("2026-10-21")
        val daysRemaining = m.getDaysUntilExpiry("2026-10-21")

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(2.dp, if (status == MemberStatus.EXPIRED) AlertRed500 else ElectricLime500),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              MemberAvatar(name = m.name, colorHex = m.avatarColorHex, sizeDp = 56)
              Spacer(modifier = Modifier.width(14.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(m.name, fontSize = 18.sp, fontWeight = FontWeight.Black, color = Charcoal900)
                Text("ID: ${m.id} • ${m.phone}", fontSize = 12.sp, color = Color(0xFF6B7280))
                Spacer(modifier = Modifier.height(2.dp))
                StatusBadge(status = status)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Plan & Expiry strip
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF9FAFB))
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text("CURRENT PLAN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9CA3AF))
                Text(m.planName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Charcoal900)
              }
              Column(horizontalAlignment = Alignment.End) {
                Text("EXPIRES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9CA3AF))
                ExpiryCountdownChip(daysRemaining = daysRemaining)
              }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // One-Tap Check In CTA
            Button(
              onClick = { onCheckIn(m.id) },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (status == MemberStatus.EXPIRED) AlertRed500 else ElectricLime500,
                contentColor = if (status == MemberStatus.EXPIRED) Color.White else Charcoal900
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("one_tap_check_in_btn")
            ) {
              Icon(
                imageVector = Icons.Default.FlashOn,
                contentDescription = null,
                tint = if (status == MemberStatus.EXPIRED) Color.White else Charcoal900,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (status == MemberStatus.EXPIRED) "VERIFY ENTRY (EXPIRED)" else "⚡ ONE-TAP CHECK IN",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
              )
            }
          }
        }
      }
    }

    // Quick Simulation / Test Presets for Front Desk
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "QUICK SIMULATION SHORTCUTS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6B7280),
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(10.dp))

          // Preset 1: Active Member
          PresetMemberRow(
            label = "Active VIP Pass (Marcus Vance)",
            subtitle = "Valid until Nov 01 • Turnstile will unlock",
            color = ElectricLime900,
            onClick = {
              selectedMember = members.find { it.id == "M-1001" } ?: members.first()
              onCheckIn(selectedMember!!.id)
            }
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Preset 2: Expired Member (Blocks with Warning)
          PresetMemberRow(
            label = "Expired Pass (Tyler Brooks - Oct 12)",
            subtitle = "Expired 9d ago • Will block & offer renewal",
            color = AlertRed500,
            onClick = {
              selectedMember = members.find { it.id == "M-1005" } ?: members.first()
              onCheckIn(selectedMember!!.id)
            }
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Preset 3: Expiring Soon
          PresetMemberRow(
            label = "Expiring Tomorrow (Jessica Chen)",
            subtitle = "Expires Oct 22 • Turnstile unlocks + alert",
            color = Color(0xFFF59E0B),
            onClick = {
              selectedMember = members.find { it.id == "M-1004" } ?: members.first()
              onCheckIn(selectedMember!!.id)
            }
          )
        }
      }
    }
  }
}

@Composable
fun PresetMemberRow(
  label: String,
  subtitle: String,
  color: Color,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFFF9FAFB))
      .border(1.dp, BorderLight, RoundedCornerShape(8.dp))
      .clickable { onClick() }
      .padding(10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(10.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Charcoal900)
      Text(subtitle, fontSize = 11.sp, color = Color(0xFF6B7280))
    }
    Text("Test", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Charcoal900)
  }
}
