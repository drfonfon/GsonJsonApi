package com.fonfon.gsonjsonapi.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public abstract class GsonAdapter<T> extends TypeAdapter<T> {

  protected Gson gson;
  private JsonParser parser = new JsonParser();

  public GsonAdapter(Gson gson) {
    this.gson = gson;
  }

  @Override
  public void write(JsonWriter out, T value) throws IOException {
    out.setSerializeNulls(false);
  }

  @Override
  public T read(JsonReader in) throws IOException {
    return null;
  }

  public String nextNullableString(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.skipValue();
      return null;
    } else {
      return reader.nextString();
    }
  }

  public static <T> T nextNullableObject(JsonReader reader, TypeAdapter<T> adapter) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.skipValue();
      return null;
    } else {
      return adapter.read(reader);
    }
  }

  public void writeJsonElement(JsonWriter writer, JsonElement element, String name) throws IOException {
    if (element != null) {
      writer.name(name).jsonValue(gson.toJson(element));
    }
  }

  public JsonElement readJsonElement(JsonReader reader) throws IOException {
    return parser.parse(reader);
  }

}
