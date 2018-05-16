package test.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

public class Helper2 {



    public long calc(LocalDateTime fd, LocalDateTime td, boolean[] dayOffs, List<LocalDateTime> holidays,
                     int workDayBegin, int workDayEnd, boolean workDay, boolean nextDay,
                     int lunchBegin, int lunchEnd, boolean lunch) {

        LocalDateTime a = fd.truncatedTo(DAYS);

        List<LocalDateTime[]> periods = new ArrayList<>();
        periods.add(new LocalDateTime[] { fd, td });

        while (a.isBefore(td)) {
            LocalDateTime x1, x2;

            int index = a.getDayOfWeek().ordinal();
            int prevIndex = prevIndex(index);

            boolean isDayOff     = dayOffs[index]     || holidays.contains(a) ;
            boolean prevIsDayOff = dayOffs[prevIndex] || holidays.contains(a.minusDays(1));

            if (isDayOff) {
                if (nextDay) {
                    if (prevIsDayOff) {
                        periods = intersect(periods, a, a.plusDays(1));
                    } else {
                        periods = intersect(periods, a.plusMinutes(workDayEnd), a.plusDays(1));
                    }
                } else {
                    periods = intersect(periods, a, a.plusDays(1));
                }
            } else if (workDay) {
                if (nextDay) {
                    if (lunch)
                        periods = intersect(periods, a.plusMinutes(lunchBegin), a.plusMinutes(lunchEnd));
                    if (prevIsDayOff) {
                        periods = intersect(periods, a, a.plusMinutes(workDayBegin));
                    } else {
                        periods = intersect(periods, a.plusMinutes(workDayEnd), a.plusMinutes(workDayBegin));
                    }
                } else {
                    x1 = max(fd, a.plusMinutes(workDayBegin));
                    x2 = min(td, a.plusMinutes(workDayEnd));
                    if (lunch) {
                        periods = intersect(periods, a.plusMinutes(lunchBegin), a.plusMinutes(lunchEnd));
                    }
                    periods = intersect(periods, a, x1);
                    periods = intersect(periods, x2, a.plusDays(1));
                }
            }

            a = a.plusDays(1);
        }

        System.out.println("###");
        long duration = 0;
        for (LocalDateTime[] period : periods) {
            System.out.println(String.format("%s %s", period[0], period[1]));
            duration += period[0].until(period[1], MINUTES);
        }

        return duration;
    }

    private List<LocalDateTime[]> intersect(List<LocalDateTime[]> periods, LocalDateTime a, LocalDateTime b) {
        if (a.isAfter(b) || a.equals(b))
            return periods;

        List<LocalDateTime[]> result = new ArrayList<>();

        for (LocalDateTime[] period : periods) {
            LocalDateTime p1 = period[0];
            LocalDateTime p2 = period[1];

            if (b.isBefore(p1) || b.equals(p1) || a.isAfter(p2) || a.equals(p2)) {
                result.add(period);
            } else if (a.isBefore(p1) || a.equals(p1)) {
                if (b.isAfter(p1) && b.isBefore(p2)) {
                    //System.out.println(String.format("1. %s %s", b, p2));
                    result.add(new LocalDateTime[] { b, p2 });
                }
            } else if (a.isBefore(p2)) {

                //System.out.println(String.format("2. %s %s", p1, a));
                result.add(new LocalDateTime[] { p1, a });

                if (b.isBefore(p2)) {
                    //System.out.println(String.format("3. %s %s", b, p2));
                    result.add(new LocalDateTime[]{b, p2});
                }
            } else {
                result.add(period);
            }
        }

        if (result.isEmpty()) {
            result = periods;
        } else {
            Collections.sort(result, (o1, o2) -> o1[0].compareTo(o2[0]));
        }

        //System.out.println(String.format("Total: %d", result.size()));

        return result;
    }



    private int prevIndex(int index) {
        if (index == 0)
            return 6;
        return index - 1;
    }

    private int nextIndex(int index) {
        if (index == 6)
            return 0;
        return index + 1;
    }

    private LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        if (a.isBefore(b)) {
            return b;
        }
        return a;
    }
    private LocalDateTime min(LocalDateTime a, LocalDateTime b) {
        if (a.isBefore(b)) {
            return a;
        }
        return b;
    }

}
