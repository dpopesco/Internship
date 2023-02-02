package org.example.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum Title {
    @JsonProperty("mr")
    MR,
    @JsonProperty("ms")
    MS,
    @JsonProperty("mrs")
    MRS,
    @JsonProperty("miss")
    MISS,
    @JsonProperty("dr")
    DR
}
