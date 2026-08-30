package kr.co.webee.application.gate.dto.request;

import java.time.LocalDateTime;

public record GateBeeCountRequest(
        Integer entranceIn,
        Integer entranceOut,
        Integer exitIn,
        Integer exitOut,
        LocalDateTime timestamp
) {
}
