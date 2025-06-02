package kr.co.webee.domain.product.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionType {
    PURCHASE("구매"),
    RENTAL("임대");

    private final String label;

    TransactionType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static TransactionType fromLabel(String input) {
        for (TransactionType t : values()) {
            if (t.label.equals(input)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid TransactionType: " + input);
    }
}
