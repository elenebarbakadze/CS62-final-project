import java.time.LocalDateTime;

/** One calendar entry belonging to a person. 
 * @author Elene and Ellie
*/
public class Event implements Comparable<Event> {
    public enum Type { HARD, SOFT }

    /** Name of the person*/
    private final String owner;
    /**Title of the event */
    private final String title;
    /**When the event starts */
    private final LocalDateTime start;
    /** When the event ends */
    private final LocalDateTime end;
    /** Hard or soft commitment */
    private final Type type;
    /** If the event repeats */
    private boolean repeating;
    /** Where the event is */
    private final String location;

    public Event(String owner, String title, LocalDateTime start, LocalDateTime end,
                 Type type,  boolean repeating, String location) {
        this.owner = owner;
        this.title = title;
        this.start = start;
        this.end = end;
        this.type = type;
        this.repeating = repeating;
        this.location = location;
    }

    public String getOwner(){ 
        return owner; 
    }

    public String getTitle(){ 
        return title; 
    }

    public LocalDateTime getStart(){ 
        return start; 
    }

    public LocalDateTime getEnd(){ 
        return end; 
    }

    public Type getType(){ 
        return type; 
    }

    public boolean isRepeating(){ 
        return repeating; 
    }

    public String getLocation(){ 
        return location; 
    }

    //comparator ordering events by the start time for mergesort
    public int compareTo(Event other) {
        return start.compareTo(other.start);
    }
}