package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.GymMember
import com.example.model.MembershipPlan
import com.example.ui.theme.AlertRed500
import com.example.ui.theme.BorderLight
import com.example.ui.theme.Charcoal800
import com.example.ui.theme.Charcoal900
import com.example.ui.theme.ElectricLime100
import com.example.ui.theme.ElectricLime500
import com.example.ui.theme.ElectricLime900

@Composable
fun PlansSetupScreen(
  plans: List<MembershipPlan>,
  members: List<GymMember>,
  onAddPlan: (name: String, durationDays: Int, price: Double, description: String, features: List<String>, isPopular: Boolean) -> Unit
) {
  var showAddDialog by remember { mutableStateOf(false) }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAddDialog = true },
        containerColor = ElectricLime500,
        contentColor = Charcoal900,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.testTag("fab_add_plan")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add Plan")
          Spacer(modifier = Modifier.width(6.dp))
          Text("+ Add Plan", fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
      // Header
      Surface(
        color = Color.White,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 1.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "PLANS & PRICING SETUP",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF6B7280),
              letterSpacing = 0.5.sp
            )
            Text(
              text = "Gym Membership Tiers",
              fontSize = 18.sp,
              fontWeight = FontWeight.Black,
              color = Charcoal900
            )
          }

          Button(
            onClick = { showAddDialog = true },
            colors = ButtonDefaults.buttonColors(
              containerColor = Charcoal900,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.height(40.dp).testTag("header_add_plan_btn")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = ElectricLime500)
            Spacer(modifier = Modifier.width(4.dp))
            Text("+ Add Plan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      // Plans List
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(plans) { plan ->
          val subscriberCount = members.count { it.planId == plan.id }
          PlanCardItem(plan = plan, subscriberCount = subscriberCount)
        }
      }
    }
  }

  if (showAddDialog) {
    AddPlanDialog(
      onDismiss = { showAddDialog = false },
      onSave = { name, days, price, desc, feats, popular ->
        onAddPlan(name, days, price, desc, feats, popular)
        showAddDialog = false
      }
    )
  }
}

@Composable
fun PlanCardItem(
  plan: MembershipPlan,
  subscriberCount: Int
) {
  Card(
    modifier = Modifier.fillMaxWidth().testTag("plan_card_${plan.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(
      width = if (plan.isPopular) 2.dp else 1.dp,
      color = if (plan.isPopular) ElectricLime500 else BorderLight
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = plan.name,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            color = Charcoal900
          )
          if (plan.isPopular) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(ElectricLime500)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "POPULAR",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = Charcoal900
              )
            }
          }
        }

        Text(
          text = "$subscriberCount active members",
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color(0xFF6B7280)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(verticalAlignment = Alignment.Bottom) {
        Text(
          text = "$${String.format("%.2f", plan.price)}",
          fontSize = 24.sp,
          fontWeight = FontWeight.Black,
          color = Charcoal900
        )
        Text(
          text = " / ${plan.durationDays} days",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color(0xFF6B7280),
          modifier = Modifier.padding(bottom = 2.dp)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = plan.description,
        fontSize = 13.sp,
        color = Color(0xFF4B5563)
      )

      if (plan.features.isNotEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF9FAFB))
            .padding(10.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          plan.features.forEach { feat ->
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ElectricLime900,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = feat,
                fontSize = 12.sp,
                color = Charcoal900,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun AddPlanDialog(
  onDismiss: () -> Unit,
  onSave: (name: String, durationDays: Int, price: Double, description: String, features: List<String>, isPopular: Boolean) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var durationDaysStr by remember { mutableStateOf("30") }
  var priceStr by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var featuresStr by remember { mutableStateOf("") }
  var isPopular by remember { mutableStateOf(false) }
  var errorMsg by remember { mutableStateOf<String?>(null) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(18.dp),
      color = Color.White,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Create Membership Plan",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = Charcoal900
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Plan Name *") },
          placeholder = { Text("e.g. 6-Month Crossfit Pro") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("new_plan_name_input"),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedTextField(
            value = durationDaysStr,
            onValueChange = { durationDaysStr = it },
            label = { Text("Duration (Days) *") },
            placeholder = { Text("30") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f).testTag("new_plan_duration_input"),
            shape = RoundedCornerShape(10.dp)
          )

          OutlinedTextField(
            value = priceStr,
            onValueChange = { priceStr = it },
            label = { Text("Price ($) *") },
            placeholder = { Text("79.99") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.weight(1f).testTag("new_plan_price_input"),
            shape = RoundedCornerShape(10.dp)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Description") },
          placeholder = { Text("Short highlight of this tier") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("new_plan_desc_input"),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = featuresStr,
          onValueChange = { featuresStr = it },
          label = { Text("Features (Comma separated)") },
          placeholder = { Text("24/7 Access, Free Towels, Sauna") },
          modifier = Modifier.fillMaxWidth().testTag("new_plan_features_input"),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Highlight as Popular Plan", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
          Switch(
            checked = isPopular,
            onCheckedChange = { isPopular = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Charcoal900, checkedTrackColor = ElectricLime500)
          )
        }

        if (errorMsg != null) {
          Spacer(modifier = Modifier.height(6.dp))
          Text(errorMsg!!, color = AlertRed500, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            if (name.isBlank()) {
              errorMsg = "Plan name is required"
              return@Button
            }
            val days = durationDaysStr.toIntOrNull()
            if (days == null || days <= 0) {
              errorMsg = "Valid duration in days is required"
              return@Button
            }
            val price = priceStr.toDoubleOrNull()
            if (price == null || price < 0) {
              errorMsg = "Valid price is required"
              return@Button
            }
            val featList = featuresStr.split(",").map { it.trim() }.filter { it.isNotBlank() }
            onSave(name.trim(), days, price, description.trim(), featList, isPopular)
          },
          colors = ButtonDefaults.buttonColors(containerColor = Charcoal900, contentColor = Color.White),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_plan_btn")
        ) {
          Text("+ Add Membership Plan", fontWeight = FontWeight.Black, fontSize = 14.sp)
        }
      }
    }
  }
}
