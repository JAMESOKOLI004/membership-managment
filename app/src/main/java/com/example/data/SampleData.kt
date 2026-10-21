package com.example.data

import com.example.model.CheckInRecord
import com.example.model.GymMember
import com.example.model.MembershipPlan
import com.example.model.PaymentRecord
import com.example.model.StaffUser

object SampleData {
  val defaultStaff = listOf(
    StaffUser("ST-1", "Alex Mercer", "Front Desk Lead", "1234", "AM"),
    StaffUser("ST-2", "Elena Rostova", "Floor Manager", "2026", "ER"),
    StaffUser("ST-3", "Marcus Cole", "Head Coach & Admin", "9999", "MC")
  )

  val defaultPlans = listOf(
    MembershipPlan(
      id = "PLAN-1",
      name = "Monthly Unlimited",
      durationDays = 30,
      price = 69.00,
      description = "Full gym floor, lockers & sauna access for 30 days.",
      features = listOf("Full gym floor access", "Lockers & Sauna access", "1 Free guest pass/mo", "FitAdmin mobile barcode"),
      isPopular = false,
      badgeColorHex = 0xFF84CC16
    ),
    MembershipPlan(
      id = "PLAN-2",
      name = "Quarterly Pro",
      durationDays = 90,
      price = 189.00,
      description = "Save 15% with 3 months upfront. Includes trainer consult.",
      features = listOf("All Monthly features", "1 Complimentary PT assessment", "Free daily towel service", "Freeze membership up to 14 days"),
      isPopular = true,
      badgeColorHex = 0xFF84CC16
    ),
    MembershipPlan(
      id = "PLAN-3",
      name = "Annual VIP All-Access",
      durationDays = 365,
      price = 649.00,
      description = "VIP 365-day access to all facilities & group classes.",
      features = listOf("24/7 Unlimited access", "Free guest pass on every visit", "Unlimited HIIT & Boxing classes", "Dedicated locker & gear bag"),
      isPopular = false,
      badgeColorHex = 0xFFF59E0B
    ),
    MembershipPlan(
      id = "PLAN-4",
      name = "Student / Off-Peak",
      durationDays = 30,
      price = 45.00,
      description = "Valid Mon-Fri 6AM-4PM & weekends anytime.",
      features = listOf("Off-peak floor access", "Locker room & hot showers", "Valid with Student/Staff ID"),
      isPopular = false,
      badgeColorHex = 0xFF3B82F6
    ),
    MembershipPlan(
      id = "PLAN-5",
      name = "Day Pass",
      durationDays = 1,
      price = 18.00,
      description = "Single-day visitor pass with full access.",
      features = listOf("Full 1-day floor access", "Lockers & steam room"),
      isPopular = false,
      badgeColorHex = 0xFF6B7280
    )
  )

