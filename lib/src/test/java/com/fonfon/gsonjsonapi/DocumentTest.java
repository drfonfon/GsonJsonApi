package com.fonfon.gsonjsonapi;

import com.fonfon.gsonjsonapi.adapters.DocumentAdapter;
import com.fonfon.gsonjsonapi.model.Article;
import com.fonfon.gsonjsonapi.model.Comment;
import com.fonfon.gsonjsonapi.model.Person;
import com.fonfon.gsonjsonapi.model.Photo;
import com.fonfon.gsonjsonapi.models.Document;
import com.fonfon.gsonjsonapi.models.Error;
import com.fonfon.gsonjsonapi.models.Resource;
import com.fonfon.gsonjsonapi.models.ResourceIdentifier;
import com.fonfon.gsonjsonapi.models.relationship.Many;
import com.fonfon.gsonjsonapi.models.relationship.One;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("all")
public class DocumentTest {

  @Test(expected = EOFException.class)
  public void deserialize_empty() throws Exception {
    getDocumentAdapter(Article.class).fromJson("");
  }

  @Test
  public void deserialize_null() throws Exception {
    assertNull(getDocumentAdapter(null).fromJson("null"));
  }

  @Test
  public void deserialize_object() throws Exception {
    Document<Article> document = TestUtil.gson(Article.class)
        .getAdapter(Document.class).fromJson(TestUtil.fromResource("/single.json"));
    assertThat(document, instanceOf(Document.class));
    Article article = document.getSingleData();
    assertThat(article.getId(), equalTo("1"));
    assertThat(article.getType(), equalTo("articles"));
    assertThat(article.getTitle(), equalTo("JSON API paints my bikeshed!"));
    assertThat(article.getAuthor().get(), equalTo(
        new ResourceIdentifier("people", "9")));
    assertThat(article.getComments().get(), hasItems(
        new ResourceIdentifier("comments", "5"),
        new ResourceIdentifier("comments", "12")));
  }

  @Test
  public void deserialize_object_null() throws Exception {
    Document<Article> document = getDocumentAdapter(Article.class).fromJson(TestUtil.fromResource("/single_null.json"));
    assertNull(document.getSingleData());
  }

  @Test
  public void deserialize_private_type() throws Exception {
    Document<Article2> document = TestUtil.gson(Article2.class)
        .getAdapter(Document.class).fromJson(TestUtil.fromResource("/single.json"));
    assertOnArticle1(document.getSingleData());
  }

  @Test(expected = RuntimeException.class)
  public void deserialize_no_default() throws Exception {
    TestUtil.gsonNoDefault(Article.class)
        .getAdapter(Document.class)
        .fromJson(TestUtil.fromResource("/multiple_compound.json"));
  }

  @Test
  public void deserialize_polymorphic_type() throws Exception {
    Resource resource = (Resource) getDocumentAdapter(Resource.class, Article.class)
        .fromJson(TestUtil.fromResource("/single.json")).getSingleData();
    assertThat(resource, instanceOf(Article.class));
    Article article = (Article) resource;
    assertThat(article.getId(), equalTo("1"));
    assertThat(article.getType(), equalTo("articles"));
    assertThat(article.getTitle(), equalTo("JSON API paints my bikeshed!"));
    assertThat(article.getAuthor().get(), equalTo(
        new ResourceIdentifier("people", "9")));
    assertThat(article.getComments().get(), hasItems(
        new ResourceIdentifier("comments", "5"),
        new ResourceIdentifier("comments", "12")));
  }

  @Test
  public void deserialize_polymorphic_fallback() throws Exception {
    Gson gson = TestUtil.gson();
    Resource resource = (Resource) gson.getAdapter(Document.class).fromJson(TestUtil.fromResource("/single.json")).getSingleData();
    assertThat(resource.getId(), equalTo("1"));
    assertThat(resource, instanceOf(TestUtil.Default.class));
  }

