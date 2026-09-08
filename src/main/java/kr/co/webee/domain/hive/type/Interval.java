package kr.co.webee.domain.hive.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@RequiredArgsConstructor
public enum Interval implements SlotStrategy {
    ONE_MIN(1),
    FIVE_MIN(5),
    TEN_MIN(10);

    private final int minutes;
    private static final DateTimeFormatter LABEL_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public LocalDateTime truncate(LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.HOURS)
                .plusMinutes((long) (dateTime.getMinute() / minutes) * minutes);
    }

    public LocalDateTime next(LocalDateTime dateTime) {
        return dateTime.plusMinutes(minutes);
    }

    public String formatLabel(LocalDateTime dateTime) {
        return dateTime.format(LABEL_FORMATTER);
    }
}
