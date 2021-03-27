package com.javase11.certification.textdatenumeric;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeValueFormat {

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        System.out.println(today.plusYears(1).getDayOfWeek());
        LocalTime teaTime = LocalTime.of(17, 30);
        Duration timeGap = Duration.between(LocalTime.now(), teaTime);
        System.out.println(timeGap);
        LocalDateTime tomorrowTeaTime = LocalDateTime.of(today.plusDays(1), teaTime);
        System.out.println(tomorrowTeaTime);

        ZoneId london = ZoneId.of("Europe/London");
        ZoneId brazil = ZoneId.of("America/Sao_Paulo");
        ZonedDateTime londonTime = ZonedDateTime.of(tomorrowTeaTime, london);
        System.out.println(londonTime);
        ZonedDateTime brazilTime = londonTime.withZoneSameInstant(brazil);
        System.out.println(brazilTime);

        Locale locale = Locale.UK;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EE', 'd' of 'MMM yyyy' at 'HH:mm z", locale);
        String timeTxt = dateFormat.format(brazilTime);
        System.out.println(timeTxt);

    }

}
