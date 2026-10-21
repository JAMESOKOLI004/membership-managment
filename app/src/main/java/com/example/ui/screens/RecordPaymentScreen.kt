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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GymMember
import com.example.model.MembershipPlan
import com.example.ui.components.MemberAvatar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed500
import com.example.ui.theme.BorderLight
import com.example.ui.theme.Charcoal800
import com.example.ui.theme.Charcoal900
import com.example.ui.theme.ElectricLime100
import com.example.ui.theme.ElectricLime500
import com.example.ui.theme.ElectricLime900
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordPaymentScreen(
  members: List<GymMember>,
  plans: List<MembershipPlan>,
  preselectedMemberId: String?,
  preselectedPlanId: String?,
  onBack: () -> Unit,
  onSavePayment: (
    memberId: String,
    planId: String,
    amount: Double,
    paymentMethod: String,
    autoExtend: Boolean,
    notes: String?
  ) -> Unit
) {
  var selectedMember by remember {
    mutableStateOf(members.find { it.id == preselectedMemberId } ?: members.firstOrNull())
  }
  var memberSearchQuery by remember { mutableStateOf("") }
  var showMemberSearchMenu by remember { mutableStateOf(false) }

  var selectedPlan by remember {
    mutableStateOf(
      plans.find { it.id == preselectedPlanId }
        ?: plans.find { it.id == selectedMember?.planId }
        ?: plans.firstOrNull() ?: MembershipPlan("PLAN-1", "Standard", 30, 50.0, "", emptyList())
    )
  }
  var planDropdownExpanded by remember { mutableStateOf(false) }

  var amountStr by remember { mutableStateOf(String.format("%.2f", selectedPlan.price)) }
  var dateStr by remember { mutableStateOf("2026-10-21") }
  var paymentMethod by remember { mutableStateOf("Card (Visa/Mastercard)") }
  var autoExtendExpiry by remember { mutableStateOf(true) }
  var notes by remember { mutableStateOf("") }
  var errorMsg by remember { mutableStateOf<String?>(null) }

  val paymentMethods = listOf("Card (Visa/Mastercard)", "Cash", "Apple Pay / Contactless", "Bank Transfer")

  // Calculate simulated preview of new expiry date
  val simulatedNewExpiry = remember(selectedMember, selectedPlan, autoExtendExpiry) {
    if (!autoExtendExpiry || selectedMember == null) null
    else {
      try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val today = sdf.parse("2026-10-21") ?: Date()
        val curExp = sdf.parse(selectedMember!!.expiryDate) ?: today
        val baseDate = if (curExp.after(today)) curExp else today
        val cal = Calendar.getInstance()
        cal.time = baseDate
        cal.add(Calendar.DAY_OF_YEAR, selectedPlan.durationDays)
        sdf.format(cal.time)
      } catch (e: Exception) {
        "2026-11-20"
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Record Payment & Invoice", fontWeight = FontWeight.Bold, color = Color.White) },
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
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Member Search & Selection Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "SELECT MEMBER",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF6B7280),
              letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Search Member Dropdown
            OutlinedTextField(
              value = memberSearchQuery,
              onValueChange = {
                memberSearchQuery = it
                showMemberSearchMenu = it.isNotBlank()
              },
              placeholder = { Text("Search member by name, phone or ID...") },
              leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Charcoal900) },
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("payment_member_search_input"),
              shape = RoundedCornerShape(10.dp)
            )

            if (showMemberSearchMenu) {
              val filtered = members.filter {
                it.name.contains(memberSearchQuery, ignoreCase = true) ||
                    it.phone.contains(memberSearchQuery, ignoreCase = true) ||
                    it.id.contains(memberSearchQuery, ignoreCase = true)
              }
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 8.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .border(1.dp, BorderLight, RoundedCornerShape(8.dp))
                  .background(Color(0xFFF9FAFB))
              ) {
                if (filtered.isEmpty()) {
                  Text("No matching members", modifier = Modifier.padding(12.dp), color = Color(0xFF6B7280), fontSize = 12.sp)
                } else {
                  filtered.take(4).forEach { m ->
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                          selectedMember = m
                          showMemberSearchMenu = false
                          memberSearchQuery = ""
                        }
                        .padding(10.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      MemberAvatar(name = m.name, colorHex = m.avatarColorHex, sizeDp = 32)
                      Spacer(modifier = Modifier.width(10.dp))
                      Column {
                        Text(m.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Charcoal900)
                        Text("${m.id} • ${m.phone} • Exp: ${m.expiryDate}", fontSize = 11.sp, color = Color(0xFF6B7280))
                      }
                    }
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Currently Selected Member Summary
            if (selectedMember != null) {
              val m = selectedMember!!
              val status = m.getCalculatedStatus("2026-10-21")
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .background(Color(0xFFF9FAFB))
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                MemberAvatar(name = m.name, colorHex = m.avatarColorHex, sizeDp = 48)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(m.name, fontWeight = FontWeight.Black, fontSize = 15.sp, color = Charcoal900)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(m.id, fontSize = 11.sp, color = Color(0xFF6B7280), fontWeight = FontWeight.Bold)
                  }
                  Text("Current Expiry: ${m.expiryDate}", fontSize = 12.sp, color = Color(0xFF4B5563))
                }
                StatusBadge(status = status)
              }
            }
          }
        }
      }

      // Plan & Amount Selection Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "PAYMENT & PLAN DETAILS",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF6B7280),
              letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Plan dropdown
            ExposedDropdownMenuBox(
              expanded = planDropdownExpanded,
              onExpandedChange = { planDropdownExpanded = !planDropdownExpanded }
            ) {
              OutlinedTextField(
                value = "${selectedPlan.name} • $${String.format("%.2f", selectedPlan.price)} (${selectedPlan.durationDays}d)",
                onValueChange = {},
                readOnly = true,
                label = { Text("Membership Plan") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = planDropdownExpanded) },
                modifier = Modifier
                  .menuAnchor()
                  .fillMaxWidth()
                  .testTag("payment_plan_dropdown"),
                shape = RoundedCornerShape(10.dp)
              )

              ExposedDropdownMenu(
                expanded = planDropdownExpanded,
                onDismissRequest = { planDropdownExpanded = false }
              ) {
                plans.forEach { plan ->
                  DropdownMenuItem(
                    text = {
                      Column {
                        Text(plan.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("$${String.format("%.2f", plan.price)} • ${plan.durationDays} days", fontSize = 12.sp, color = Color(0xFF6B7280))
                      }
                    },
                    onClick = {
                      selectedPlan = plan
                      amountStr = String.format("%.2f", plan.price)
                      planDropdownExpanded = false
                    }
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amount field
            OutlinedTextField(
              value = amountStr,
              onValueChange = { amountStr = it },
              label = { Text("Amount Due ($) *") },
              leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null, tint = Charcoal900) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("payment_amount_input"),
              shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Payment Date
            OutlinedTextField(
              value = dateStr,
              onValueChange = { dateStr = it },
              label = { Text("Payment Date (YYYY-MM-DD)") },
              leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Charcoal900) },
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("payment_date_input"),
              shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Payment Method Selector
            Text("Payment Method", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Charcoal900)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              paymentMethods.forEach { method ->
                val isSelected = paymentMethod == method
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) ElectricLime100 else Color(0xFFF9FAFB))
                    .border(1.dp, if (isSelected) ElectricLime500 else BorderLight, RoundedCornerShape(8.dp))
                    .clickable { paymentMethod = method }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = if (isSelected) ElectricLime900 else Color(0xFF6B7280),
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = method,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) ElectricLime900 else Charcoal900
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Auto-extends Expiry Date Switch
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (autoExtendExpiry) ElectricLime100.copy(alpha = 0.5f) else Color(0xFFF3F4F6))
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Auto-Extend Expiry Date",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = Charcoal900
                )
                Text(
                  text = if (autoExtendExpiry && simulatedNewExpiry != null)
                    "New Expiry: $simulatedNewExpiry (+${selectedPlan.durationDays} days)"
                  else "Membership expiry date will not change",
                  fontSize = 12.sp,
                  color = if (autoExtendExpiry) ElectricLime900 else Color(0xFF6B7280),
                  fontWeight = if (autoExtendExpiry) FontWeight.Bold else FontWeight.Normal
                )
              }

              Switch(
                checked = autoExtendExpiry,
                onCheckedChange = { autoExtendExpiry = it },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = Charcoal900,
                  checkedTrackColor = ElectricLime500
                )
              )
            }
          }
        }
      }

      if (errorMsg != null) {
        item {
          Text(errorMsg!!, color = AlertRed500, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }

      // Save Button
      item {
        Button(
          onClick = {
            if (selectedMember == null) {
              errorMsg = "Please select a member to record payment for"
              return@Button
            }
            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0) {
              errorMsg = "Please enter a valid payment amount"
              return@Button
            }

            onSavePayment(
              selectedMember!!.id,
              selectedPlan.id,
              amount,
              paymentMethod,
              autoExtendExpiry,
              notes.ifBlank { null }
            )
            onBack()
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Charcoal900,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("save_payment_btn")
        ) {
          Icon(Icons.Default.Check, contentDescription = null, tint = ElectricLime500)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "SAVE & ISSUE RECEIPT ($${amountStr.ifBlank { "0.00" }})",
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
          )
        }
      }
    }
  }
}
