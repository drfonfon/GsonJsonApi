package com.fonfon.gsonjsonapi.adapters;

import com.fonfon.gsonjsonapi.models.ResourceIdentifier;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static com.fonfon.gsonjsonapi.Constants.ID;
import static com.fonfon.gsonjsonapi.Constants.META;
import static com.fonfon.gsonjsonapi.Constants.TYPE;

public class ResourceIdentiferAdapter extends GsonAdapter<ResourceIdentifier> {

  public ResourceIdentiferAdapter(Gson gson) {
    super(gson);
  }

  @Override
  public void write(JsonWriter writer, ResourceIdentifier value) throws IOException {
    super.write(writer, value);
    writer.beginObject();
    writer.name(TYPE).value(value.getType());
    writer.name(ID).value(value.getId());
    writeJsonElement(writer, value.getMeta(), META);
    writer.endObject();
  }

  @Override
  public ResourceIdentifier read(JsonReader reader) throws IOException {
    ResourceIdentifier object = new ResourceIdentifier();
    reader.beginObject();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case ID:
          object.setId(nextNullableString(reader));
          break;
        case TYPE:
          object.setType(nextNullableString(reader));
          break;
        case META:
          object.setMeta(readJsonElement(reader));
          break;
        default:
          reader.skipValue();
          break;
      }
    }
    reader.endObject();
    return object;
  }
}
