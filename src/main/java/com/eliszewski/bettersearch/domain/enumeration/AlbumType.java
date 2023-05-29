package com.eliszewski.bettersearch.domain.enumeration;

/**
 * The AlbumType enumeration.
 */
public enum AlbumType {
    ALBUM("album"),
    COMPILATION("compilation"),
    SINGLE("single");

    private final String value;

    AlbumType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
