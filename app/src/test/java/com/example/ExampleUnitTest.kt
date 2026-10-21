package com.example

import com.example.data.FitAdminRepository
import com.example.model.GymMember
import com.example.model.MemberStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testMemberStatusCalculation() {
    val activeMember = GymMember(
      id = "T-1",
      name = "Active Guy",
      phone = "555-1234",
      email = "a@test.com",
      planId = "PLAN-1",
      planName = "Monthly",
      startDate = "2026-10-01",
      expiryDate = "2026-11-20"
    )
    assertEquals(MemberStatus.ACTIVE, activeMember.getCalculatedStatus("2026-10-21"))

    val expiringMember = GymMember(
      id = "T-2",
      name = "Expiring Soon",
      phone = "555-1235",
      email = "b@test.com",
      planId = "PLAN-1",
      planName = "Monthly",
      startDate = "2026-09-24",
      expiryDate = "2026-10-24"
    )
    assertEquals(MemberStatus.EXPIRING_SOON, expiringMember.getCalculatedStatus("2026-10-21"))

    val expiredMember = GymMember(
      id = "T-3",
      name = "Expired Guy",
      phone = "555-1236",
      email = "c@test.com",
      planId = "PLAN-1",
      planName = "Monthly",
      startDate = "2026-09-01",
      expiryDate = "2026-10-10"
    )
    assertEquals(MemberStatus.EXPIRED, expiredMember.getCalculatedStatus("2026-10-21"))
  }

  @Test
  fun testCheckInActiveAndBlocked() {
    val repo = FitAdminRepository.getInstance()

    // Active member M-1001
    val (activeSuccess, activeRecord) = repo.performCheckIn("M-1001")
    assertTrue(activeSuccess)
    assertTrue(activeRecord.isSuccess)

    // Expired member M-1005 (expired Oct 12)
    val (expiredSuccess, expiredRecord) = repo.performCheckIn("M-1005")
    assertFalse(expiredSuccess)
    assertFalse(expiredRecord.isSuccess)
    assertTrue(expiredRecord.message.contains("BLOCKED"))
  }

  @Test
  fun testRecordPaymentAutoExtendsExpiry() {
    val repo = FitAdminRepository.getInstance()
    val memberBefore = repo.getMember("M-1005")!!
    val originalExpiry = memberBefore.expiryDate

    val pay = repo.recordPayment(
      memberId = "M-1005",
      planId = "PLAN-1",
      amount = 69.0,
      paymentMethod = "Card",
      autoExtend = true,
      notes = "Test renewal"
    )

    assertNotNull(pay.newExpiryDate)
    val memberAfter = repo.getMember("M-1005")!!
    assertEquals(pay.newExpiryDate, memberAfter.expiryDate)
  }
}
