package com.fonfon.gsonjsonapi.models.relationship;

import com.fonfon.gsonjsonapi.models.Document;
import com.fonfon.gsonjsonapi.models.Resource;
import com.fonfon.gsonjsonapi.models.ResourceIdentifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Many<T extends Resource> extends Relationship<List<T>> implements Iterable<ResourceIdentifier> {

  private final List<ResourceIdentifier> linkedResources = new ArrayList<>();

  public List<ResourceIdentifier> getLinkedResources() {
    return linkedResources;
  }

  public Many() {
  }

  public Many(ResourceIdentifier... resources) {
    for (ResourceIdentifier resource : resources)
      add(resource);
  }

  @Override
  public List<T> get(Document<?> document) {
    return get(document, null);
  }

  public List<T> get(Document<?> document, T defaultValue) {
    List<T> collector = new ArrayList<>(linkedResources.size());
    for (ResourceIdentifier resourceId : linkedResources) {
      T obj = document.find(resourceId);
      collector.add(obj == null ? defaultValue : obj);
    }
    return collector;
  }

  public ResourceIdentifier get(int position) {
    return linkedResources.get(position);
  }

  public List<ResourceIdentifier> get() {
    return linkedResources;
  }

  @Override
  public Iterator<ResourceIdentifier> iterator() {
    return linkedResources.iterator();
  }

  public boolean add(ResourceIdentifier identifier) {
    if (identifier == null) {
      return false;
    } else if (identifier.getClass() == ResourceIdentifier.class) {
      return linkedResources.add(identifier);
    } else {
      return add(identifier.getType(), identifier.getId());
    }
  }

  public boolean add(String type, String id) {
    return add(new ResourceIdentifier(type, id));
  }

  public boolean remove(ResourceIdentifier identifier) {
    return remove(identifier.getType(), identifier.getId());
  }

  public boolean remove(String type, String id) {
    return linkedResources.remove(new ResourceIdentifier(type, id));
  }

  @Override
  public String toString() {
    return "Many{linkedResources=" + linkedResources + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return linkedResources.equals(((Many) o).linkedResources);
  }
}
