package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import schedule.TimeSlot;

public class TimeSlotTests {
	LocalDate date = LocalDate.of(2025, 04, 02);
	LocalTime eightAM = LocalTime.of(8, 0);
	LocalTime nineAM = LocalTime.of(9, 0);
	LocalTime tenAM = LocalTime.of(10, 0);
	LocalTime elevenAM = LocalTime.of(11, 0);
	LocalTime noon = LocalTime.of(12, 0);
	LocalTime onePM = LocalTime.of(13, 0);
	LocalTime twoPM = LocalTime.of(14, 0);
	LocalTime threePM = LocalTime.of(15, 0);
	LocalTime fourPM = LocalTime.of(16, 0);
	LocalTime fivePM = LocalTime.of(17, 0);
	LocalTime sixPM = LocalTime.of(18, 0);

	@Test
	public void test1() {
		TimeSlot ts1 = new TimeSlot(date, eightAM, tenAM);
		TimeSlot ts2 = new TimeSlot(date, onePM, twoPM);
		assertFalse(ts1.overlapsWith(ts2));
		assertFalse(ts2.overlapsWith(ts1));
	}

	@Test
	public void test2() {
		TimeSlot ts1 = new TimeSlot(date, onePM, threePM);
		TimeSlot ts2 = new TimeSlot(date, threePM, fivePM);
		assertFalse(ts1.overlapsWith(ts2));
		assertFalse(ts2.overlapsWith(ts1));
	}
	
	@Test
	public void test3() {
		TimeSlot ts1 = new TimeSlot(date, eightAM, tenAM);
		TimeSlot ts2 = new TimeSlot(date, nineAM, elevenAM);
		assertTrue(ts1.overlapsWith(ts2));
		assertTrue(ts2.overlapsWith(ts1));
	}
	
	@Test
	public void test4() {
		TimeSlot ts1 = new TimeSlot(date, noon, sixPM);
		TimeSlot ts2 = new TimeSlot(date, twoPM, fourPM);
		assertTrue(ts1.overlapsWith(ts2));
		assertTrue(ts2.overlapsWith(ts1));
	}
}

