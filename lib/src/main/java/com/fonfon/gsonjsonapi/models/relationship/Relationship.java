package com.fonfon.gsonjsonapi.models.relationship;

import com.fonfon.gsonjsonapi.models.Document;
import com.google.gson.JsonElement;

public abstract class Relationship<T> {

  private JsonElement meta;
  private JsonElement links;

  public JsonElement getMeta() {
    return meta;
  }

  public void setMeta(JsonElement meta) {
    this.meta = meta;
  }

  public JsonElement getLinks() {
    return links;
  }

  public void setLinks(JsonElement links) {
    this.links = links;
  }

  public abstract T get(Document<?> document);

}
