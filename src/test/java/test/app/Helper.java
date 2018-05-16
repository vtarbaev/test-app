package test.app;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

public class Helper {

    public List<LocalDateTime[]> calc2(LocalDateTime fd, LocalDateTime td, boolean[] dayOffs, Long beginDay, Long endDay, boolean nextDay) {

        List<LocalDateTime[]> intervals = new ArrayList<>();

        LocalDateTime a = fd.plus(0, ChronoUnit.DAYS);

        boolean isFirst = true;

        while (a.isBefore(td)) {

            LocalDateTime beginCurrDay = a.truncatedTo(ChronoUnit.DAYS);
            LocalDateTime beginNextDay = a.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);
            LocalDateTime beginWork, endWork, prevEndWork = null;

            if (beginDay == null) {
                beginWork = beginCurrDay.plus(0, MINUTES);
            } else {
                beginWork = beginCurrDay.plus(beginDay, MINUTES);
            }

            if (endDay == null) {
                endWork = beginNextDay.plus(0, MINUTES);
            } else {
                endWork = beginCurrDay.plus(endDay, MINUTES);
                if (nextDay) {
                    endWork = endWork.plus(1, ChronoUnit.DAYS);
                    prevEndWork = beginCurrDay.plus(endDay, MINUTES);
                }
            }

            if (nextDay) {
                if (a.isBefore(prevEndWork)) {
                    print(1, a, prevEndWork);
                    intervals.add(new LocalDateTime[] { a, prevEndWork });

                    if (beginWork.isBefore(td)) {
                        print(2, beginWork, beginNextDay);
                        intervals.add(new LocalDateTime[]{beginWork, beginNextDay});
                    }
                } else if (a.isBefore(beginWork)) {
                    if (beginWork.isBefore(td)) {
                        print(3, beginWork, beginNextDay);
                        intervals.add(new LocalDateTime[]{beginWork, beginNextDay});
                    }
                } else if (a.isBefore(beginNextDay)) {
                    print(4, a, beginNextDay);
                    intervals.add(new LocalDateTime[] { a, beginNextDay });
                }
            } else {
                if (a.isBefore(beginWork)) {
                    if (beginWork.isBefore(td)) {
                        print(5, beginWork, endWork);
                        intervals.add(new LocalDateTime[]{beginWork, endWork});
                    }
                } else if (a.isBefore(endWork)) {
                    print(6, a, endWork);
                    intervals.add(new LocalDateTime[] { a, endWork });
                }
            }
            a = beginNextDay;
            //print(10, a, a);
        }

