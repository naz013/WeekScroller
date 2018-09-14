package com.github.naz013.weekscroller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

public class Week {

    @NonNull
    private List<Day> days = new LinkedList<>();
    @NonNull
    private DateTime start = new DateTime();
    @NonNull
    private DateTime end = new DateTime();

    public Week fromWeek(@NonNull Week week) {
        setDays(week.days);
        setEnd(week.end);
        setStart(week.start);
        return this;
    }

    public Week setDays(@NonNull List<Day> days) {
        this.days = days;
        return this;
    }

    public Week setEnd(@NonNull DateTime end) {
        this.end = end;
        return this;
    }

    public Week setStart(@NonNull DateTime start) {
        this.start = start;
        return this;
    }

    @NonNull
    public DateTime getEnd() {
        return end;
    }

    @NonNull
    public DateTime getStart() {
        return start;
    }

    @NonNull
    public List<Day> getDays() {
        return days;
    }

    @Nullable
    public DateTime findSelected() {
        for (Day day : days) {
            if (day.isSelected()) {
                return day.getDateTime();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Week{" +
                "days=" + days +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
