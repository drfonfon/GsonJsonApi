package com.fonfon.gsonjsonapi.adapters;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.fonfon.gsonjsonapi.models.Error;

import java.io.IOException;

import static com.fonfon.gsonjsonapi.Constants.CODE;
import static com.fonfon.gsonjsonapi.Constants.DETAIL;
import static com.fonfon.gsonjsonapi.Constants.ID;
import static com.fonfon.gsonjsonapi.Constants.LINKS;
import static com.fonfon.gsonjsonapi.Constants.META;
import static com.fonfon.gsonjsonapi.Constants.SOURCE;
import static com.fonfon.gsonjsonapi.Constants.STATUS;
import static com.fonfon.gsonjsonapi.Constants.TITLE;

public class ErrorAdapter extends GsonAdapter<Error> {

  public ErrorAdapter(Gson gson) {
    super(gson);
  }

  @Override
  public void write(JsonWriter writer, Error value) throws IOException {
    super.write(writer, value);
    writer.beginObject();
    writer.name(ID).value(value.getId());
    writer.name(STATUS).value(value.getStatus());
    writer.name(CODE).value(value.getCode());
    writer.name(TITLE).value(value.getTitle());
    writer.name(DETAIL).value(value.getDetail());
    writeJsonElement(writer, value.getMeta(), META);
    writeJsonElement(writer, value.getLinks(), LINKS);
    writeJsonElement(writer, value.getSource(), SOURCE);
    writer.endObject();
  }

  @Override
  public Error read(JsonReader reader) throws IOException {
    Error err = new Error();
    reader.beginObject();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case ID:
          err.setId(nextNullableString(reader));
          break;
        case STATUS:
          err.setStatus(nextNullableString(reader));
          break;
        case CODE:
          err.setCode(nextNullableString(reader));
          break;
        case TITLE:
          err.setTitle(nextNullableString(reader));
          break;
        case DETAIL:
          err.setDetail(nextNullableString(reader));
          break;
        case SOURCE:
          err.setSource(readJsonElement(reader));
          break;
        case META:
          err.setMeta(readJsonElement(reader));
          break;
        case LINKS:
          err.setLinks(readJsonElement(reader));
          break;
        default:
          reader.skipValue();
          break;
      }
    }
    reader.endObject();
    return err;
  }
}

