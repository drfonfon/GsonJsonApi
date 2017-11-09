package com.fonfon.gsonjsonapi;

import com.fonfon.gsonjsonapi.models.Resource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import okio.Buffer;

public class TestUtil {

  @Type("default")
  public static class Default extends Resource {

  }

  @SafeVarargs
  public static Gson gson(Class<? extends Resource>... types) {
    ResourceAdapterFactory factory = new ResourceAdapterFactory(types);
    factory.addType(Default.class);
    return new GsonBuilder().registerTypeAdapterFactory(factory).create();
  }

  @SafeVarargs
  public static Gson gsonNoDefault(Class<? extends Resource>... types) {
    ResourceAdapterFactory factory = new ResourceAdapterFactory(types);
    return new GsonBuilder().registerTypeAdapterFactory(factory).create();
  }

  public static String fromResource(String resourceName) throws IOException {
    InputStream in = TestUtil.class.getResourceAsStream(resourceName);
    if (in == null) {
      throw new FileNotFoundException(resourceName);
    }
    Buffer buffer = new Buffer();
    buffer.readFrom(in);
    return buffer.readString(Charset.forName("UTF-8"));
  }

}
