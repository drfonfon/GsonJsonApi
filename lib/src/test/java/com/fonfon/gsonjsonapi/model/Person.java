package com.fonfon.gsonjsonapi.model;

import com.fonfon.gsonjsonapi.Type;
import com.fonfon.gsonjsonapi.models.Resource;
import com.google.gson.annotations.SerializedName;

@Type("people")
public class Person extends Resource {

  @SerializedName("first-name")
  private String firstName;

  @SerializedName("last-name")
  private String lastName;

  private String twitter;
  private Integer age;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getTwitter() {
    return twitter;
  }

  public void setTwitter(String twitter) {
    this.twitter = twitter;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }
}
