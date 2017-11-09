package com.fonfon.gsonjsonapi.models;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Document<T extends ResourceIdentifier> {

  private List<T> datalist = new ArrayList<T>();
  private T data = null;

  private boolean isExplicitNull = false;
  private boolean isArrayDocument = false;

  private List<Error> errors = new ArrayList<>(0);
  private Map<ResourceIdentifier, Resource> included = new HashMap<>(0);

  private JsonApi jsonApi;
  private JsonElement links;
  private JsonElement meta;

  public JsonElement getMeta() {
    return meta;
  }

  public void setMeta(JsonElement meta) {
    this.meta = meta;
  }

  public Document(){
  }

  public void setArrayDocument(boolean arrayDocument) {
    isArrayDocument = arrayDocument;
  }

  public boolean isArrayDocument() {
    return isArrayDocument;
  }

  public void setSingleData(T data) {
    this.data = data;
  }

  public T getSingleData() {
    return data;
  }

  public void setListData(List<T> data) {
    this.datalist = data;
    if(data != null && data.size() > 0)
      isArrayDocument = true;
  }

  public List<T> getListData() {
    return datalist;
  }

  public void setNull(boolean isNull) {
    isExplicitNull = isNull;
  }

  public boolean isNull() {
    return isExplicitNull;
  }

  public Map<ResourceIdentifier, Resource> getIncluded() {
    return included;
  }

  public List<Error> getErrors() {
    return errors;
  }

  public boolean include(Resource resource) {
    included.put(new ResourceIdentifier(resource), resource);
    return true;
  }

  public boolean exclude(Resource resource) {
    included.remove(new ResourceIdentifier(resource));
    return true;
  }

  @SuppressWarnings({"SuspiciousMethodCalls", "unchecked"})
  public <T extends Resource> T find(ResourceIdentifier resourceIdentifier) {
    return (T) included.get(resourceIdentifier);
  }

  public boolean errors(List<Error> errors) {
    this.errors.clear();
    if (errors != null)
      this.errors.addAll(errors);
    return true;
  }

  public List<Error> errors() {
    return this.errors;
  }

  public boolean hasError() {
    return errors.size() != 0;
  }

  public JsonElement getLinks() {
    return links;
  }

  public void setLinks(JsonElement links) {
    this.links = links;
  }

  public void setJsonApi(JsonApi jsonApi) {
    this.jsonApi = jsonApi;
  }

  public JsonApi getJsonApi() {
    return jsonApi;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Document<?> document = (Document<?>) o;

    if (!included.equals(document.included)) return false;
    if (!errors.equals(document.errors)) return false;
    if (getMeta() != null ? !getMeta().equals(document.getMeta()) : document.getMeta() != null)
      return false;
    if (links != null ? !links.equals(document.links) : document.links != null) return false;
    if (jsonApi != null ? jsonApi.equals(document.jsonApi) : document.jsonApi == null) return false;
    if (isExplicitNull != document.isExplicitNull) return false;
    return true;
  }

  @Override
  public int hashCode() {
    int result = included.hashCode();
    result = 31 * result + errors.hashCode();
    result = 31 * result + (getMeta() != null ? getMeta().hashCode() : 0);
    result = 31 * result + (links != null ? links.hashCode() : 0);
    result = 31 * result + (jsonApi != null ? jsonApi.hashCode() : 0);
    return result;
  }
}