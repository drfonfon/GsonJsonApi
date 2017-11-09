package com.fonfon.gsonjsonapi.models;

import com.fonfon.gsonjsonapi.Type;
import com.google.gson.JsonElement;

public abstract class Resource extends ResourceIdentifier {

  public Resource() {
    setType(getClass().getAnnotation(Type.class).value());
  }

  private JsonElement links;

  public JsonElement getLinks() {
    return links;
  }

  public void setLinks(JsonElement links) {
    this.links = links;
  }
}
