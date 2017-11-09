package com.fonfon.gsonjsonapi.adapters;

import com.fonfon.gsonjsonapi.models.JsonApi;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static com.fonfon.gsonjsonapi.Constants.META;
import static com.fonfon.gsonjsonapi.Constants.VERSION;

public class JsonApiAdapter extends GsonAdapter<JsonApi> {

  public JsonApiAdapter(Gson gson) {
    super(gson);
  }

  @Override
  public void write(JsonWriter writer, JsonApi value) throws IOException {
    super.write(writer, value);
    writer.name(VERSION).value(value.getVersion());
    writeJsonElement(writer, value.getMeta(), META);
  }

  @Override
  public JsonApi read(JsonReader reader) throws IOException {
    JsonApi jsonApi = new JsonApi();
    reader.beginObject();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case VERSION:
          jsonApi.setVersion(nextNullableString(reader));
          break;
        case META:
          jsonApi.setMeta(readJsonElement(reader));
          break;
        default:
          reader.skipValue();
          break;
      }
    }
    reader.endObject();
    return jsonApi;
  }
}
