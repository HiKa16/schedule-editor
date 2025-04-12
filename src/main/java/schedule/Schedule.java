package schedule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Schedule {
	private HashMap<LocalDate, List<PlannedEvent>> plannedEvents;
	
	public Schedule() {
		this.plannedEvents = new HashMap<LocalDate, List<PlannedEvent>>();
	}
	
	public HashMap<LocalDate, List<PlannedEvent>> getPlannedEvents() {
		return this.plannedEvents; 
	}
	
	public void setEventToDate(PlannedEvent event, LocalDate date) {
		if (this.plannedEvents.get(date) == null) {
			this.plannedEvents.put(date, new ArrayList<PlannedEvent>());
		}
		this.plannedEvents.get(date).add(event);
	}
	
	public List<PlannedEvent> getEventsByDate(LocalDate date) {
		return this.plannedEvents.get(date);
	}
	 
	public void addPlannedEvent(String title, String desc, TimeSlot ts) throws UnavailableTSException {
		if (!TimeSlotIsAvailable(ts)) { 
			throw new UnavailableTSException();
		}
		LocalDate date = ts.getDate();
		this.setEventToDate(new PlannedEvent(title, desc, ts), date);
	}
	
	public void addPlannedEvent(PlannedEvent event) throws UnavailableTSException {
		TimeSlot ts = event.getTimeSlot();
		if (!TimeSlotIsAvailable(ts)) { 
			throw new UnavailableTSException();
		}
		LocalDate date = ts.getDate();
		this.setEventToDate(event, date);}
	


	public void removePlannedEvent(PlannedEvent e) {
		TimeSlot ts = e.getTimeSlot();
		assert this.plannedEvents.get(ts.getDate()) != null : "Can't remove an event that's not in the schedule";
		assert this.plannedEvents.get(ts.getDate()).contains(e) : "Can't remove an event that's not in the schedule";
		this.plannedEvents.get(ts.getDate()).remove(e);
	}
	

	public boolean TimeSlotIsAvailable(TimeSlot ts) {
		LocalDate date = ts.getDate();
		if (this.getEventsByDate(date) == null) {
			return true;
		}
		for (PlannedEvent event : this.getEventsByDate(date)) {
			if (event.getTimeSlot().overlapsWith(ts)) { 
				return false; 
			}
		}
		return true;
	}
	
	public List<PlannedEvent> getConflictingEvents (TimeSlot slot) {
		LocalDate date = slot.getDate();
		List<PlannedEvent> conflicts = new ArrayList<PlannedEvent>(); 
		if (this.getEventsByDate(date) != null) {
			for (PlannedEvent e : this.getEventsByDate(date)) {
				if (e.getTimeSlot().overlapsWith(slot)) { 
					conflicts.add(e);
				}
			}
		}
		return conflicts;
	}
	
	public void updateEvent(PlannedEvent event, String newTitle, String newDesc, TimeSlot newSlot) throws UnavailableTSException {
		TimeSlot slot = event.getTimeSlot();
		assert this.plannedEvents.get(slot.getDate()).contains(event) : "Can't update an event that's not in the schedule";		
		if (!TimeSlotIsAvailable(newSlot)) { 
			List<PlannedEvent> conflicts = getConflictingEvents(newSlot);
			if (!(conflicts.size() == 1 && conflicts.get(0) == event)) {
				throw new UnavailableTSException();
			}
		}
		event.setTitle(newTitle);
		event.setDescription(newDesc);
		slot.setStartTime(newSlot.getStartTime());
		slot.setEndTime(newSlot.getEndTime());
		if (slot.getDate() != newSlot.getDate()) {
			this.setEventToDate(event, newSlot.getDate());
			slot.setDate(newSlot.getDate());
		} 
	}
	
	public PlannedEvent getEventStartingAt(LocalDate date, LocalTime time) {
		if (this.plannedEvents.get(date) == null) { return null; }
		for (PlannedEvent event : this.plannedEvents.get(date)) {
			TimeSlot slot = event.getTimeSlot();
			if (slot.getStartTime().equals(time)) { 
				//slot.start <= time.start < time.end <= slot.end
				return event;
			}
		}
		return null;
	}
}
	
