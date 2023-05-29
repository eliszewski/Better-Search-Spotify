package com.eliszewski.bettersearch.domain.enumeration;

/**
 * The SearchParameterType enumeration.
 */
public enum SearchParameterType {
    STARTSWITH("Starts-with"),
    CONTAINS("Contains"),
    ENDSWITH("Ends-with"),
    BEFOREDATE("Before-Date"),
    AFTERDATE("After-Date"),
    REGEX("Regular-Expression"),
    TRACKNUMBER("Track-Number"),
    ALBUMORDER("Album-Order"),
    LENGTH("Length"),
    OVERLENGTH("Over-Length"),
    UNDERLENGTH("Under-Length");

    private final String value;

    SearchParameterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
