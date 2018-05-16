package test.app;

import javax.swing.text.DateFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.DAYS;

public class Test01 {



    public static void main(String[] args) {


        //LocalDateTime q1 = LocalDateTime.of(2018, 1, 1, 12, 12, 12);
        //LocalDateTime q2 = LocalDateTime.of(2018, 1, 2, 13, 12, 12);
        //out.println(q1.until(q2, ChronoUnit.MINUTES));

        boolean[] weekDays = new boolean[] { false, false, false, false, false, true, true};

        int begin =   8*60;
        int end   =  17*60;

        boolean nextDay = false;

        List<LocalDateTime> dayOffs = new ArrayList<>();
        dayOffs.add(LocalDateTime.of(2018, 3, 14, 0, 0, 0));
        dayOffs.add(LocalDateTime.of(2018, 3, 15, 0, 0, 0));

        LocalDateTime fd = LocalDateTime.of(2018, 3, 12, 11, 0, 0);
        LocalDateTime td = LocalDateTime.of(2018, 3, 16, 11, 0, 0);

        final LocalDateTime beginPeriod = fd.plusMinutes(0);
        final LocalDateTime endPeriod = td.plusMinutes(0);

        //out.println(fd);
        //out.println(td);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy'T'HH.mm");

        LocalDateTime a = fd.plusMinutes(0);

        long duration = fd.until(td, ChronoUnit.MINUTES);

        out.println(String.format("duration %d", duration));

        while (a.isBefore(td)) {

            if (nextDay) {



                /*
                LocalDateTime prevB = a.truncatedTo(DAYS);
                if (nextDay) {
                    prevB = prevB.plusMinutes(end);
                }
                LocalDateTime x1 = a.plusMinutes(0);

                int currIndex = a.getDayOfWeek().ordinal();
                int prevIndex = (currIndex == 0 ? 6 : currIndex - 1);
                boolean currIsDayOff = weekDays[currIndex] || dayOffs.contains(a.truncatedTo(DAYS));
                boolean prevIsDayOff = weekDays[prevIndex] || dayOffs.contains(a.minusDays(1).truncatedTo(DAYS));


                a = a.plusDays(1).truncatedTo(DAYS);
                */
            } else {
                LocalDateTime x1 = a.truncatedTo(DAYS);
                LocalDateTime x2 = end > 0 ? x1.plusMinutes(end): x1.plusDays(1);
                if (begin > 0) {
                    x1 = x1.plusMinutes(begin);
                }
                if (x1.isBefore(a)) {
                    x1 = a;
                }

                if (x2.isAfter(td)) {
                    x2 = td;
                }
                int currIndex = a.getDayOfWeek().ordinal();
                boolean currIsDayOff = weekDays[currIndex] || dayOffs.contains(a.truncatedTo(DAYS));
                if (!currIsDayOff) {
                    long dur = x1.until(x2, ChronoUnit.MINUTES);
                    if (begin == 0 && end == 0) {

                    } else {
                        duration -= begin;
                        duration -= (24 * 60) - end;
                    }

                    out.println(String.format("worday %s %s dur %d", x1.format(formatter), x2.format(formatter), dur));
                } else {

                    long dur = x1.until(x2, ChronoUnit.MINUTES);
                    duration -= dur;

                    out.println(String.format("dayOff %s %s dur %d", x1.format(formatter), x2.format(formatter), dur));
                }
            }
            a = a.plusDays(1).truncatedTo(DAYS);
        }

        long mm = duration % 60;
        long hh = ( duration - mm ) / 60;
        out.println(String.format("duration %d %02d:%02d", duration, hh, mm));

    }

    private static boolean prevWeekDayIsDayOff(boolean[] weekDays, int index) {
        if (index == 0)
            index = 6;
        else
            index -= 1;
        return weekDays[index];
    }


}
