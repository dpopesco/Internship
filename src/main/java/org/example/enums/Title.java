package org.example.enums;

import lombok.Getter;

@Getter
public enum Title {
    MR("mr"),
    MS("ms"),
    MRS("mrs"),
    MISS("miss"),
    DR("dr");
    private final String userTitle;

    Title(String title) {
        this.userTitle = title;
    }
}
