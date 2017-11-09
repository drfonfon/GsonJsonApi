package com.fonfon.gsonjsonapi.model;

import com.fonfon.gsonjsonapi.models.relationship.Many;
import com.fonfon.gsonjsonapi.models.relationship.One;
import com.fonfon.gsonjsonapi.models.Resource;
import com.fonfon.gsonjsonapi.Type;

@Type("articles")
public class Article extends Resource {

  private String title;
  private One<Person> author;
  private Many<Comment> comments;
  private transient String ignored;
  private String nullable;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public One<Person> getAuthor() {
    return author;
  }

  public void setAuthor(One<Person> author) {
    this.author = author;
  }

  public Many<Comment> getComments() {
    return comments;
  }

  public void setComments(Many<Comment> comments) {
    this.comments = comments;
  }

  public String getIgnored() {
    return ignored;
  }

  public void setIgnored(String ignored) {
    this.ignored = ignored;
  }

  public String getNullable() {
    return nullable;
  }

  public void setNullable(String nullable) {
    this.nullable = nullable;
  }
}
