package com.fonfon.gsonjsonapi.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;

import static com.fonfon.gsonjsonapi.adapters.GsonAdapter.nextNullableObject;

public class FieldAdapter<T> {

  private final Field field;
  final int fieldType;
  final TypeAdapter<T> adapter;

  FieldAdapter(Field field, int fieldType, TypeAdapter<T> adapter) {
    this.field = field;
    this.fieldType = fieldType;
    this.adapter = adapter;
  }

  void set(Object target, T value) {
    try {
      field.set(target, value);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  T get(Object value) {
    try {
      return (T) field.get(value);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  void readFrom(JsonReader reader, T value) throws IOException {
    set(value, nextNullableObject(reader, adapter));
  }

  void writeTo(JsonWriter writer, Object object) throws IOException {
    writer.setSerializeNulls(false);
    writeNullableValue(writer, adapter, get(object));
  }

  public static <T> void writeNullableValue(JsonWriter writer, TypeAdapter<T> adapter, T value) throws IOException {
    if (value != null) adapter.write(writer, value);
    else writer.nullValue();
  }

}
