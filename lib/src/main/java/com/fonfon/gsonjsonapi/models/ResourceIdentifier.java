package com.fonfon.gsonjsonapi.models;

import com.google.gson.JsonElement;

public class ResourceIdentifier {

  private String type;
  private String id;

  private JsonElement meta;

  public ResourceIdentifier() {
    this(null, null);
  }

  public ResourceIdentifier(ResourceIdentifier identifier) {
    this(identifier.getType(), identifier.getId());
  }

  public ResourceIdentifier(String type, String id) {
    this.type = type;
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public JsonElement getMeta() {
    return meta;
  }

  public void setMeta(JsonElement meta) {
    this.meta = meta;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{type='" + type + "\'" + ", id='" + id + "\'}";
  }

  @SuppressWarnings("SimplifiableIfStatement")
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !getClass().equals(o.getClass())) return false;
    ResourceIdentifier that = (ResourceIdentifier) o;
    if (type != null ? !type.equals(that.type) : that.type != null) return false;
    return id != null ? id.equals(that.id) : that.id == null;
  }

  @Override
  public int hashCode() {
    int result = type != null ? type.hashCode() : 0;
    result = 31 * result + (id != null ? id.hashCode() : 0);
    return result;
  }
}
