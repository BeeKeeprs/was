package kr.co.webee.domain.product.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Origin {
    DOMESTIC("국내산"),
    IMPORTED("수입산");

    private final String label;

    Origin(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static Origin fromLabel(String input) {
        for (Origin o : values()) {
            if (o.label.equals(input)) {
                return o;
            }
        }
        throw new IllegalArgumentException("Invalid Origin: " + input);
    }
}
