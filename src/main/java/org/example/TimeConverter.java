package org.example;

public class TimeConverter {

    // Given a time duration in: h, m, s it converts to milliseconds

    public int convertToMilliseconds(int h, int m, int s) {
        if (h >= 0 && h <= 23 && m >= 0 && m <= 59 && s >= 0 && s <= 59) {
            return h * 3600000 + m * 60000 + s * 1000;
        }
        return -1;
    }
}
