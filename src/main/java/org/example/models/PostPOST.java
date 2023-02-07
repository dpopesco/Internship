package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Getter
@Setter
public class PostPOST extends Post {
    @JsonProperty(value = "owner")
    private String ownerId;

    public PostPOST() {
    }

    public PostPOST(String text, String image, int likes, ArrayList<String> tags) {
        setText(text);
        setImage(image);
        setLikes(likes);
        setTags(tags);
    }

    public static PostPOST generateRandomPost() {
        String text = randomAlphanumeric(30);
        String image = "https://img.dummyapi.io/photo-1510414696678-2415ad8474aa.jpg";
        int likes = Integer.valueOf(randomNumeric(3));
        ArrayList<String> tags = new ArrayList<>();
        tags.add("dog");
        tags.add("cat");
        tags.add("dolphin");


        PostPOST newPost = new PostPOST(text, image, likes, tags);
        return newPost;
    }

}
