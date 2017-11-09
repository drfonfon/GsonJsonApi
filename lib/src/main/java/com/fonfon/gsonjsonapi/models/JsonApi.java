package com.fonfon.gsonjsonapi.models;

import com.google.gson.JsonElement;

public class JsonApi {

  private String version;
  private JsonElement meta;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public JsonElement getMeta() {
    return meta;
  }

  public void setMeta(JsonElement meta) {
    this.meta = meta;
  }

  @Override
  public int hashCode() {
    return 31 * (version != null ? version.hashCode() : 0) + (meta != null ? meta.hashCode() : 0);
  }
}
