package com.fonfon.gsonjsonapi;

import com.fonfon.gsonjsonapi.models.relationship.One;
import com.fonfon.gsonjsonapi.models.Document;
import com.fonfon.gsonjsonapi.model.Person;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OneTest {

  private One<Person> createHasOne() {
    return new One<>("people", "5");
  }

  @Test
  public void equality() throws Exception {
    assertEquals(createHasOne(), createHasOne());
  }

  @Test
  public void resolution() throws Exception {
    Document document = new Document();
//    assertNull(createHasOne().get(document));

    //Person holder = new Person();
  //  assertEquals(createHasOne().get(document, holder), holder);

    Person person = new Person();
    person.setId("5");
    document.include(person);
    assertEquals(createHasOne().get(document), person);
  }

  @Test
  public void serialization() throws Exception {
    assertThat(TestUtil.gson().getAdapter(One.class).toJson(createHasOne()),
        equalTo("{\"data\":{\"type\":\"people\",\"id\":\"5\"}}"));
  }

  @Test
  public void deserialization() throws Exception {
    assertThat(TestUtil.gson().getAdapter(One.class).fromJson("{\"data\":{\"type\":\"people\",\"id\":\"5\"}}"),
        equalTo((One) createHasOne()));
  }

}
