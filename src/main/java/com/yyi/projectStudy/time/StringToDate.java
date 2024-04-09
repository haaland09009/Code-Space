package com.yyi.projectStudy.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class StringToDate {
    /* 분 단위로 변환하는 함수 */
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

    /* 일 단위로 변환하는 함수 (메시지 전용) */
    public static String formatChatTime(String dateString) {
        LocalDateTime date = LocalDateTime.parse(dateString);
        LocalDateTime now = LocalDateTime.now();

        long daysDiff = ChronoUnit.DAYS.between(date.toLocalDate(), now.toLocalDate());
        if (daysDiff == 0) {
            return formatHourAndMinute(date);
        } else {
            return formatMonthAndDate(date);
        }
    }

    /* 오전, 오후 구분 함수 */
    private static String formatHourAndMinute(LocalDateTime date) {
        int hour = date.getHour();
        int minute = date.getMinute();
        String period = (hour < 12) ? "오전" : "오후";
        // 0시일 경우, 12로 변경
        if (hour == 0) {
            hour = 12;
        }
        // 오후인 경우 12를 빼서 12시간 형식으로 변경
        else if (hour > 12) {
            hour -= 12;
        }
        return String.format("%s %d:%02d", period, hour, minute);
    }

    /* 년,월,일 변환 함수 */
    private static String formatMonthAndDate(LocalDateTime date) {
        LocalDate localDate = date.toLocalDate();
        LocalDate now = LocalDate.now();

        // 현재 연도와 비교
        if (localDate.getYear() == now.getYear()) {
            return localDate.format(DateTimeFormatter.ofPattern("M월 d일"));
        } else {
            return localDate.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));
        }
    }


    public static void main(String[] args) {
        System.out.println(formatChatTime("2024-03-17T10:30:00"));
        System.out.println(formatChatTime("2024-04-07T10:30:00"));
        System.out.println(formatChatTime("2024-04-07T00:00:00"));
        System.out.println(formatChatTime("2024-04-07T12:00:00"));
        System.out.println(formatChatTime("2024-04-06T15:25:00"));
        System.out.println(formatChatTime("2023-12-03T08:45:00"));
    }
}
