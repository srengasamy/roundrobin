package com.roundrobin.map.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.map.api.MapTo;
import com.roundrobin.map.api.VendorTo;
import com.roundrobin.map.groups.MapValidator;
import com.roundrobin.map.groups.VendorValidator;
import com.roundrobin.map.service.MapService;
import com.roundrobin.map.service.VendorService;

@RestController
@RequestMapping(value = "admin", produces = {"application/json"})
public class AdminResource {
  @Autowired
  private VendorService vendorService;
  @Autowired
  private MapService service;

  @RequestMapping(value = "vendor", method = RequestMethod.POST)
  public Response<VendorTo> create(@RequestBody @Validated(VendorValidator.class) VendorTo vendorTo) {
    return new Response<>(vendorService.create(vendorTo));
  }

  @RequestMapping(value = "vendor", method = RequestMethod.PUT)
  public Response<VendorTo> update(@RequestBody @Validated(VendorValidator.class) VendorTo vendorTo) {
    return new Response<>(vendorService.update(vendorTo));
  }

  @RequestMapping(value = "map/query/point", method = RequestMethod.POST)
  public Response<List<MapTo>> queryByPoint(@RequestBody @Validated(MapValidator.class) MapTo mapTo,
      Authentication authentication) {
    return new Response<>(service.findVendorsByPoint(mapTo));
  }

  @RequestMapping(value = "map/query/location", method = RequestMethod.POST)
  public Response<List<MapTo>> queryByLocation(@RequestBody @Validated(MapValidator.class) MapTo mapTo,
      Authentication authentication) {
    return new Response<>(service.findVendorsByLocation(mapTo));
  }
}
