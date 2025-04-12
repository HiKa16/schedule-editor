package tests;
import static org.junit.Assert.*;


import org.junit.Test;

import schedule.Event;
import schedule.PlannedEvent;
import schedule.TimeSlot;

public class EventTests {
	
	@Test
	public void test1() {
		Event e1 = new Event("Event", "");
		Event e2 = new Event("Event", "");
		assertFalse(e1 == e2);
	}
	
	@Test
	public void test2() {
		TimeSlot slot = new TimeSlot(TestVariables.today, TestVariables.eightAM, TestVariables.tenAM);
		PlannedEvent e1 = new PlannedEvent("Event", "", slot);
		PlannedEvent e2 = new PlannedEvent("Event", "", slot);
		assertFalse(e1 == e2);
	}

}
