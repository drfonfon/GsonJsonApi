package com.fonfon.gsonjsonapi;

import com.fonfon.gsonjsonapi.models.ResourceIdentifier;

import org.junit.Test;

import static com.fonfon.gsonjsonapi.TestUtil.gson;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ResourceIdentifierTest {

  private static final String testData = "{\"type\":\"people\",\"id\":\"11\"}";

  private ResourceIdentifier createResourceIdentifier() {
    return new ResourceIdentifier("people", "11");
  }

  @Test
  public void equality_of_identifier_vs_identifier() throws Exception {
    ResourceIdentifier identifier = createResourceIdentifier();
    assertEquals(identifier, new ResourceIdentifier(identifier));
    assertEquals(identifier, createResourceIdentifier());
  }

  @Test
  public void serialization() throws Exception {
    ResourceIdentifier identifier = createResourceIdentifier();
    assertThat(TestUtil.gson().getAdapter(ResourceIdentifier.class).toJson(identifier), equalTo(testData));
  }

  @Test
  public void deserialization() throws Exception {
    ResourceIdentifier identifier = createResourceIdentifier();
    assertThat(gson().getAdapter(ResourceIdentifier.class).fromJson(testData),
        equalTo(identifier));
  }

}
