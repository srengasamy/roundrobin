package com.roundrobin.vault.helper;

import com.roundrobin.core.api.Response;
import com.roundrobin.vault.api.VendorMapTo;
import com.roundrobin.vault.domain.Job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Scope("singleton")
public class MapHelper {
  @Value("${roundrobin.map.url}")
  private String mapUrl;

  @Autowired
  public RestTemplate template;

  public List<VendorMapTo> getNearByVendors(Job job) {
    return template.exchange(mapUrl + "nearby", HttpMethod.POST, new HttpEntity<>(job), new
            ParameterizedTypeReference<Response<List<VendorMapTo>>>() {
            }).getBody().getEntity();
  }

  public List<VendorMapTo> getNearByVendors(GeoJsonPoint location, String skillId, double radius) {
    return template.exchange(mapUrl + "nearby?location={location}&skillId={skillId}&radius={radius}",
            HttpMethod.GET,
            new HttpEntity<>(null),
            new ParameterizedTypeReference<Response<List<VendorMapTo>>>() {
            }, location, skillId, radius).getBody().getEntity();
  }
}