  val defaultMembers = listOf(
    GymMember(
      id = "M-1001",
      name = "Marcus Vance",
      phone = "(555) 234-5678",
      email = "marcus.v@example.com",
      planId = "PLAN-2",
      planName = "Quarterly Pro",
      startDate = "2026-08-01",
      expiryDate = "2026-11-01",
      avatarColorHex = 0xFF1E293B,
      assignedTrainer = "Elena Rostova",
      emergencyContact = "(555) 998-1122 (Wife - Clara)",
      checkInCount = 28,
      notes = "Focusing on Olympic lifting. Locker #42 reserved."
    ),
    GymMember(
      id = "M-1002",
      name = "Samantha Reed",
      phone = "(555) 876-5432",
      email = "s.reed@example.com",
      planId = "PLAN-1",
      planName = "Monthly Unlimited",
      startDate = "2026-09-24",
      expiryDate = "2026-10-24",
      avatarColorHex = 0xFF065F46,
      assignedTrainer = "Marcus Cole",
      emergencyContact = "(555) 443-2211 (Brother - Dan)",
      checkInCount = 14,
      notes = "Pre-ordered protein shake subscription."
    ),
    GymMember(
      id = "M-1003",
      name = "David Kalu",
      phone = "(555) 345-6789",
      email = "dkalu@example.com",
      planId = "PLAN-3",
      planName = "Annual VIP All-Access",
      startDate = "2026-01-15",
      expiryDate = "2027-01-15",
      avatarColorHex = 0xFF831843,
      assignedTrainer = "Marcus Cole",
      emergencyContact = "(555) 776-8899 (Father - James)",
      checkInCount = 89,
      notes = "VIP keytag #007. Uses private locker room."
    ),
    GymMember(
      id = "M-1004",
      name = "Jessica Chen",
      phone = "(555) 901-2345",
      email = "jchen@example.com",
      planId = "PLAN-1",
      planName = "Monthly Unlimited",
      startDate = "2026-09-22",
      expiryDate = "2026-10-22",
      avatarColorHex = 0xFF4338CA,
      assignedTrainer = null,
      emergencyContact = "(555) 332-1144 (Partner - Leo)",
      checkInCount = 19,
      notes = "Wants to switch to Quarterly Pro next renewal."
    ),
    GymMember(
      id = "M-1005",
      name = "Tyler Brooks",
      phone = "(555) 678-9012",
      email = "tbrooks@example.com",
      planId = "PLAN-1",
      planName = "Monthly Unlimited",
      startDate = "2026-09-12",
      expiryDate = "2026-10-12",
      avatarColorHex = 0xFF7C2D12,
      assignedTrainer = null,
      emergencyContact = "(555) 123-9988 (Mother - Laura)",
      checkInCount = 8,
      notes = "Card expired on file. Needs manual renewal."
    ),
    GymMember(
      id = "M-1006",
      name = "Aaliyah Patel",
      phone = "(555) 456-7890",
      email = "aaliyah.p@example.com",
      planId = "PLAN-2",
      planName = "Quarterly Pro",
      startDate = "2026-07-26",
      expiryDate = "2026-10-26",
      avatarColorHex = 0xFF14532D,
      assignedTrainer = "Elena Rostova",
      emergencyContact = "(555) 667-8890 (Sister - Priya)",
      checkInCount = 34,
      notes = "Attends 7am Spin & HIIT classes regularly."
    ),
    GymMember(
      id = "M-1007",
      name = "Liam O'Connor",
      phone = "(555) 567-8901",
      email = "liam.oc@example.com",
      planId = "PLAN-4",
      planName = "Student / Off-Peak",
      startDate = "2026-09-15",
      expiryDate = "2026-10-15",
      avatarColorHex = 0xFF581C87,
      assignedTrainer = null,
      emergencyContact = "(555) 887-2211 (Roommate - Ben)",
      checkInCount = 11,
      notes = "State University student ID verified."
    ),
    GymMember(
      id = "M-1008",
      name = "Zoe Martinez",
      phone = "(555) 789-0123",
      email = "zoe.m@example.com",
      planId = "PLAN-3",
      planName = "Annual VIP All-Access",
      startDate = "2026-04-10",
      expiryDate = "2027-04-10",
      avatarColorHex = 0xFF0E7490,
      assignedTrainer = "David Chen",
      emergencyContact = "(555) 998-3344 (Spouse - Carlos)",
      checkInCount = 67,
      notes = "Powerlifting competitor. Requests extra chalk bins."
    ),
    GymMember(
      id = "M-1009",
      name = "Jordan Hayes",
      phone = "(555) 890-1234",
      email = "jordan.h@example.com",
      planId = "PLAN-1",
      planName = "Monthly Unlimited",
      startDate = "2026-10-01",
      expiryDate = "2026-10-31",
      avatarColorHex = 0xFF0F172A,
      assignedTrainer = "Marcus Cole",
      emergencyContact = "(555) 223-4455 (Friend - Sam)",
      checkInCount = 12,
      notes = "Recovering from ankle sprain. Low impact cardio."
    ),
    GymMember(
      id = "M-1010",
      name = "Brandon Miller",
      phone = "(555) 901-3456",
      email = "bmiller@example.com",
      planId = "PLAN-1",
      planName = "Monthly Unlimited",
      startDate = "2026-09-18",
      expiryDate = "2026-10-18",
      avatarColorHex = 0xFF881337,
      assignedTrainer = null,
      emergencyContact = "(555) 445-6677 (Sister - Chloe)",
      checkInCount = 15,
      notes = "Expired 3 days ago. Usually renews at desk."
    )
  )

