package kr.co.webee.domain.hive.type;

import java.time.LocalDateTime;

public interface SlotStrategy {
    LocalDateTime truncate(LocalDateTime dateTime);
    LocalDateTime next(LocalDateTime dateTime);
    String formatLabel(LocalDateTime dateTime);
}
