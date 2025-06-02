package kr.co.webee.domain.product.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionMethod {
    ONLINE("온라인"),
    OFFLINE("오프라인");

    private final String value;

    TransactionMethod(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TransactionMethod from(String input) {
        for (TransactionMethod m : values()) {
            if (m.value.equals(input)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Invalid TransactionMethod value: " + input);
    }
}
