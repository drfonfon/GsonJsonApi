package com.fonfon.gsonjsonapi.model;

import com.fonfon.gsonjsonapi.Type;
import com.fonfon.gsonjsonapi.models.Resource;
import com.fonfon.gsonjsonapi.models.relationship.One;

@Type("photos")
public class Photo extends Resource {

  private String url;
  private Boolean visible;
  private Double shutter;
  private Location location;
  private One<Person> author;

  public static class Location {
    public Double longitude;
    public Double latitude;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Boolean getVisible() {
    return visible;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
  }

  public Double getShutter() {
    return shutter;
  }

  public void setShutter(Double shutter) {
    this.shutter = shutter;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public One<Person> getAuthor() {
    return author;
  }

  public void setAuthor(One<Person> author) {
    this.author = author;
  }

  @Type("photos")
  public static class Photo3 extends Photo {

  }
}