  @Test
  public void deserialize_multiple_objects() throws Exception {
    Gson gson = TestUtil.gson(Article.class, Person.class, Comment.class);
    Document<Article> document = gson.getAdapter(Document.class).fromJson(TestUtil.fromResource("/multiple_compound.json"));
    assertThat(document, instanceOf(Document.class));
    Document<Article> arrayDocument = document;
    assertThat(arrayDocument.getListData().size(), equalTo(1));
    assertOnArticle1(arrayDocument.getListData().get(0));
  }

  @Test
  public void deserialize_multiple_empty() throws Exception {
    Document<Article> document = getDocumentAdapter(Article.class).fromJson(TestUtil.fromResource("/multiple_empty.json"));
    assertThat(document, instanceOf(Document.class));
    Document<Article> arrayDocument = document;
    assertTrue(arrayDocument.getListData().isEmpty());
  }

  @Test
  public void deserialize_multiple_polymorphic() throws Exception {
    Document<Resource> document = getDocumentAdapter(Resource.class, Article.class, Photo.class).fromJson(TestUtil.fromResource("/multiple_polymorphic.json"));
    assertThat(document, instanceOf(Document.class));
    Document<Resource> arrayDocument = document;
    assertThat(arrayDocument.getListData().get(0), instanceOf(Article.class));
    assertThat(arrayDocument.getListData().get(1), instanceOf(Photo.class));
    assertOnArticle1((Article) arrayDocument.getListData().get(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void deserialize_multiple_polymorphic_type_policy_ex1() throws Exception {
    TestUtil.gson(Photo.class, Photo.Photo3.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void deserialize_multiple_polymorphic_type_policy_ex1_sym() throws Exception {
    TestUtil.gson(Photo.Photo3.class, Photo.class);
  }

  @Test(expected = RuntimeException.class)
  public void deserialize_multiple_polymorphic_no_default() throws Exception {
    TestUtil.gsonNoDefault(Article.class)
        .getAdapter(Document.class)
        .fromJson(TestUtil.fromResource("/multiple_polymorphic.json"));
  }

  @Test
  public void deserialize_unparameterized() throws Exception {
    Document document = getDocumentAdapter(null, Person.class).fromJson("{\"data\":{\"type\":\"people\",\"id\":\"5\"}}");
    assertThat(document, instanceOf(Document.class));
    assertThat(document.getSingleData().getType(), equalTo("people"));
    assertThat(document.getSingleData(), instanceOf(Person.class));
  }

  @Test
  public void deserialize_object_to_object_typed_document() throws Exception {
    Gson gson = TestUtil.gson(Article.class);
    TypeAdapter<?> adapter = gson.getAdapter(Document.class);
    assertThat(adapter, instanceOf(DocumentAdapter.class));
    Document<Article> singleDocument = ((Document<Article>) adapter.fromJson(TestUtil.fromResource("/single.json")));
    assertThat(singleDocument, instanceOf(Document.class));
    assertOnArticle1(singleDocument.getSingleData());
  }

  @Test
  public void deserialize_array_to_array_typed_document() throws Exception {
    Gson gson = TestUtil.gson(Article.class, Person.class, Comment.class);
    TypeAdapter<?> adapter = gson.getAdapter(Document.class);
    assertThat(adapter, instanceOf(DocumentAdapter.class));
    Document<Article> arrayDocument = ((Document<Article>) adapter.fromJson(TestUtil.fromResource("/multiple_compound.json")));
    assertThat(arrayDocument.getListData().size(), equalTo(1));
    assertOnArticle1(arrayDocument.getListData().get(0));
  }

  @Test
  public void serialize_null() {
    Document document = new Document();
    assertThat(getDocumentAdapter(ResourceIdentifier.class).toJson(document), equalTo("{}"));
    document.setNull(true);
    assertThat(getDocumentAdapter(ResourceIdentifier.class).toJson(document), equalTo("{\"data\":null}"));
  }

  @Test
  public void serialize_empty() throws Exception {
    Document document = new Document();
    document.setArrayDocument(true);
    assertThat(getDocumentAdapter(ResourceIdentifier.class).toJson(document), equalTo("{\"data\":[]}"));
  }

  @Test
  public void serialize_object() throws Exception {
    Article article = new Article();
    article.setTitle("Nineteen Eighty-Four");
    article.setAuthor(new One<Person>("people", "5"));
    article.setComments(new Many<Comment>(
        new ResourceIdentifier("comments", "1")));
    Document document = new Document();
    document.setSingleData(article);
    assertThat(getDocumentAdapter(Article.class).toJson(document), equalTo(
        "{\"data\":{\"type\":\"articles\",\"attributes\":{\"title\":\"Nineteen Eighty-Four\"},\"relationships\":{\"author\":{\"data\":{\"type\":\"people\",\"id\":\"5\"}},\"comments\":{\"data\":[{\"type\":\"comments\",\"id\":\"1\"}]}}}}"));
  }

  @Test
  public void serialize_polymorphic() throws Exception {
    Article article = new Article();
    article.setTitle("Nineteen Eighty-Four");
    Document document = new Document();
    document.setSingleData(article);
    assertThat(getDocumentAdapter(Resource.class).toJson(document),
        equalTo("{\"data\":{\"type\":\"articles\",\"attributes\":{\"title\":\"Nineteen Eighty-Four\"}}}"));
  }

  @Test
  public void serialize_multiple_polymorphic_compound() throws Exception {
    Document document = new Document();
    Comment comment1 = new Comment();
    comment1.setId("1");
    comment1.setBody("Awesome!");
    Person author = new Person();
    author.setId("5");
    author.setFirstName("George");
    author.setLastName("Orwell");
    Article article = new Article();
    article.setTitle("Nineteen Eighty-Four");
    article.setAuthor(new One<Person>(author));
    article.setComments(new Many<Comment>(comment1));
    List<ResourceIdentifier> list = new ArrayList<>();
    list.add(article);
    list.add(author);
    document.setListData(list);
    document.include(comment1);
    assertThat(getDocumentAdapter(Resource.class).toJson(document),
        equalTo("{\"data\":[" +
            "{\"type\":\"articles\",\"attributes\":{\"title\":\"Nineteen Eighty-Four\"},\"relationships\":{\"author\":{\"data\":{\"type\":\"people\",\"id\":\"5\"}},\"comments\":{\"data\":[{\"type\":\"comments\",\"id\":\"1\"}]}}}," +
            "{\"type\":\"people\",\"id\":\"5\",\"attributes\":{\"first-name\":\"George\",\"last-name\":\"Orwell\"}}" +
            "],\"included\":[{\"type\":\"comments\",\"id\":\"1\",\"attributes\":{\"body\":\"Awesome!\"}}]}"));
  }

  @Test
  public void deserialize_resource_identifier() throws Exception {
    Document document = getDocumentAdapter(ResourceIdentifier.class)
        .fromJson(TestUtil.fromResource("/relationship_single.json"));
    assertThat(document.getSingleData(), instanceOf(ResourceIdentifier.class));
    assertThat(document.getSingleData().getId(), equalTo("12"));
    assertTrue(getDocumentAdapter(ResourceIdentifier.class)
        .fromJson(TestUtil.fromResource("/relationship_single_null.json")).isNull());
  }

  @Test
  public void deserialize_multiple_resource_identifiers() throws Exception {
    Gson gson = TestUtil.gson(Tags.class);
    Document<Tags> document = gson.getAdapter(Document.class)
        .fromJson(TestUtil.fromResource("/relationship_multi.json"));
    assertThat(document.getListData().size(), equalTo(2));
    assertThat(document.getListData().get(0), instanceOf(ResourceIdentifier.class));
    assertThat(document.getListData().get(1).getType(), equalTo("tags"));
    assertThat(gson.getAdapter(Document.class)
        .fromJson(TestUtil.fromResource("/relationship_multi_empty.json")), instanceOf(Document.class));
  }

  @Test
  public void serialize_resource_identifier() throws Exception {
    Document document = new Document();
    document.setSingleData(new ResourceIdentifier("people", "5"));
    TypeToken<Document<ResourceIdentifier>> tt = new TypeToken<Document<ResourceIdentifier>>() {};
    Gson gson = TestUtil.gson(Person.class);
    assertThat(gson.getAdapter(tt).toJson(document),
        equalTo("{\"data\":{\"type\":\"people\",\"id\":\"5\"}}"));
  }

  @Test
  public void serialize_multiple_resource_identifiers() throws Exception {
    Document document = new Document();
    List<ResourceIdentifier> list = new ArrayList<>();
    list.add(new ResourceIdentifier("people", "5"));
    list.add(new ResourceIdentifier("people", "11"));
    document.setListData(list);
    TypeToken<Document<ResourceIdentifier>> tt = new TypeToken<Document<ResourceIdentifier>>() {};
    Gson gson = TestUtil.gson(Person.class);
    assertThat(gson.getAdapter(tt).toJson(document),
        equalTo("{\"data\":[{\"type\":\"people\",\"id\":\"5\"},{\"type\":\"people\",\"id\":\"11\"}]}"));
  }

  @Test
  public void serialize_errors() throws Exception {
    Error error = new Error();
    error.setId("4");
    error.setStatus("502");
    error.setTitle("Internal error");
    error.setCode("502000");
    error.setDetail("Ouch! There's some trouble with our server.");
    Document document = new Document();
    document.errors(Collections.singletonList(error));
    assertThat(getDocumentAdapter(null).toJson(document),
        equalTo("{\"error\":[{\"id\":\"4\",\"status\":\"502\",\"code\":\"502000\",\"title\":\"Internal error\",\"detail\":\"Ouch! There's some trouble with our server.\"}]}"));
  }

  @Test
  public void deserialize_errors() throws Exception {
    Document document1 = getDocumentAdapter(null).fromJson(TestUtil.fromResource("/errors.json"));
    assertTrue(document1.hasError());
    assertEquals(document1.errors().size(), 2);
    Document document2 = getDocumentAdapter(null).fromJson(TestUtil.fromResource("/errors_empty.json"));
    assertFalse(document2.hasError());
  }

  @Test
  public void deserialize_meta() throws Exception {
    Document document = TestUtil.gson().getAdapter(Document.class).fromJson(TestUtil.fromResource("/meta.json"));

    JsonObject meta = new JsonObject();
    meta.addProperty("copyright", "Copyright 20XX Example Corp.");

    JsonArray metaList = new JsonArray();
    metaList.add("Yehuda Katz");
    metaList.add("Steve Klabnik");
    metaList.add("Dan Gebhardt");
    metaList.add("Tyler Kellen");

    meta.add("authors", metaList);

    assertEquals(document.getMeta(), meta);
  }

  @Test
  public void equality() throws Exception {
    Gson gson = TestUtil.gson(Article.class, Person.class, Comment.class);
    Document<Article> document1 = gson.getAdapter(Document.class).fromJson(TestUtil.fromResource("/multiple_compound.json"));
    Document<Resource> document2 = gson.getAdapter(Document.class).fromJson(TestUtil.fromResource("/multiple_compound.json"));
    assertEquals(document1, document2);
    assertEquals(document1.hashCode(), document2.hashCode());
  }

  @Type("articles")
  private static class Article2 extends Article {

  }

  @Type("tags")
  private static class Tags extends Resource {

  }

  public <T extends ResourceIdentifier> TypeAdapter<Document> getDocumentAdapter(Class<T> typeParameter, Class<? extends Resource>... knownTypes) {
    Gson gson;
    if (typeParameter == null) {
      return (TypeAdapter) TestUtil.gson(knownTypes).getAdapter(Document.class);
    } else {
      gson = TestUtil.gson(knownTypes);
    }
    return gson.getAdapter(Document.class);
  }

  private void assertOnArticle1(Article article) {
    assertThat(article.getId(), equalTo("1"));
    assertThat(article.getType(), equalTo("articles"));
    assertThat(article.getTitle(), equalTo("JSON API paints my bikeshed!"));
    assertThat(article.getAuthor().get(), equalTo(
        new ResourceIdentifier("people", "9")));
    assertThat(article.getComments().get(), hasItems(
        new ResourceIdentifier("comments", "5"),
        new ResourceIdentifier("comments", "12")));
  }

}
