package schedule;

import java.util.Objects;

public class Event {
	private static int counter=0;
	private final int id;
	private String title;
	private String description;
	
	public Event(String title, String desc) {
		this.id = counter++;
		this.title = title; 
		this.description = desc;
	}

	public String getTitle() {
		return this.title;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setTitle(String name) {
		this.title = name;
	}
	
	public void setDescription(String descr) {
		this.description = descr;
	}
	
	public int getID() {
		return this.id;
	}
	
    @Override
    public boolean equals(Object o) {
        if(o instanceof Event) {
            Event other = (Event) o;
            return this.id == other.id;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
	@Override
	public String toString() {
		return String.format("%s : %s", this.title, this.description);
	}
}
