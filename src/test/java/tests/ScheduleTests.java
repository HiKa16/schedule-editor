package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import schedule.*;

public class ScheduleTests {

	@Test
	public void test1() {
		Schedule schedule = new Schedule();
		TimeSlot slot = new TimeSlot(TestVariables.today, TestVariables.eightAM, TestVariables.tenAM);
		try {
			schedule.addPlannedEvent("Event n°1", "", slot);
		} catch (UnavailableSlotException e) {
			fail();
		}
		
		try {
			schedule.addPlannedEvent("Event n°2", "", slot);
			fail();
		} catch (UnavailableSlotException e) {}	
	}
	
	@Test
	public void test2() {
		Schedule schedule = new Schedule();
		TimeSlot slot = new TimeSlot(TestVariables.today, TestVariables.eightAM, TestVariables.tenAM);
		PlannedEvent event = new PlannedEvent("Event", "", slot);
		try {
			schedule.addPlannedEvent(event);
		} catch (UnavailableSlotException e) {
			fail();
		}
		assertTrue(schedule.getEventsByDate(event.getTimeSlot().getDate()).contains(event));
		schedule.removePlannedEvent(event);
		assertFalse(schedule.getEventsByDate(event.getTimeSlot().getDate()).contains(event));
	}
	

}
