package com.fonfon.gsonjsonapi.models;

import com.google.gson.JsonElement;

public class Error {

  private String id;
  private String status;
  private String code;
  private String title;
  private String detail;

  private JsonElement meta;
  private JsonElement source;
  private JsonElement links;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

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

  public JsonElement getSource() {
    return source;
  }

  public void setSource(JsonElement source) {
    this.source = source;
  }

  @Override
  public String toString() {
    return "Error{" +
        "id='" + id + '\'' +
        ", status='" + status + '\'' +
        ", code='" + code + '\'' +
        ", title='" + title + '\'' +
        ", detail='" + detail + '\'' +
        '}';
  }

  @SuppressWarnings("SimplifiableIfStatement")
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Error error = (Error) o;

    if (id != null ? !id.equals(error.id) : error.id != null) return false;
    if (status != null ? !status.equals(error.status) : error.status != null) return false;
    if (code != null ? !code.equals(error.code) : error.code != null) return false;
    if (title != null ? !title.equals(error.title) : error.title != null) return false;
    return detail != null ? detail.equals(error.detail) : error.detail == null;
  }
}
