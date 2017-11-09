package com.fonfon.gsonjsonapi.adapters;

import com.fonfon.gsonjsonapi.models.Document;
import com.fonfon.gsonjsonapi.models.Error;
import com.fonfon.gsonjsonapi.models.JsonApi;
import com.fonfon.gsonjsonapi.models.Resource;
import com.fonfon.gsonjsonapi.models.ResourceIdentifier;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.fonfon.gsonjsonapi.Constants.DATA;
import static com.fonfon.gsonjsonapi.Constants.ERROR;
import static com.fonfon.gsonjsonapi.Constants.ERRORS;
import static com.fonfon.gsonjsonapi.Constants.INCLUDED;
import static com.fonfon.gsonjsonapi.Constants.JSONAPI;
import static com.fonfon.gsonjsonapi.Constants.LINKS;
import static com.fonfon.gsonjsonapi.Constants.META;

public class DocumentAdapter<DATA extends ResourceIdentifier> extends GsonAdapter<Document<DATA>> {

  private TypeAdapter<Error> errorAdapter;
  private TypeAdapter<DATA> dataAdapter;
  private TypeAdapter<Resource> resourceAdapter;
  private TypeAdapter<JsonApi> jsonApiAdapter;

  public DocumentAdapter(Gson gson, Class<DATA> type) {
    super(gson);
    resourceAdapter = gson.getAdapter(Resource.class);
    errorAdapter = gson.getAdapter(Error.class);
    jsonApiAdapter = gson.getAdapter(JsonApi.class);
    dataAdapter = gson.getAdapter(type);
  }

  @Override
  public void write(JsonWriter writer, Document<DATA> value) throws IOException {
    super.write(writer, value);
    writer.beginObject();
    if (value.isArrayDocument())
      writeArrayDocument(writer, value);
    else
      writeSingleDocument(writer, value);
    writeIncluded(writer, value);
    writeErrors(writer, value);
    writeJsonApi(writer, value);
    writeJsonElement(writer, value.getMeta(), META);
    writeJsonElement(writer, value.getLinks(), LINKS);
    writer.endObject();
  }

  @Override
  public Document<DATA> read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      return null;
    }
    Document<DATA> document = new Document<>();
    reader.beginObject();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case DATA:
          document = readDATA(reader, document);
          break;
        case INCLUDED:
          reader.beginArray();
          while (reader.hasNext())
            document.include(resourceAdapter.read(reader));
          reader.endArray();
          break;
        case ERRORS:
          reader.beginArray();
          List<Error> errors = document.errors();
          while (reader.hasNext())
            errors.add(errorAdapter.read(reader));
          reader.endArray();
          break;
        case META:
          document.setMeta(readJsonElement(reader));
          break;
        case LINKS:
          document.setLinks(readJsonElement(reader));
          break;
        case JSONAPI:
          document.setJsonApi(jsonApiAdapter.read(reader));
          break;
        default:
          reader.skipValue();
          break;
      }
    }
    reader.endObject();
    return document;
  }

  private void writeJsonApi(JsonWriter writer, Document<DATA> value) throws IOException {
    if (value.getJsonApi() != null) {
      writer.name(JSONAPI);
      jsonApiAdapter.write(writer, value.getJsonApi());
    }
  }

  private void writeErrors(JsonWriter writer, Document<DATA> value) throws IOException {
    if (value.getErrors().size() > 0) {
      writer.name(ERROR).beginArray();
      for (Error err : value.getErrors())
        errorAdapter.write(writer, err);
      writer.endArray();
    }
  }

  private void writeIncluded(JsonWriter writer, Document<DATA> value) throws IOException {
    if (value.getIncluded().size() > 0) {
      writer.name(INCLUDED).beginArray();
      for (Resource resource : value.getIncluded().values())
        resourceAdapter.write(writer, resource);
      writer.endArray();
    }
  }

  private void writeSingleDocument(JsonWriter writer, Document<DATA> value) throws IOException {
    writer.name(DATA);
    if (value.isNull()) {
      boolean serializeFlag = writer.getSerializeNulls();
      try {
        writer.setSerializeNulls(true);
        writer.nullValue();
      } finally {
        writer.setSerializeNulls(serializeFlag);
      }
    } else if (value.getSingleData() == null) {
      writer.nullValue();
    } else {
      dataAdapter.write(writer, value.getSingleData());
    }
  }

  private void writeArrayDocument(JsonWriter writer, Document<DATA> value) throws IOException {
    writer.name(DATA);
    writer.beginArray();
    for (DATA resource : value.getListData())
      dataAdapter.write(writer, resource);
    writer.endArray();
  }

  private Document<DATA> readDATA(JsonReader reader, Document<DATA> document) throws IOException {
    switch (reader.peek()) {
      case BEGIN_ARRAY:
        reader.beginArray();
        List<DATA> list = new ArrayList<>();
        while (reader.hasNext())
          list.add(dataAdapter.read(reader));
        document.setListData(list);
        document.setArrayDocument(true);
        reader.endArray();
        break;
      case BEGIN_OBJECT:
        document.setSingleData(dataAdapter.read(reader));
        break;
      case NULL:
        reader.nextNull();
        document.setNull(true);
        break;
      default:
        reader.skipValue();
        break;
    }
    return document;
  }
}
