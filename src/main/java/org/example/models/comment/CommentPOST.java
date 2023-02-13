package org.example.models.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Getter
@Setter
public class CommentPOST {
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "owner", required = true)
    private String ownerId;
    @JsonProperty(value = "post", required = true)
    private String postId;

    public CommentPOST() {
    }

    public CommentPOST(String message, String ownerId, String postId) {
        setMessage(message);
        setOwnerId(ownerId);
        setPostId(postId);
    }

    public static CommentPOST generateComment() {
        String msg = randomAlphanumeric(30);
        String ownerId = "60d0fe4f5311236168a109d0";
        String postId = "60d21b7967d0d8992e610d1b";
        return new CommentPOST(msg, ownerId, postId);
    }
}
