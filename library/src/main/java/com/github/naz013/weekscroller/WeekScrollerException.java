package com.github.naz013.weekscroller;

public class WeekScrollerException extends Exception {

    public WeekScrollerException() {
        this("Something went wrong");
    }

    public WeekScrollerException(String message) {
        super(message);
    }
}
