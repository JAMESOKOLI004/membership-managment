package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StaffUser
import com.example.ui.theme.AlertRed500
import com.example.ui.theme.Charcoal800
import com.example.ui.theme.Charcoal900
import com.example.ui.theme.ElectricLime500

@Composable
fun LoginScreen(
  staffList: List<StaffUser>,
  onLogin: (String, String) -> Boolean,
  onQuickEnter: (StaffUser) -> Unit
) {
  var selectedStaff by remember { mutableStateOf(staffList.first()) }
  var pin by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Surface(
    modifier = Modifier.fillMaxSize(),
    color = Charcoal900
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // Bold Brand Header
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(ElectricLime500),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.FitnessCenter,
          contentDescription = "FitAdmin",
          tint = Charcoal900,
          modifier = Modifier.size(48.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "FIT",
          color = Color.White,
          fontWeight = FontWeight.Black,
          fontSize = 32.sp,
          letterSpacing = 1.sp
        )
        Text(
          text = "ADMIN",
          color = ElectricLime500,
          fontWeight = FontWeight.Black,
          fontSize = 32.sp,
          letterSpacing = 1.sp
        )
      }

      Text(
        text = "Front-Desk & Turnstile Terminal",
        color = Color(0xFF9CA3AF),
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
      )

      Spacer(modifier = Modifier.height(32.dp))

      // Staff selector card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Charcoal800)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "SELECT ON-DUTY STAFF",
            color = Color(0xFF9CA3AF),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          staffList.forEach { staff ->
            val isSelected = staff.id == selectedStaff.id
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) ElectricLime500.copy(alpha = 0.15f) else Color.Transparent)
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) ElectricLime500 else Color(0xFF374151),
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                  selectedStaff = staff
                  pin = staff.pin // Auto-fill for ultra smooth front-desk kiosk experience
                  errorMessage = null
                }
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) ElectricLime500 else Color(0xFF4B5563)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = staff.avatarInitials,
                  color = if (isSelected) Charcoal900 else Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = staff.name,
                  color = Color.White,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "${staff.role} • PIN: ${staff.pin}",
                  color = if (isSelected) ElectricLime500 else Color(0xFF9CA3AF),
                  fontSize = 11.sp
                )
              }

              if (isSelected) {
                Box(
                  modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(ElectricLime500)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          OutlinedTextField(
            value = pin,
            onValueChange = {
              if (it.length <= 4) {
                pin = it
                errorMessage = null
              }
            },
            label = { Text("Staff PIN", color = Color(0xFF9CA3AF)) },
            placeholder = { Text("Enter 4-digit PIN", color = Color(0xFF6B7280)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ElectricLime500) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("staff_pin_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedBorderColor = ElectricLime500,
              unfocusedBorderColor = Color(0xFF4B5563),
              focusedLabelColor = ElectricLime500,
              cursorColor = ElectricLime500
            )
          )

          if (errorMessage != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = errorMessage!!,
              color = AlertRed500,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Spacer(modifier = Modifier.height(18.dp))

          Button(
            onClick = {
              val success = onLogin(selectedStaff.id, pin)
              if (!success) {
                errorMessage = "Incorrect PIN for ${selectedStaff.name}. Default is ${selectedStaff.pin}"
              }
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = ElectricLime500,
              contentColor = Charcoal900
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("login_button")
          ) {
            Text(
              text = "LOGIN TO DESK TERMINAL",
              fontSize = 14.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 0.5.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = { onQuickEnter(selectedStaff) },
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF374151),
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("quick_kiosk_entry_btn")
          ) {
            Text(
              text = "⚡ Quick Kiosk Enter (${selectedStaff.name.split(" ").first()})",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
