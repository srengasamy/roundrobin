package com.roundrobin.geo;

import java.io.IOException;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class GeoJsonDeserializer extends JsonDeserializer<GeoJsonPoint> {

  @Override
  public GeoJsonPoint deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    final JsonNode tree = jp.getCodec().readTree(jp);
    Double x = tree.get("x").asDouble();
    Double y = tree.get("y").asDouble();
    if(x == null || y == null){
      throw new HttpMessageNotReadableException("Unable to parse GeoJsonPoint");
    }
    return new GeoJsonPoint(x, y);
  }

}
