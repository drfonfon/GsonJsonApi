package com.fonfon.gsonjsonapi.model;

import com.fonfon.gsonjsonapi.models.relationship.One;
import com.fonfon.gsonjsonapi.Type;
import com.fonfon.gsonjsonapi.models.Resource;

@Type("comments")
public class Comment extends Resource {
  private String body;
  private One<Person> author;

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public One<Person> getAuthor() {
    return author;
  }

  public void setAuthor(One<Person> author) {
    this.author = author;
  }
}
