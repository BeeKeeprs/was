package kr.co.webee.domain.hive.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@RequiredArgsConstructor
public enum Interval implements SlotStrategy {
    TEN_SEC(10, DateTimeFormatter.ofPattern("HH:mm:ss")),
    ONE_MIN(60, DateTimeFormatter.ofPattern("HH:mm")),
    FIVE_MIN(300, DateTimeFormatter.ofPattern("HH:mm")),
    TEN_MIN(600, DateTimeFormatter.ofPattern("HH:mm"));

    private final int seconds;
    private final DateTimeFormatter labelFormatter;

    public LocalDateTime truncate(LocalDateTime dateTime) {
        int secondOfHour = dateTime.getMinute() * 60 + dateTime.getSecond();
        return dateTime.truncatedTo(ChronoUnit.HOURS)
                .plusSeconds((long) (secondOfHour / seconds) * seconds);
    }

    public LocalDateTime next(LocalDateTime dateTime) {
        return dateTime.plusSeconds(seconds);
    }

    public String formatLabel(LocalDateTime dateTime) {
        return dateTime.format(labelFormatter);
    }
}
