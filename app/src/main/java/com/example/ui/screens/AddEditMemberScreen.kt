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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.model.GymMember
import com.example.model.MembershipPlan
import com.example.ui.components.MemberAvatar
import com.example.ui.theme.AlertRed500
import com.example.ui.theme.BorderLight
import com.example.ui.theme.Charcoal900
import com.example.ui.theme.ElectricLime500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMemberScreen(
  existingMember: GymMember?,
  plans: List<MembershipPlan>,
  onBack: () -> Unit,
  onSave: (
    existingId: String?,
    name: String,
    phone: String,
    email: String,
    planId: String,
    startDate: String,
    assignedTrainer: String?,
    emergencyContact: String?,
    notes: String?
  ) -> Unit
) {
  var name by remember { mutableStateOf(existingMember?.name ?: "") }
  var phone by remember { mutableStateOf(existingMember?.phone ?: "") }
  var email by remember { mutableStateOf(existingMember?.email ?: "") }
  var selectedPlan by remember {
    mutableStateOf(plans.find { it.id == existingMember?.planId } ?: plans.firstOrNull() ?: MembershipPlan("PLAN-1", "Standard", 30, 50.0, "", emptyList()))
  }
  var startDate by remember { mutableStateOf(existingMember?.startDate ?: "2026-10-21") }
  var assignedTrainer by remember { mutableStateOf(existingMember?.assignedTrainer ?: "None") }
  var emergencyContact by remember { mutableStateOf(existingMember?.emergencyContact ?: "") }
  var notes by remember { mutableStateOf(existingMember?.notes ?: "") }

  var planDropdownExpanded by remember { mutableStateOf(false) }
  var trainerDropdownExpanded by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val trainers = listOf("None", "Marcus Cole (Strength)", "Elena Rostova (HIIT/Spin)", "David Chen (Mobility/Yoga)")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (existingMember != null) "Edit Member Profile" else "Add New Member",
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = Color.White
          )
        },
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
      // Photo Header Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            MemberAvatar(
              name = name.ifBlank { "Member" },
              colorHex = existingMember?.avatarColorHex ?: 0xFF111827,
              sizeDp = 64
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
              Text(
                text = if (name.isBlank()) "New Member Photo" else name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Charcoal900
              )
              Text(
                text = "ID: ${existingMember?.id ?: "Auto-Generated (M-XXXX)"}",
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
              )
              Text(
                text = "High-contrast avatar generated automatically",
                fontSize = 11.sp,
                color = Color(0xFF9CA3AF)
              )
            }
          }
        }
      }

      // Member Basic Info Form
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "BASIC INFORMATION",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF6B7280),
              letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = name,
              onValueChange = {
                name = it
                errorMessage = null
              },
              label = { Text("Full Name *") },
              placeholder = { Text("e.g. Alex Henderson") },
              leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Charcoal900) },
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("member_name_input"),
              shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = phone,
              onValueChange = {
                phone = it
                errorMessage = null
              },
              label = { Text("Phone Number *") },
              placeholder = { Text("(555) 000-0000") },
              leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Charcoal900) },
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("member_phone_input"),
              shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = email,
              onValueChange = { email = it },
              label = { Text("Email Address") },
              placeholder = { Text("member@example.com") },
              leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Charcoal900) },
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("member_email_input"),
              shape = RoundedCornerShape(10.dp)
            )
          }
        }
      }

      // Membership Plan Selection
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "MEMBERSHIP PLAN & DURATION",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF6B7280),
              letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
              expanded = planDropdownExpanded,
              onExpandedChange = { planDropdownExpanded = !planDropdownExpanded }
            ) {
              OutlinedTextField(
                value = "${selectedPlan.name} • $${String.format("%.2f", selectedPlan.price)} (${selectedPlan.durationDays} days)",
                onValueChange = {},
                readOnly = true,
                label = { Text("Membership Plan") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = planDropdownExpanded) },
                modifier = Modifier
                  .menuAnchor()
                  .fillMaxWidth()
                  .testTag("member_plan_dropdown"),
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
                        Text(
                          text = plan.name,
                          fontWeight = FontWeight.Bold,
                          fontSize = 14.sp
                        )
                        Text(
                          text = "$${String.format("%.2f", plan.price)} • ${plan.durationDays} days • ${plan.description}",
                          fontSize = 12.sp,
                          color = Color(0xFF6B7280)
                        )
                      }
                    },
                    onClick = {
                      selectedPlan = plan
                      planDropdownExpanded = false
                    }
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = startDate,
              onValueChange = { startDate = it },
              label = { Text("Start Date (YYYY-MM-DD)") },
              leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Charcoal900) },
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("member_start_date_input"),
              shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "Plan duration (${selectedPlan.durationDays} days) will automatically set expiration date.",
              fontSize = 12.sp,
              color = Color(0xFF4B5563)
            )
          }
        }
      }

      // Trainer & Emergency Contact
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "ASSIGNED TRAINER & DETAILS",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF6B7280),
              letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
              expanded = trainerDropdownExpanded,
              onExpandedChange = { trainerDropdownExpanded = !trainerDropdownExpanded }
            ) {
              OutlinedTextField(
                value = assignedTrainer,
                onValueChange = {},
                readOnly = true,
                label = { Text("Assigned Trainer (Optional)") },
                leadingIcon = { Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Charcoal900) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = trainerDropdownExpanded) },
                modifier = Modifier
                  .menuAnchor()
                  .fillMaxWidth()
                  .testTag("member_trainer_dropdown"),
                shape = RoundedCornerShape(10.dp)
              )

              ExposedDropdownMenu(
                expanded = trainerDropdownExpanded,
                onDismissRequest = { trainerDropdownExpanded = false }
              ) {
                trainers.forEach { trainer ->
                  DropdownMenuItem(
                    text = { Text(trainer) },
                    onClick = {
                      assignedTrainer = trainer
                      trainerDropdownExpanded = false
                    }
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = emergencyContact,
              onValueChange = { emergencyContact = it },
              label = { Text("Emergency Contact (Optional)") },
              placeholder = { Text("(555) 123-4567 (Relationship - Name)") },
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("member_emergency_input"),
              shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = notes,
              onValueChange = { notes = it },
              label = { Text("Desk Notes (Optional)") },
              placeholder = { Text("Locker reservation, medical notes, goals...") },
              modifier = Modifier.fillMaxWidth().testTag("member_notes_input"),
              maxLines = 3,
              shape = RoundedCornerShape(10.dp)
            )
          }
        }
      }

      // Error message
      if (errorMessage != null) {
        item {
          Text(
            text = errorMessage!!,
            color = AlertRed500,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp)
          )
        }
      }

      // Save Button
      item {
        Button(
          onClick = {
            if (name.isBlank()) {
              errorMessage = "Please enter member's full name"
              return@Button
            }
            if (phone.isBlank()) {
              errorMessage = "Please enter member's contact phone number"
              return@Button
            }
            onSave(
              existingMember?.id,
              name.trim(),
              phone.trim(),
              email.trim(),
              selectedPlan.id,
              startDate.trim(),
              if (assignedTrainer == "None") null else assignedTrainer,
              emergencyContact.trim().ifBlank { null },
              notes.trim().ifBlank { null }
            )
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Charcoal900,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("save_member_btn")
        ) {
          Icon(Icons.Default.Check, contentDescription = null, tint = ElectricLime500)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (existingMember != null) "SAVE MEMBER CHANGES" else "CREATE GYM MEMBER",
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
          )
        }
      }
    }
  }
}
