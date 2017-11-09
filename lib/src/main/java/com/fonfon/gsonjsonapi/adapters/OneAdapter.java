package com.fonfon.gsonjsonapi.adapters;

import com.fonfon.gsonjsonapi.models.Resource;
import com.fonfon.gsonjsonapi.models.ResourceIdentifier;
import com.fonfon.gsonjsonapi.models.relationship.One;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static com.fonfon.gsonjsonapi.Constants.DATA;
import static com.fonfon.gsonjsonapi.Constants.LINKS;
import static com.fonfon.gsonjsonapi.Constants.META;

public class OneAdapter<T extends Resource> extends GsonAdapter<One<T>> {

  private TypeAdapter<ResourceIdentifier> resourceIdentifierJsonAdapter;

  public OneAdapter(Gson gson) {
    super(gson);
    resourceIdentifierJsonAdapter = gson.getAdapter(ResourceIdentifier.class);
  }

  @Override
  public void write(JsonWriter writer, One<T> value) throws IOException {
    super.write(writer, value);
    writer.beginObject();
    if (value.get() != null) {
      resourceIdentifierJsonAdapter.write(writer.name(DATA), value.get());
    }
    writeJsonElement(writer, value.getMeta(), META);
    writeJsonElement(writer, value.getLinks(), LINKS);
    writer.endObject();
  }

  @Override
  public One<T> read(JsonReader reader) throws IOException {
    One<T> relationship = new One<>();
    reader.beginObject();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case DATA:
          relationship.set(nextNullableObject(reader, resourceIdentifierJsonAdapter));
          break;
        case META:
          relationship.setMeta(readJsonElement(reader));
          break;
        case LINKS:
          relationship.setLinks(readJsonElement(reader));
          break;
        default:
          reader.skipValue();
          break;
      }
    }
    reader.endObject();
    return relationship;
  }
}
