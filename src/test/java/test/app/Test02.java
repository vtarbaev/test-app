package test.app;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Test02 {

    public static void main(String[] args) {
        long dur = 0;


        LocalDateTime fd = LocalDateTime.of(2018, 1, 1, 11, 0, 0);
        LocalDateTime td = LocalDateTime.of(2018, 1, 5, 11, 0, 0);

        List<LocalDateTime> holidays = new ArrayList<>();
        holidays.add(LocalDateTime.of(2018, 1, 3, 0, 0, 0));
        holidays.add(LocalDateTime.of(2018, 1, 4, 0, 0, 0));

        Helper2 h = new Helper2();

        long duration = 0;

        duration = h.calc(fd, td, new boolean[] { false,false,false,false,false,true,true }, holidays, 0, 0, false, false, 0, 0, false);
        System.out.println(String.format("1) %d", duration));

        System.out.println("=================");

        duration = h.calc(fd, td, new boolean[] { false,false,false,false,false,true,true }, holidays, 8*60, 17*60, true, false, 12*60, 13*60, true);
        System.out.println(String.format("2) %d", duration));

        System.out.println("=================");

        duration = h.calc(fd, td, new boolean[] { false,false,false,false,false,true,true }, holidays, 8*60,  1*60, true, true, 12*60, 13*60, true);
        System.out.println(String.format("3) %d", duration));

        td = LocalDateTime.of(2018, 1, 10, 19, 0, 0);
        duration = h.calc(fd, td, new boolean[] { false,false,false,false,false,true,true }, holidays, 8*60,  20*60, true, false, 12*60, 13*60, true);
        System.out.println(String.format("3) %d", duration));
        /*
        Helper helper = new Helper();
        dur = fd.until(td, ChronoUnit.MINUTES);
        System.out.println(dur);
        dur = helper.calcNotNextDay(fd, td, new boolean[] { false,false,false,false,false,true,true }, days);
        System.out.println(dur);
        dur = helper.calcNotNextDay(fd, td, new boolean[] { false,false,false,false,true ,true,true }, days);
        System.out.println(dur);
        dur = helper.calcNotNextDay(fd, td, new boolean[] { false,false,false,false,false,true,true }, days, 8 * 60, 17 * 60);
        System.out.println(dur);
        dur = helper.calcNextDay(fd, td, new boolean[] { false,false,false,false,false,true,true }, days, 8 * 60, 1 * 60);
        System.out.println(dur);
        */
    }

}
