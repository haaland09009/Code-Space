package com.yyi.projectStudy.time;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class StringToDate {
    public static String formatDateTime(String dateString) {
        LocalDateTime date = LocalDateTime.parse(dateString);
        LocalDateTime now = LocalDateTime.now();

        long minutesDiff = ChronoUnit.MINUTES.between(date, now);

        if (minutesDiff < 60) {
            if (minutesDiff < 1) {
                return "방금 전";
            } else {
                return minutesDiff + "분 전";
            }
        } else if (minutesDiff < 1440) {
            long hoursDiff = minutesDiff / 60;
            return hoursDiff + "시간 전";
        } else if (minutesDiff < 30 * 24 * 60) {
            long daysDiff = minutesDiff / (24 * 60);
            return daysDiff + "일 전";
        } else if (minutesDiff < 12 * 30 * 24 * 60) {
            long monthsDiff = minutesDiff / (30 * 24 * 60);
            return monthsDiff + "달 전";
        }

        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int hours = date.getHour();
        int minutes = date.getMinute();
        String period = "오전";

        if (hours >= 12) {
            period = "오후";
            if (hours > 12) {
                hours -= 12;
            }
        } else if (hours == 0) {
            hours = 12;
        }

        return String.format("%d-%02d-%02d %s %02d:%02d", year, month, day, period, hours, minutes);
    }

    public static void main(String[] args) {
        System.out.println(formatDateTime("2024-03-17T10:30:00")); // Example usage
    }
}
