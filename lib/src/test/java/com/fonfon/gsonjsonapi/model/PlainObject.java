package com.fonfon.gsonjsonapi.model;

import com.fonfon.gsonjsonapi.models.relationship.Many;
import com.fonfon.gsonjsonapi.models.relationship.One;
import com.fonfon.gsonjsonapi.Type;
import com.fonfon.gsonjsonapi.models.Resource;

@Type("articles")
public class PlainObject extends Resource {
  public String title;
  public One<Person> author;
  public Many<Comment> comments;
  public transient String ignored;
}
