package org.example.tests.api.rest.wrapper.post;

import lombok.extern.slf4j.Slf4j;
import org.example.models.post.PostGET;
import org.example.models.post.PostsCollection;
import org.example.tests.api.rest.wrapper.ApiBaseClass;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@Slf4j
public class GETPostsTest extends ApiBaseClass {
    @Test
    public void getPostList() {

        PostsCollection response = restWrapper.usingPosts().getPosts();

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getUserPostsById() {

        String id = "60d21b4967d0d8992e610c90";

        PostGET response = restWrapper.usingPosts().getInfoByPostId(id);

        log.info("Validate response post id as per request!");
        assertEquals(response.getId(), id);

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getUserPostsByUserId() {

        String id = "60d0fe4f5311236168a109ed";

        PostsCollection response = restWrapper.usingPosts().getInfoByUserId(id);

        log.info("Validate response owner id as per request!");
        assertEquals(response.getData().get(0).getUser().getId(), id);

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }

    @Test
    public void getPostsByTag() {

        String tag = "ice";

        PostsCollection response = restWrapper.usingPosts().getInfoByTag(tag);

        log.info("Validate response tag as per request!");
        assertTrue(response.getData().get(0).getTags().contains(tag));

        log.info("Validate status code!");
        int statusCode = restWrapper.getStatusCode();
        assertEquals(statusCode, SC_OK);
    }
}
