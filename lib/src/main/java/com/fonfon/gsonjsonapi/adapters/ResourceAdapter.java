package com.fonfon.gsonjsonapi.adapters;

import com.fonfon.gsonjsonapi.models.Resource;
import com.fonfon.gsonjsonapi.models.relationship.Relationship;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.fonfon.gsonjsonapi.Constants.ATTRIBUTES;
import static com.fonfon.gsonjsonapi.Constants.ID;
import static com.fonfon.gsonjsonapi.Constants.LINKS;
import static com.fonfon.gsonjsonapi.Constants.META;
import static com.fonfon.gsonjsonapi.Constants.RELATIONSHIPS;
import static com.fonfon.gsonjsonapi.Constants.TYPE;

public class ResourceAdapter<T extends Resource> extends GsonAdapter<T> {

  private final Constructor<T> constructor;

  private static final int TYPE_ATTRIBUTE = 0x01;
  private static final int TYPE_RELATIONSHIP = 0x03;

  private final Map<String, FieldAdapter> bindings = new LinkedHashMap<>();

  public ResourceAdapter(Gson gson, Class<T> type) {
    super(gson);

    try {
      constructor = type.getDeclaredConstructor();
      constructor.setAccessible(true);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("No default constructor on [" + type + "]", e);
    }

    for (Field field : listFields(type, Resource.class)) {
      int modifiers = field.getModifiers();
      if (Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers)) {
        // skip transient fields and static fields
        continue;
      }
      if (!Modifier.isPublic(modifiers)) {
        // make private fields accessible
        field.setAccessible(true);
      }
      String name = field.getName();
      SerializedName json = field.getAnnotation(SerializedName.class);
      if (json != null) {
        name = json.value();
      }
      if (bindings.containsKey(name)) {
        throw new IllegalArgumentException("Duplicated field '" + name + "' in [" + type + "].");
      }
      int tp = Relationship.class.isAssignableFrom(getRawType(field.getGenericType())) ? TYPE_RELATIONSHIP : TYPE_ATTRIBUTE;
      TypeAdapter<?> adapter = gson.getAdapter(field.getType());
      bindings.put(name, new FieldAdapter<>(field, tp, adapter));
    }
  }


  private Class<?> getRawType(Type type) {
    if (type instanceof Class<?>) {
      return (Class<?>) type;
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type rawType = parameterizedType.getRawType();
      return (Class<?>) rawType;
    } else if (type instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      return Array.newInstance(getRawType(componentType), 0).getClass();
    } else if (type instanceof TypeVariable) {
      return Object.class;
    } else if (type instanceof WildcardType) {
      return getRawType(((WildcardType) type).getUpperBounds()[0]);
    } else {
      String className = type == null ? "null" : type.getClass().getName();
      throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
          + "GenericArrayType, but <" + type + "> is of type " + className);
    }
  }

  private void readFields(JsonReader reader, Object resource) throws IOException {
    reader.beginObject();
    while (reader.hasNext()) {
      FieldAdapter fieldAdapter = bindings.get(reader.nextName());
      if (fieldAdapter != null) {
        fieldAdapter.readFrom(reader, resource);
      } else {
        reader.skipValue();
      }
    }
    reader.endObject();
  }

  private void writeFields(JsonWriter writer, int fieldType, String name, Object value) throws IOException {
    boolean skipFlag = true;
    for (Map.Entry<String, FieldAdapter> entry : bindings.entrySet()) {
      FieldAdapter<?> adapter = entry.getValue();
      if (adapter.fieldType != fieldType) {
        continue;
      }
      if (adapter.get((Object) value) == null && !writer.getSerializeNulls()) {
        // skip write of null values
        continue;
      }
      if (skipFlag) {
        writer.name(name).beginObject();
        skipFlag = false;
      }
      writer.name(entry.getKey());
      adapter.writeTo(writer, value);
    }
    if (!skipFlag) {
      writer.endObject();
    }
  }

  private List<Field> listFields(Class<?> type, Class<?> baseType) {
    List<Field> fields = new ArrayList<>();
    Class<?> clazz = type;
    while (clazz != baseType) {
      Collections.addAll(fields, clazz.getDeclaredFields());
      clazz = clazz.getSuperclass();
    }
    return fields;
  }

  @Override
  public void write(JsonWriter writer, T value) throws IOException {
    super.write(writer, value);
    writer.beginObject();
    writer.name(TYPE).value(value.getType());
    writer.name(ID).value(value.getId());
    writeFields(writer, TYPE_ATTRIBUTE, ATTRIBUTES, value);
    writeFields(writer, TYPE_RELATIONSHIP, RELATIONSHIPS, value);
    writeJsonElement(writer, value.getMeta(), META);
    writeJsonElement(writer, value.getLinks(), LINKS);
    writer.endObject();
  }

  @Override
  public T read(JsonReader reader) throws IOException {
    T resource;
    try {
      resource = constructor.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    reader.beginObject();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case ID:
          resource.setId(nextNullableString(reader));
          break;
        case TYPE:
          resource.setType(nextNullableString(reader));
          break;
        case ATTRIBUTES:
        case RELATIONSHIPS:
          readFields(reader, resource);
          break;
        case META:
          resource.setMeta(readJsonElement(reader));
          break;
        case LINKS:
          resource.setLinks(readJsonElement(reader));
          break;
        default:
          reader.skipValue();
          break;
      }
    }
    reader.endObject();
    return resource;
  }
}
