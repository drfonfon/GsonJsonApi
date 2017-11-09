package com.fonfon.gsonjsonapi.adapters;

import com.fonfon.gsonjsonapi.models.Resource;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public class GenericAdapter<T extends Resource> extends GsonAdapter<T> {

  private Map<String, Class<?>> typeMap;

  public GenericAdapter(Gson gson, Map<String, Class<?>> typeMap) {
    super(gson);
    this.typeMap = typeMap;
  }

  private static String findTypeOf(JsonReader reader) throws NoSuchFieldException, IllegalAccessException {
    Field bufField = JsonReader.class.getDeclaredField("buffer");
    Field posField = JsonReader.class.getDeclaredField("pos");

    bufField.setAccessible(true);
    posField.setAccessible(true);

    String buff = new String((char[]) bufField.get(reader));
    int pos = posField.getInt(reader);

    bufField.setAccessible(false);
    posField.setAccessible(false);

    String s = (buff.substring(buff.indexOf("type", pos))).replace("type\"", "");
    int firstIndex = s.indexOf("\"");
    return s.substring(firstIndex + 1, s.indexOf("\"", firstIndex + 1));
  }

  @Override
  @SuppressWarnings("unchecked")
  public void write(JsonWriter writer, T value) throws IOException {
    super.write(writer, value);
    gson.getAdapter((Class<T>) value.getClass()).write(writer, value);
  }


  @Override
  public T read(JsonReader reader) throws IOException {
    String typeName = null;
    try {
      typeName = findTypeOf(reader);
    } catch (NoSuchFieldException e ) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    if (typeMap.containsKey(typeName)) {
      return (T) gson.getAdapter(typeMap.get(typeName)).read(reader);
    } else if (typeMap.containsKey("default")) {
      return (T) gson.getAdapter(typeMap.get("default")).read(reader);
    } else {
      throw new RuntimeException("Unknown type of resource: " + typeName);
    }
  }
}
