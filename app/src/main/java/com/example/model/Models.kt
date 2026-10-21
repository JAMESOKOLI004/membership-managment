package com.example.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

enum class MemberStatus(val label: String) {
  ACTIVE("Active"),
  EXPIRING_SOON("Expiring Soon"),
  EXPIRED("Expired")
}

data class GymMember(
  val id: String,
  val name: String,
  val phone: String,
  val email: String,
  val planId: String,
  val planName: String,
  val startDate: String, // YYYY-MM-DD
  val expiryDate: String, // YYYY-MM-DD
  val avatarColorHex: Long = 0xFF111827,
  val assignedTrainer: String? = null,
  val emergencyContact: String? = null,
  val checkInCount: Int = 0,
  val notes: String? = null,
  val reminderSentAt: String? = null
) {
  fun getCalculatedStatus(referenceDateStr: String = "2026-10-21"): MemberStatus {
    val days = getDaysUntilExpiry(referenceDateStr)
    return when {
      days < 0 -> MemberStatus.EXPIRED
      days <= 7 -> MemberStatus.EXPIRING_SOON
      else -> MemberStatus.ACTIVE
    }
  }

  fun getDaysUntilExpiry(referenceDateStr: String = "2026-10-21"): Long {
    return try {
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
      val refDate = sdf.parse(referenceDateStr) ?: Date()
      val expDate = sdf.parse(expiryDate) ?: Date()
      val diffMs = expDate.time - refDate.time
      TimeUnit.DAYS.convert(diffMs, TimeUnit.MILLISECONDS)
    } catch (e: Exception) {
      0L
    }
  }
}

data class MembershipPlan(
  val id: String,
  val name: String,
  val durationDays: Int,
  val price: Double,
  val description: String,
  val features: List<String>,
  val isPopular: Boolean = false,
  val badgeColorHex: Long = 0xFF84CC16
)

data class CheckInRecord(
  val id: String,
  val memberId: String,
  val memberName: String,
  val planName: String,
  val timestamp: Long,
  val timeFormatted: String,
  val dateFormatted: String,
  val isSuccess: Boolean,
  val message: String,
  val staffName: String = "Front Desk"
)

data class PaymentRecord(
  val id: String,
  val receiptNumber: String,
  val memberId: String,
  val memberName: String,
  val planId: String,
  val planName: String,
  val amount: Double,
  val date: String,
  val paymentMethod: String, // "Card", "Cash", "Bank Transfer", "Apple Pay"
  val autoExtendedDays: Int = 0,
  val newExpiryDate: String? = null,
  val notes: String? = null
)

data class StaffUser(
  val id: String,
  val name: String,
  val role: String,
  val pin: String,
  val avatarInitials: String
)
