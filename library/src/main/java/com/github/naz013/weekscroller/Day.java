package com.github.naz013.weekscroller;

import org.joda.time.DateTime;

public class Day {

    private boolean isSelected = false;
    private boolean isCurrent = false;
    private DateTime dateTime = new DateTime();

    public Day() {
    }

    public Day(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Day setSelected(boolean selected) {
        isSelected = selected;
        return this;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public Day setCurrent(boolean current) {
        isCurrent = current;
        return this;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public Day setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    @Override
    public String toString() {
        return "Day{" +
                "isSelected=" + isSelected +
                ", isCurrent=" + isCurrent +
                ", dateTime=" + dateTime +
                '}';
    }
}
