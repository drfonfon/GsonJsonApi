package com.fonfon.gsonjsonapi.models.relationship;


import com.fonfon.gsonjsonapi.models.Document;
import com.fonfon.gsonjsonapi.models.Resource;
import com.fonfon.gsonjsonapi.models.ResourceIdentifier;

public final class One<T extends Resource> extends Relationship<T> {

  private ResourceIdentifier linkedResource;

  public One() {
  }

  public One(String type, String id) {
    this(new ResourceIdentifier(type, id));
  }

  public One(ResourceIdentifier linkedResource) {
    set(linkedResource);
  }

  @Override
  public T get(Document<?> document) {
    return get(document, null);
  }

  public T get(Document<?> document, T defaultValue) {
    T obj = document.find(linkedResource);
    return obj == null ? defaultValue : obj;
  }

  public ResourceIdentifier get() {
    return linkedResource;
  }

  public void set(ResourceIdentifier identifier) {
    if (identifier == null) {
      linkedResource = null;
    } else if (ResourceIdentifier.class == identifier.getClass()) {
      linkedResource = identifier;
    } else {
      set(identifier.getType(), identifier.getId());
    }
  }

  public void set(String type, String id) {
    set(new ResourceIdentifier(type, id));
  }

  @Override
  public String toString() {
    return "One{linkedResource=" + linkedResource + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    One<?> one = (One<?>) o;
    return linkedResource != null ? linkedResource.equals(one.linkedResource) : one.linkedResource == null;
  }

  @Override
  public int hashCode() {
    return linkedResource != null ? linkedResource.hashCode() : 0;
  }

}
