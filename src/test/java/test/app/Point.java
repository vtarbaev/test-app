package test.app;

import java.time.LocalDateTime;

public class Point {

    LocalDateTime date;
    boolean begin;

    public Point(LocalDateTime date, boolean begin) {
        this.date = date;
        this.begin = begin;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isBegin() {
        return begin;
    }

    public boolean isEnd() {
        return !begin;
    }

    public void setBegin(boolean begin) {
        this.begin = begin;
    }
}
