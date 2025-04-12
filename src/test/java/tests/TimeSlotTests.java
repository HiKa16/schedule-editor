package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import schedule.TimeSlot;

public class TimeSlotTests {


	@Test
	public void test1() {
		TimeSlot slot1 = new TimeSlot(TestVariables.today, TestVariables.eightAM, TestVariables.tenAM);
		TimeSlot slot2 = new TimeSlot(TestVariables.today, TestVariables.onePM, TestVariables.twoPM);
		assertFalse(slot1.overlapsWith(slot2));
		assertFalse(slot2.overlapsWith(slot1));
	}

	@Test
	public void test2() {
		TimeSlot slot1 = new TimeSlot(TestVariables.today, TestVariables.onePM, TestVariables.threePM);
		TimeSlot slot2 = new TimeSlot(TestVariables.today, TestVariables.threePM, TestVariables.fivePM);
		assertFalse(slot1.overlapsWith(slot2));
		assertFalse(slot2.overlapsWith(slot1));
	}
	
	@Test
	public void test3() {
		TimeSlot slot1 = new TimeSlot(TestVariables.today, TestVariables.eightAM, TestVariables.tenAM);
		TimeSlot slot2 = new TimeSlot(TestVariables.today, TestVariables.nineAM, TestVariables.elevenAM);
		assertTrue(slot1.overlapsWith(slot2));
		assertTrue(slot2.overlapsWith(slot1));
	}
	
	@Test
	public void test4() {
		TimeSlot slot1 = new TimeSlot(TestVariables.today, TestVariables.noon, TestVariables.sixPM);
		TimeSlot slot2 = new TimeSlot(TestVariables.today, TestVariables.twoPM, TestVariables.fourPM);
		assertTrue(slot1.overlapsWith(slot2));
		assertTrue(slot2.overlapsWith(slot1));
	}
}

