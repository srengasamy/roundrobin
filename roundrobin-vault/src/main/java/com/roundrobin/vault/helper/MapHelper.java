package com.roundrobin.vault.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import com.roundrobin.api.Response;
import com.roundrobin.helper.HttpHelper;
import com.roundrobin.vault.api.VendorMapTo;

@Component
@Scope("singleton")
public class MapHelper {
  @Value("${roundrobin.map.url}")
  private String mapUrl;

  @Autowired
  public HttpHelper httpHelper;

  public List<VendorMapTo> getNearByVendors(Job job) {
    return httpHelper.post(mapUrl + "nearby", job, new ParameterizedTypeReference<Response<List<VendorMapTo>>>() {})
        .getBody().getEntity();
  }

  public List<VendorMapTo> getNearByVendors(GeoJsonPoint location, String skillId, double radius) {
    return httpHelper
        .get(mapUrl + "nearby?location={location}&skillId={skillId}&radius={radius}",
            new ParameterizedTypeReference<Response<List<VendorMapTo>>>() {}, location, skillId, radius)
        .getBody().getEntity();
  }
}
