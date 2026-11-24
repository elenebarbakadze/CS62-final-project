import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** 
 * Reads .ics files and converts their events into Event objects
 * @author Elene and Ellie
 */
public class CalendarParser {

    /**
     * Parses a .ics file into events 
     *
     * @param file calendar file name
     * @param ownerName the name of owner
     * @param from first day to include
     * @param to last day to include (the whole day is included)
     * @return the events found in the file
     * @throws IOException if the file cannot be read
     */
    public List<Event> parse(File file, String ownerName, LocalDate from, LocalDate to)
    throws IOException {
        // include the entire final day by ending at midnight on the following day.
        LocalDateTime rangeStart = from.atStartOfDay();
        LocalDateTime rangeEnd = to.plusDays(1).atStartOfDay();
        List<Event> events = new ArrayList<>();
    
        List<String> eventLines = null;

        //read the file
        for (String line : Files.readAllLines(file.toPath())) {
            if (line.startsWith("BEGIN:VEVENT")) {
                // Start of the events are marked by BEGIN:VEVENT
                // create an empty list to start logging data
                eventLines = new ArrayList<>();
            } else if (line.startsWith("END:VEVENT")) {
                // The end is marked by END:VEVENT
                // Once the code reaches the end, it creates an Event object
                Event e = buildEvent(eventLines, ownerName, rangeStart, rangeEnd);
                
                // in case the event is null, it won't do anything
                // any other time it will add the event to the list
                if (e != null) {
                    events.add(e);}   
                //clears eventLines 
                eventLines = null;

            } else if (eventLines != null) {
                //while the event still has information, keep adding it to eventlines
                eventLines.add(line);
            }
        }
        return events;
    }

    /** Builds one Event from the lines between BEGIN:VEVENT and END:VEVENT. */
    private Event buildEvent(List<String> lines, String owner,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        try {
            //parse the start time, marked by DTSTART
            String startText = getValue(lines, "DTSTART");
            //if the startText is empty, return null and stop the method
            if (startText == null){
                return null;}
            //parse the endtime, marked by DTEND
            String endText = getValue(lines, "DTEND");
            //parse the title, marked by SUMMARY
            String title = getValue(lines, "SUMMARY");
            //parse the location, marked by LOCATION
            String location = getValue(lines, "LOCATION");
            
            if (title == null){
                title = "(no title)";
            }
            
            if (location == null){
                location = "";
            }
            //parse if the event is reccuring, marked by RRULE
            //is null if not
            String recurrenceRule = getValue(lines, "RRULE");
            boolean repeating;
            if (recurrenceRule != null) {
                repeating = true;
            } else {
                repeating = false;
            }
            
            //if its only 8 values, it means its an all day event
            boolean allDay = startText.length() == 8;
            //convert start time to a time object
            LocalDateTime start = parseTime(startText);
            //convert end time to a time object
            LocalDateTime end;
            if (endText != null){
                end = parseTime(endText);}
            else if (allDay) {
                end = start.plusDays(1);
            } else {
                end = start;
            }

            // ignore events that do not overlap the requested date range.
            if (!end.isAfter(rangeStart) || !start.isBefore(rangeEnd)) {
                return null;}
            
            return new Event(owner, title, start, end, classify(title), repeating, location);

        } catch (Exception e) {
            // skip entries whose date or time values cannot be parsed.
            return null;
        }
    }

    /** Finds the line starting with the given name and returns its value */
    private String getValue(List<String> lines, String name) {
        for (String line : lines) {
        
            if (line.startsWith(name + ":") || line.startsWith(name + ";")) {
                //return the value starting after ":" without the whitespace
                return line.substring(line.indexOf(':') + 1).trim();
            }
        }
        return null;
    }

    /** Converts .ics time text into a LocalDateTime */
    private LocalDateTime parseTime(String text) {
        // A date without a time (only 8 numbers) represents an all-day event beginning at midnight
        if (text.length() == 8) {
            return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay();
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        if (text.endsWith("Z")) {
            // Z marks UTC
            // convert that time to the computer's local time zone.
            LocalDateTime utc = LocalDateTime.parse(text.substring(0, text.length() - 1), format);
            return utc.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        }
        return LocalDateTime.parse(text, format);
    }

    /** Guesses whether an event is movable or fixed by looking for keywords in its title. */
    public Event.Type classify(String title) {
        //lowercase all titles
        String t = title.toLowerCase();
        // titles with these keywords are movable
        // other events default to hard commitments.
        String[] soft = {"gym", "workout", "lunch", "dinner", "breakfast", "coffee", "study", "hang"};
        for (String word : soft) {
            if (t.contains(word)) return Event.Type.SOFT;
        }
        return Event.Type.HARD;
    }

    /** a test code to make sure everything works properly */
    public static void main(String[] args) throws Exception {
        CalendarParser parser = new CalendarParser();
        // Parse the calendar over the selected date range.
        List<Event> events = parser.parse(
                new File("/Users/macbook/Desktop/data/elene.ics"), "Elene",
                LocalDate.of(2026, 1, 10), LocalDate.of(2026, 3, 10));

        // Print each event, followed by the total number found.
        for (Event e : events) {
            System.out.println(e.getTitle() + " | " + e.getStart() + " - " + e.getEnd()
                    + " | " + e.getType());
        }
        System.out.println(events.size() + " events found");
    }
}
