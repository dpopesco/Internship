package org.example.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    private final String userGender;

    private Gender(String type) {
        this.userGender = type;
    }
}