        return intervals;
    }

    public List<LocalDateTime[]> calc(LocalDateTime fd, LocalDateTime td, boolean[] dayOffs, Long beginDay, Long endDay, boolean nextDay) {
        List<LocalDateTime[]> list = new ArrayList<>();

        int d = 0;
        LocalDateTime a = fd.plus(d, ChronoUnit.DAYS);
        while (a.isBefore(td)) {
            boolean dayOff = dayOffs[a.getDayOfWeek().ordinal()];
            LocalDateTime beginNextDay = a.plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
            LocalDateTime beginCurrDay = a.truncatedTo(ChronoUnit.DAYS);

            if (beginNextDay.isBefore(td)) {
                if (dayOff) {
                    // выкидываем
                    a = beginNextDay;
                } else {
                    if (beginDay > 0 && endDay > 0) {
                        LocalDateTime x1 = beginCurrDay.plus(beginDay, MINUTES);
                        LocalDateTime x2 = beginCurrDay.plus(endDay, MINUTES);
                        if (a.isBefore(x1)) {
                            list.add(new LocalDateTime[] { x1, x2 });
                        } else if (a.isBefore(x2)) {
                            list.add(new LocalDateTime[] { a, x2 });
                        } else {
                            //list.add(new LocalDateTime[] { a, beginNextDay });
                        }
                    } else {
                        list.add(new LocalDateTime[]{a, beginNextDay});
                    }
                    a = beginNextDay;
                }

            } else {
                if (dayOff) {
                    // выкидываем
                    a = td;
                } else {
                    if (beginDay > 0 && endDay > 0) {
                        LocalDateTime x1 = beginCurrDay.plus(beginDay, MINUTES);
                        LocalDateTime x2 = beginCurrDay.plus(endDay, MINUTES);

                        System.out.println(String.format("1: %s %s", x1, x2));

                        if (td.isBefore(x1)) {

                        } else if (td.isBefore(x2)) {
                            list.add(new LocalDateTime[] { x1, td });
                        } else {
                            list.add(new LocalDateTime[] { x1, x2 });
                        }
                    } else {
                        list.add(new LocalDateTime[]{a, td});
                    }
                    a = td;
                }

            }
        }

        /*
        for (LocalDateTime[] localDateTimes : list) {
            System.out.println(String.format("%s %s", localDateTimes[0], localDateTimes[1]));
        }
        */

        return list;
    }

    private void print(int i, LocalDateTime a1, LocalDateTime a2) {
        System.out.println(String.format("%4d  %s %s", i, a1, a2));
    }

    public LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        if (a.isBefore(b)) {
            return b;
        }
        return a;
    }
    public LocalDateTime min(LocalDateTime a, LocalDateTime b) {
        if (a.isBefore(b)) {
            return a;
        }
        return b;
    }


    public boolean in(LocalDateTime point, LocalDateTime a, LocalDateTime b) {
        return !(point.isBefore(a) || point.isAfter(b));
    }

    public List<LocalDateTime[]> calc(LocalDateTime fd, LocalDateTime td, boolean nextDay, boolean[] offs, Integer begin, Integer end) {
        List<LocalDateTime[]> result = new ArrayList<>();

        LocalDateTime a = fd.plusSeconds(0);
        LocalDateTime b = td;

        while (a.isBefore(b)) {

            LocalDateTime x1 = a.truncatedTo(ChronoUnit.DAYS);
            LocalDateTime x4 = x1.plusDays(1);

            if (nextDay) {
                LocalDateTime x2 = x1.plusMinutes(end);
                LocalDateTime x3 = x1.plusMinutes(begin);

                boolean a1 = in(a, x1, x2);
                boolean a2 = in(a, x2, x3);
                boolean a3 = in(a, x3, x4);

                if (b.isBefore(x4)) {

                    boolean b1 = in(b, x1, x2);
                    boolean b2 = in(b, x2, x3);
                    boolean b3 = in(b, x3, x4);

                    if (a1 && b1) {
                        result.add(new LocalDateTime[]{a, b});
                    } else if (a1 && b2) {
                        result.add(new LocalDateTime[]{a, x2});
                    } else if (a1 && b3) {
                        result.add(new LocalDateTime[]{a, x2});
                        result.add(new LocalDateTime[]{x3, b});
                    } else if (a2 && b2) {
                        // nothing
                    } else if (a2 && b3) {
                        result.add(new LocalDateTime[]{x3, b});
                    } else if (a3 && b3) {
                        result.add(new LocalDateTime[]{a, b});
                    }
                } else {
                    if (a1) {
                        result.add(new LocalDateTime[]{a, x2});
                        result.add(new LocalDateTime[]{x3, x4});
                    } else if (a2) {
                        result.add(new LocalDateTime[]{x3, x4});
                    } else if (a3) {
                        result.add(new LocalDateTime[]{a, x4});
                    }
                }
                a = x4;
            } else {

            }

        }

        return result;
    }

    /**
     * Расчёт длительности без учета рабочего дня. Рабочий день принимается с 0 до 24
     */
    public long calcNotNextDay(LocalDateTime fd, LocalDateTime td, boolean[] dayOffs, List<LocalDateTime> days) {
        long duration = fd.until(td, MINUTES);
        LocalDateTime a = fd.plusMinutes(0);

        while (a.isBefore(td)) {
            LocalDateTime x1 = max(a.truncatedTo(DAYS), fd);
            LocalDateTime x2 = min(x1.plusDays(1), td);

            int index = x1.getDayOfWeek().ordinal();

            boolean isDayOff = dayOffs[index] || days.contains(x1);
            //System.out.println(String.format("%s %s %s", x1, dayOffs[index], days.contains(x1)));
            if (isDayOff) {
                duration -= x1.until(x2, MINUTES);
            }

            a = a.plusDays(1).truncatedTo(DAYS);
        }

        return duration;
    }

    /**
     * Расчёт длительности с началом и концом рабочего дня. например с 8 до 17
     */
    public long calcNotNextDay(LocalDateTime fd, LocalDateTime td, boolean[] dayOffs, List<LocalDateTime> days, int begin, int end) {
        long duration = fd.until(td, MINUTES);

        LocalDateTime a = fd.plusMinutes(0);

        while (a.isBefore(td)) {
            LocalDateTime bd = a.truncatedTo(DAYS);
            LocalDateTime ed = bd.plusDays(1);
            LocalDateTime x1 = max(bd.plusMinutes(begin), fd);
            LocalDateTime x2 = min(bd.plusMinutes(end  ), td);

            int index = x1.getDayOfWeek().ordinal();
            boolean isDayOff = dayOffs[index] || days.contains(x1.truncatedTo(DAYS));
            if (isDayOff) {
                duration -= 24 * 60;
            } else {
                if (bd.isBefore(fd)) { // первый день
                    if (fd.isBefore(x1)) {
                        duration = getDuration(fd, td, duration, ed, x1, x2);
                    } else if (fd.isBefore(x2)) {
                        if (td.isBefore(x2)) {
                            duration -= 0;
                        } else if (td.isBefore(ed)) {
                            duration -= x2.until(td, MINUTES);
                        } else {
                            duration -= x2.until(ed, MINUTES);
                        }
                    } else {
                        if (td.isBefore(ed)) {
                            duration -= fd.until(td, MINUTES);
                        } else {
                            duration -= fd.until(ed, MINUTES);
                        }
                    }
                } else {
                    duration = getDuration(bd, td, duration, ed, x1, x2);
                }
            }

            a = bd.plusDays(1);
        }

        return duration;
    }

    private long getDuration(LocalDateTime fd, LocalDateTime td, long duration, LocalDateTime ed, LocalDateTime x1, LocalDateTime x2) {
        if (td.isBefore(x1)) {
            duration -= fd.until(td, MINUTES);
        } else if (td.isBefore(x2)) {
            duration -= fd.until(x1, MINUTES);
        } else if (td.isBefore(ed)) {
            duration -= fd.until(x1, MINUTES);
            duration -= x2.until(td, MINUTES);
        } else {
            duration -= fd.until(x1, MINUTES);
            duration -= x2.until(ed, MINUTES);
        }
        return duration;
    }

    public long calcNextDay(LocalDateTime fd, LocalDateTime td, boolean[] dayOffs, List<LocalDateTime> days, int begin, int end) {
        long duration = fd.until(td, MINUTES);

        LocalDateTime a = fd.plusMinutes(0);

        while (a.isBefore(td)) {
            LocalDateTime bd = a.truncatedTo(DAYS);
            LocalDateTime ed = bd.plusDays(1);
            LocalDateTime x1 = max(bd.plusMinutes(begin), fd);
            LocalDateTime x2 = min(bd.plusMinutes(end  ), td);

            int index = x1.getDayOfWeek().ordinal();
            int prevIndex = index == 0 ? 6: index - 1;
            boolean isDayOff = dayOffs[index] || days.contains(x1.truncatedTo(DAYS));
            boolean prevIsDayOff = dayOffs[prevIndex] || days.contains(x1.truncatedTo(DAYS).minusDays(1));

            if (isDayOff) {
                if (prevIsDayOff) {
                    duration -= 24 * 60;
                } else {

                }
            }

        }

        return duration;
    }

}
