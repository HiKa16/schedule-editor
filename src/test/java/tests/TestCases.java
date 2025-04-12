package tests;

import static org.junit.Assert.*;

import java.time.*;

import org.junit.Test;

import schedule.*;


public class TestCases {

	@Test
	public void test() {
		LocalTime sixPM = LocalTime.of(18, 0);
		LocalTime sevenPM = LocalTime.of(19, 0);
		LocalTime eightPM = LocalTime.of(20, 0);
		LocalTime ninePM = LocalTime.of(21, 0);
		LocalDate april3 = LocalDate.of(2025, Month.APRIL, 3);
		TimeSlot ts1 = new TimeSlot(april3, sixPM, eightPM);
		TimeSlot ts2 = new TimeSlot(april3, eightPM, ninePM);
		assertFalse(ts1.overlapsWith(ts2));
		
		TimeSlot ts3 = new TimeSlot(april3, sevenPM, ninePM);
		assertTrue(ts1.overlapsWith(ts3));
		assertTrue(ts3.overlapsWith(ts1));
	
	}

}
