import java.time.LocalDateTime;

/**
 * A block of free time that could work for a hangout.
 * The Scheduler creates time slots, scores each one, 
 * and puts them in a priority queue to find the best one.
 * @author Elene and Ellie
 */
public class Slot implements Comparable<Slot> {

    /** When the free time starts */
    private final LocalDateTime start;
    /** When the free time ends */
    private final LocalDateTime end;
    /** How good this slot is (higher = better) */
    private int score;

    /**
     * Creates a slot with a score of 0
     *
     * @param start when the free time starts
     * @param end when the free time ends
     */
    public Slot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
        this.score = 0;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Checks if a hangout of the required length fits inside this slot
     *
     * @param minutes length of the hangout (minutes)
     * @return true if the hangout works
     */
    public boolean fits(int minutes) {
        // the hangout works if starting at "start" and lasting "minutes"
        // does not go past the end of the slot
        return !start.plusMinutes(minutes).isAfter(end);
    }

    /**
     * Orders slots so the best slot comes first.
     * Since priority queue's extractMin returns the smallest item,
     * a higher score counts as "smaller".
     */
    public int compareTo(Slot other) {
        if (score > other.score) {
            return -1;
        } else if (score < other.score) {
            return 1;
        }
        // same score so the earlier slot comes first
        return start.compareTo(other.start);
    }

    public String toString() {
        return start + " to " + end + " (score " + score + ")";
    }
}