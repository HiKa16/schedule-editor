package schedule;

public class PlannedEvent extends Event{

	private TimeSlot timeSlot;
	
	
	public PlannedEvent(String title, String desc, TimeSlot ts) {
		super(title, desc);
		this.timeSlot = ts; 
	}
	
	public TimeSlot getTimeSlot() {
		return this.timeSlot;
	}
	

}
