package org.example.models.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {
    @JsonProperty(value = "id", required = true)
    private String id;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "post", required = true)
    private String postId;
}
