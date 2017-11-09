package com.fonfon.gsonjsonapi.adapters;

import com.fonfon.gsonjsonapi.models.Resource;
import com.fonfon.gsonjsonapi.models.ResourceIdentifier;
import com.fonfon.gsonjsonapi.models.relationship.Many;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static com.fonfon.gsonjsonapi.Constants.DATA;
import static com.fonfon.gsonjsonapi.Constants.LINKS;
import static com.fonfon.gsonjsonapi.Constants.META;

public class ManyAdapter<T extends Resource> extends GsonAdapter<Many<T>> {

  private TypeAdapter<ResourceIdentifier> resourceIdentifierJsonAdapter;

  public ManyAdapter(Gson gson) {
    super(gson);
    resourceIdentifierJsonAdapter = gson.getAdapter(ResourceIdentifier.class);
  }

  @Override
  public void write(JsonWriter writer, Many<T> value) throws IOException {
    super.write(writer, value);
    writer.beginObject();
    writer.name(DATA);
    writer.beginArray();
    for (ResourceIdentifier resource : value.getLinkedResources())
      resourceIdentifierJsonAdapter.write(writer, resource);
    writer.endArray();
    writeJsonElement(writer, value.getMeta(), META);
    writeJsonElement(writer, value.getLinks(), LINKS);
    writer.endObject();
  }

  @Override
  public Many<T> read(JsonReader reader) throws IOException {
    Many<T> relationship = new Many<>();
    reader.beginObject();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case DATA:
          reader.beginArray();
          while (reader.hasNext())
            relationship.add(resourceIdentifierJsonAdapter.read(reader));
          reader.endArray();
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
