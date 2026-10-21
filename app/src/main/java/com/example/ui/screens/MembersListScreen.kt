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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import com.example.ui.Screen
import com.example.ui.components.ExpiryCountdownChip
import com.example.ui.components.MemberAvatar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BorderLight
import com.example.ui.theme.Charcoal800
import com.example.ui.theme.Charcoal900
import com.example.ui.theme.ElectricLime500
import com.example.ui.theme.ElectricLime900

@Composable
fun MembersListScreen(
  members: List<GymMember>,
  searchQuery: String,
  selectedStatus: MemberStatus?,
  onSearchChange: (String) -> Unit,
  onStatusSelect: (MemberStatus?) -> Unit,
  onMemberClick: (String) -> Unit,
  onCheckInMember: (String) -> Unit,
  onRenewMember: (String) -> Unit,
  onAddNewMember: () -> Unit
) {
  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = onAddNewMember,
        containerColor = ElectricLime500,
        contentColor = Charcoal900,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.testTag("fab_add_member")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add Member")
          Spacer(modifier = Modifier.width(6.dp))
          Text("+ Add Member", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Color(0xFFF9FAFB))
    ) {
      // Header Section
      Surface(
        color = Color.White,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 1.dp
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "MEMBERS DIRECTORY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B7280),
                letterSpacing = 0.5.sp
              )
              Text(
                text = "Gym Membership Roster",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Charcoal900
              )
            }

            Text(
              text = "${members.size} Total",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF4B5563)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Search Bar
          OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search by name, phone, or ID (e.g. M-1001)", fontSize = 13.sp) },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = "Search", tint = Charcoal900)
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchChange("") }) {
                  Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
              }
            },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("member_search_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color(0xFFF9FAFB),
              unfocusedContainerColor = Color(0xFFF9FAFB),
              focusedBorderColor = Charcoal900,
              unfocusedBorderColor = BorderLight
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Filter Chips
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            FilterTabChip(
              label = "All",
              isSelected = selectedStatus == null,
              onClick = { onStatusSelect(null) }
            )
            FilterTabChip(
              label = "Active",
              isSelected = selectedStatus == MemberStatus.ACTIVE,
              onClick = { onStatusSelect(MemberStatus.ACTIVE) }
            )
            FilterTabChip(
              label = "Expiring (7d)",
              isSelected = selectedStatus == MemberStatus.EXPIRING_SOON,
              onClick = { onStatusSelect(MemberStatus.EXPIRING_SOON) }
            )
            FilterTabChip(
              label = "Expired",
              isSelected = selectedStatus == MemberStatus.EXPIRED,
              onClick = { onStatusSelect(MemberStatus.EXPIRED) }
            )
          }
        }
      }

      // Hybrid Data-Table + Card List
      if (members.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = null,
              tint = Color(0xFF9CA3AF),
              modifier = Modifier.size(54.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "No members match current filter",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Charcoal900
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Try clearing your search query or resetting filters",
              fontSize = 13.sp,
              color = Color(0xFF6B7280)
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(members) { member ->
            MemberCardRow(
              member = member,
              onClick = { onMemberClick(member.id) },
              onCheckIn = { onCheckInMember(member.id) },
              onRenew = { onRenewMember(member.id) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun FilterTabChip(
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) Charcoal900 else Color(0xFFF3F4F6))
      .border(
        width = 1.dp,
        color = if (isSelected) Charcoal900 else BorderLight,
        shape = RoundedCornerShape(8.dp)
      )
      .clickable { onClick() }
      .padding(horizontal = 12.dp, vertical = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = label,
      color = if (isSelected) Color.White else Color(0xFF4B5563),
      fontSize = 12.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
    )
  }
}

@Composable
fun MemberCardRow(
  member: GymMember,
  onClick: () -> Unit,
  onCheckIn: () -> Unit,
  onRenew: () -> Unit
) {
  val status = member.getCalculatedStatus("2026-10-21")
  val daysRemaining = member.getDaysUntilExpiry("2026-10-21")
  var menuExpanded by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("member_row_${member.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        MemberAvatar(
          name = member.name,
          colorHex = member.avatarColorHex,
          sizeDp = 44
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = member.name,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = Charcoal900
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = member.id,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF6B7280)
            )
          }

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = member.phone,
            fontSize = 12.sp,
            color = Color(0xFF4B5563)
          )
        }

        StatusBadge(status = status)

        Box {
          IconButton(onClick = { menuExpanded = true }) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "Options",
              tint = Color(0xFF6B7280)
            )
          }

          DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier.background(Color.White)
          ) {
            DropdownMenuItem(
              text = { Text("View Full Profile", fontWeight = FontWeight.SemiBold) },
              onClick = {
                menuExpanded = false
                onClick()
              }
            )
            DropdownMenuItem(
              text = { Text("⚡ Log Check-In", color = Charcoal900, fontWeight = FontWeight.Bold) },
              onClick = {
                menuExpanded = false
                onCheckIn()
              }
            )
            DropdownMenuItem(
              text = { Text("💳 Renew / Pay", color = ElectricLime900, fontWeight = FontWeight.Bold) },
              onClick = {
                menuExpanded = false
                onRenew()
              }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Data-table info strip
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
          Text(
            text = "PLAN: ",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9CA3AF)
          )
          Text(
            text = member.planName,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Charcoal900
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "EXP: ",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9CA3AF)
          )
          Text(
            text = member.expiryDate,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Charcoal900
          )
          Spacer(modifier = Modifier.width(6.dp))
          ExpiryCountdownChip(daysRemaining = daysRemaining)
        }
      }
    }
  }
}
