package com.fonfon.gsonjsonapi;

import com.fonfon.gsonjsonapi.adapters.DocumentAdapter;
import com.fonfon.gsonjsonapi.adapters.ErrorAdapter;
import com.fonfon.gsonjsonapi.adapters.GenericAdapter;
import com.fonfon.gsonjsonapi.adapters.JsonApiAdapter;
import com.fonfon.gsonjsonapi.adapters.ManyAdapter;
import com.fonfon.gsonjsonapi.adapters.OneAdapter;
import com.fonfon.gsonjsonapi.adapters.ResourceAdapter;
import com.fonfon.gsonjsonapi.adapters.ResourceIdentiferAdapter;
import com.fonfon.gsonjsonapi.models.Document;
import com.fonfon.gsonjsonapi.models.Error;
import com.fonfon.gsonjsonapi.models.JsonApi;
import com.fonfon.gsonjsonapi.models.Resource;
import com.fonfon.gsonjsonapi.models.ResourceIdentifier;
import com.fonfon.gsonjsonapi.models.relationship.Many;
import com.fonfon.gsonjsonapi.models.relationship.One;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public final class ResourceAdapterFactory implements TypeAdapterFactory {

  private Map<String, Class<?>> typeMap = new HashMap<>();

  @SafeVarargs
  public ResourceAdapterFactory(Class<? extends Resource>... types) {
    for (Class<? extends Resource> type : types) {
      String typeName = type.getAnnotation(Type.class).value();
      if (typeMap.containsKey(typeName)) {
        throw new IllegalArgumentException(
            "@Type(\"" + typeName + "\") declaration of [" + type.getCanonicalName() + "] conflicts with [" + typeMap.get(typeName).getCanonicalName() + "]." );
      }
      typeMap.put(typeName, type);
    }
  }

  public ResourceAdapterFactory addType(Class<? extends Resource> type) {
    typeMap.put(type.getAnnotation(Type.class).value(), type);
    return this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {
    Class rawType = type.getRawType();
    if (rawType.equals(Many.class)) return new ManyAdapter(gson);
    if (rawType.equals(One.class)) return new OneAdapter(gson);
    if (rawType.equals(JsonApi.class)) return (TypeAdapter<T>) new JsonApiAdapter(gson);
    if (rawType.equals(Error.class)) return (TypeAdapter<T>) new ErrorAdapter(gson);
    if (rawType.equals(ResourceIdentifier.class))
      return (TypeAdapter<T>) new ResourceIdentiferAdapter(gson);
    if (rawType.equals(Resource.class)) return (TypeAdapter<T>) new GenericAdapter(gson, typeMap);

    if (Document.class.isAssignableFrom(rawType)) {
      if (type.getType() instanceof ParameterizedType) {
        java.lang.reflect.Type typeParameter = ((ParameterizedType) type.getType()).getActualTypeArguments()[0];
        if (typeParameter instanceof Class) {
          return new DocumentAdapter(gson, (Class) typeParameter);
        }
      }
      return (TypeAdapter<T>) new DocumentAdapter<>(gson, Resource.class);
    }
    if (Resource.class.isAssignableFrom(rawType)) return new ResourceAdapter(gson, rawType);
    return null;
  }
}
