package kr.co.webee.domain.hive.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Period {
    DAY("하루", DateTimeFormatter.ofPattern("HH:00")),
    WEEK("일주일", DateTimeFormatter.ofPattern("MM/dd")),
    MONTH("한달", DateTimeFormatter.ofPattern("MM/dd"));

    private final String description;
    private final DateTimeFormatter labelFormatter;

    public LocalDateTime truncate(LocalDateTime dateTime) {
        return switch (this) {
            case DAY -> dateTime.truncatedTo(ChronoUnit.HOURS);
            case WEEK -> dateTime.toLocalDate().atStartOfDay();
            case MONTH -> dateTime.toLocalDate()
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .atStartOfDay();
        };
    }

    public LocalDateTime startFrom(LocalDateTime now) {
        return truncate(switch (this) {
            case DAY -> now.minusDays(1);
            case WEEK -> now.minusWeeks(1);
            case MONTH -> now.minusMonths(1);
        });
    }

    public LocalDateTime next(LocalDateTime dateTime) {
        return switch (this) {
            case DAY -> dateTime.plusHours(1);
            case WEEK -> dateTime.plusDays(1);
            case MONTH -> dateTime.plusWeeks(1);
        };
    }

    public String formatLabel(LocalDateTime dateTime) {
        return dateTime.format(labelFormatter);
    }
}
