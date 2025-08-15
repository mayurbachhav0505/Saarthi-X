package codecharioteers.Dashboard;

import java.time.LocalDate;
import java.util.*;

public class Eventmanager {
    private final Map<LocalDate, List<String>> eventMap = new HashMap<>();

    public void addEvent(LocalDate date, String event) {
        eventMap.computeIfAbsent(date, k -> new ArrayList<>()).add(event);
    }

    public List<String> getEvents(LocalDate date) {
        return eventMap.getOrDefault(date, new ArrayList<>());
    }
    
}
