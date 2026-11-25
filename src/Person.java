import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * One person in the scheduler with all of their events.
 * @author Elene and Ellie
 */
public class Person {

    /** Name of the person */
    private final String name;
    /** Every event from this person's calendar */
    private List<Event> events;

    /**
     * Creates a person (no events yet)
     *
     * @param name their name
     */
    public Person(String name) {
        this.name = name;
        this.events = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Event> getEvents() {
        return events;
    }

    /**
     * Adds an event to this person's calendar
     *
     * @param e the event to add
     */
    public void addEvent(Event e) {
        events.add(e);
    }

    /**
     * Adds every event in a list
     *
     * @param newEvents the events to add
     */
    public void addEvents(List<Event> newEvents) {
        for (Event e : newEvents) {
            events.add(e);
        }
    }

    /**
     * Returns only the events that block this person's time
     *
     * @param softIsBusy true if soft commitments should count as busy
     * @return the events that count as busy
     */
    public List<Event> getBusyEvents(boolean softIsBusy) {
        List<Event> busy = new ArrayList<>();
        for (Event e : events) {
            // TODO: 
            // hard events always block time
            // soft events only block time if softIsBusy is true
            // add e to busy when it should count as busy
        }
        return busy;
    }

    /**
     * Checks if this person is free for the whole window
     *
     * @param start start of the window
     * @param end end of the window
     * @param softIsBusy true if soft commitments should count as busy
     * @return true if no busy event overlaps the window
     */
    public boolean isFree(LocalDateTime start, LocalDateTime end, boolean softIsBusy) {
        // TODO: 
        // loop over the busy events
        // if any of them overlaps then return false
        return true;
    }

    /**
     * Finds the soft events that overlap a window, so the program can 
     * propose skipping if needed.
     *
     * @param start start of the window
     * @param end end of the window
     * @return the soft events that overlap the window
     */
    public List<Event> getSoftConflicts(LocalDateTime start, LocalDateTime end) {
        // TODO: 
        // same overlap check as isFree but only for soft events
        // collect them in a list instead of returning false
        return new ArrayList<>();
    }

    public String toString() {
        return name + " (" + events.size() + " events)";
    }
}