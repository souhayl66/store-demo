package com.example.store.util;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TrialChecker {
    private static final int TRIAL_DAYS = 30;

    // For now, set the demo start date manually
    private static final LocalDate START_DATE = LocalDate.of(2026, 4, 16);

    public static boolean isTrialExpired() {
        long days = ChronoUnit.DAYS.between(START_DATE, LocalDate.now());
        return days >= TRIAL_DAYS;
    }
}
