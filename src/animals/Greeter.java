package animals;

import java.time.LocalTime;

public class Greeter {
    public static String getGreeting(LocalTime time) {
        String greeting;
        if (isBetween(time, "05:00", "12:00")) {
            greeting = "greeting.morning";
        } else if (isBetween(time, "12:00", "18:00")) {
            greeting = "greeting.afternoon";
        } else {
            greeting = "greeting.evening";
        }
        return greeting;
    }

    private static boolean isBetween(LocalTime time, String start, String end) {
        LocalTime endTime = LocalTime.parse(end);
        LocalTime startTime = LocalTime.parse(start);
        return time.isBefore(endTime) && (time.isAfter(startTime) || time.equals(startTime));
    }

}