  val defaultCheckIns = listOf(
    CheckInRecord("CK-501", "M-1001", "Marcus Vance", "Quarterly Pro", 1792580000000L, "11:15 AM", "2026-10-21", true, "Access Granted - Turnstile #1", "Alex Mercer"),
    CheckInRecord("CK-502", "M-1008", "Zoe Martinez", "Annual VIP All-Access", 1792576400000L, "10:20 AM", "2026-10-21", true, "Access Granted - Turnstile #2 (VIP)", "Alex Mercer"),
    CheckInRecord("CK-503", "M-1009", "Jordan Hayes", "Monthly Unlimited", 1792572800000L, "09:12 AM", "2026-10-21", true, "Access Granted - Turnstile #1", "Alex Mercer"),
    CheckInRecord("CK-504", "M-1005", "Tyler Brooks", "Monthly Unlimited", 1792569200000L, "08:15 AM", "2026-10-21", false, "BLOCKED: Membership Expired Oct 12", "Alex Mercer"),
    CheckInRecord("CK-505", "M-1003", "David Kalu", "Annual VIP All-Access", 1792565600000L, "07:05 AM", "2026-10-21", true, "Access Granted - Turnstile #2 (VIP)", "Alex Mercer"),
    CheckInRecord("CK-506", "M-1006", "Aaliyah Patel", "Quarterly Pro", 1792562000000L, "06:30 AM", "2026-10-21", true, "Access Granted - Spin Studio Gate", "Alex Mercer")
  )

  val defaultPayments = listOf(
    PaymentRecord("PAY-901", "REC-8842", "M-1001", "Marcus Vance", "PLAN-2", "Quarterly Pro", 189.00, "2026-08-01", "Card (Visa ...4421)", 90, "2026-11-01", "Front desk POS terminal"),
    PaymentRecord("PAY-902", "REC-8843", "M-1003", "David Kalu", "PLAN-3", "Annual VIP All-Access", 649.00, "2026-01-15", "Bank Transfer", 365, "2027-01-15", "Corporate gym benefit"),
    PaymentRecord("PAY-903", "REC-8844", "M-1008", "Zoe Martinez", "PLAN-3", "Annual VIP All-Access", 649.00, "2026-04-10", "Card (Amex ...1002)", 365, "2027-04-10", "Annual prepayment"),
    PaymentRecord("PAY-904", "REC-8845", "M-1009", "Jordan Hayes", "PLAN-1", "Monthly Unlimited", 69.00, "2026-10-01", "Apple Pay", 30, "2026-10-31", "Contactless desk terminal"),
    PaymentRecord("PAY-905", "REC-8846", "M-1002", "Samantha Reed", "PLAN-1", "Monthly Unlimited", 69.00, "2026-09-24", "Cash", 30, "2026-10-24", "Cash register #1"),
    PaymentRecord("PAY-906", "REC-8847", "M-1004", "Jessica Chen", "PLAN-1", "Monthly Unlimited", 69.00, "2026-09-22", "Card (Mastercard ...8831)", 30, "2026-10-22", "Online portal"),
    PaymentRecord("PAY-907", "REC-8848", "M-1006", "Aaliyah Patel", "PLAN-2", "Quarterly Pro", 189.00, "2026-07-26", "Card (Visa ...9923)", 90, "2026-10-26", "Front desk renewal"),
    PaymentRecord("PAY-908", "REC-8849", "M-1007", "Liam O'Connor", "PLAN-4", "Student / Off-Peak", 45.00, "2026-09-15", "Card (Debit ...1209)", 30, "2026-10-15", "Student discount applied")
  )
}
