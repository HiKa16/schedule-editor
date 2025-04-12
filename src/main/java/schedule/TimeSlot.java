package schedule;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot {
	private LocalDate date;
	private LocalTime start;
	private LocalTime end;
	
	public TimeSlot(LocalDate date, LocalTime start, LocalTime end) {
		if (!start.isBefore(end)) {
			throw new IllegalArgumentException("Starting time must be before ending time.");
		}
		this.date = date;
		this.start = start;
		this.end = end;
	}
	
	public LocalTime getStartTime() {
		return this.start;
	}
	
	public LocalTime getEndTime() {
		return this.end;
	}
	
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public void setStartTime(LocalTime time) {
		this.start = time;
	}
	
	public void setEndTime(LocalTime time) {
		this.end = time;
	}
	 
	public boolean overlapsWith(TimeSlot other) {
		if (!this.date.isEqual(other.getDate())) return false;
		if ( this.start.isBefore(other.getStartTime()) ) {
			return this.getEndTime().isAfter(other.getStartTime());
		} 
		else if (this.start.equals(other.getStartTime())) {
			return true;
		}
		else {
			return other.getEndTime().isAfter(this.getStartTime());
		}	
	}
	
	@Override
	public String toString() {
		return String.format("[%s] %s - %s ", this.date, this.start, this.end);
	}

}
