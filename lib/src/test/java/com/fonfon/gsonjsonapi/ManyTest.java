package com.fonfon.gsonjsonapi;

import com.fonfon.gsonjsonapi.models.Document;
import com.fonfon.gsonjsonapi.models.relationship.Many;
import com.fonfon.gsonjsonapi.model.Comment;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@SuppressWarnings("all")
public class ManyTest {

  private Many<Comment> comments(int size) {
    ArrayList<Comment> comments = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      Comment comment = new Comment();
      comment.setId("" + i);
      comments.add(comment);
    }
    return new Many<Comment>(comments.toArray(new Comment[size]));
  }

  @Test
  public void equality() throws Exception {
    assertEquals(comments(0), comments(0));
    assertEquals(comments(1), comments(1));
    assertEquals(comments(2), comments(2));
  }

  @Test
  public void resolution() {
    Document document = new Document();
    Comment holder = new Comment();
    assertThat(comments(2).get(document), hasItems(nullValue(Comment.class), nullValue(Comment.class)));
    assertThat(comments(2).get(document, holder), hasItems(holder, holder));
    Comment[] comments = new Comment[]{new Comment(), new Comment()};
    comments[0].setId("0");
    comments[1].setId("1");
    document.include(comments[0]);
    assertThat(comments(2).get(document), hasItems(equalTo(comments[0]), nullValue(Comment.class)));
    document.include(comments[1]);
    assertThat(comments(2).get(document), hasItems(comments[0], comments[1]));
  }

  @Test
  public void serialization() throws Exception {
    assertThat(TestUtil.gson().getAdapter(Many.class).toJson(comments(2)),
        equalTo("{\"data\":[{\"type\":\"comments\",\"id\":\"0\"},{\"type\":\"comments\",\"id\":\"1\"}]}"));
  }

  @Test
  public void deserialization() throws Exception {
    assertThat(TestUtil.gson().getAdapter(Many.class).fromJson("{\"data\":[{\"type\":\"comments\",\"id\":\"0\"},{\"type\":\"comments\",\"id\":\"1\"}]}"),
        equalTo((Many) comments(2)));
  }

}
