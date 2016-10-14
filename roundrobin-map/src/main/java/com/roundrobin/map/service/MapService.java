package com.roundrobin.map.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import com.roundrobin.map.api.MapTo;
import com.roundrobin.map.domain.Map;
import com.roundrobin.map.domain.Vendor;
import com.roundrobin.map.repository.MapRepository;
import com.roundrobin.map.repository.VendorRepository;

@Service
public class MapService {

  @Autowired
  private MapRepository mapRepo;

  @Autowired
  private VendorRepository vendorRepo;

  public List<MapTo> findVendorsByPoint(MapTo mapTo) {
    Distance distance = new Distance(mapTo.getRadius(), Metrics.KILOMETERS);
    return mapRepo.findByPointWithin(new Circle(mapTo.getLocation(), distance)).stream()
        .map(v -> new MapTo(v.getVendor().getId(), 0)).collect(Collectors.toList());
    // return mapRepo.findByPointWithinAndVendorSkillsIn(new Circle(mapTo.getLocation(), distance),
    // mapTo.getSkillId()).stream()
    // .map(v -> new MapTo(v.getVendor().getId(), 0)).collect(Collectors.toList());
  }

  public List<MapTo> findVendorsByLocation(MapTo mapTo) {
    Distance distance = new Distance(mapTo.getRadius(), Metrics.KILOMETERS);
    return vendorRepo.findByLocationWithinAndSkillsIn(new Circle(mapTo.getLocation(), distance), mapTo.getSkillId())
        .stream().map(v -> new MapTo(v.getId(), 0)).collect(Collectors.toList());
  }

  public Map create(Vendor vendor) {
    Map map = new Map();
    map.setVendor(vendor);
    map.setPoint(vendor.getLocation());
    return mapRepo.save(map);
  }

  public Map update(Vendor vendor, GeoJsonPoint point) {
    Map map = get(vendor);
    map.setPoint(point);
    return mapRepo.save(map);
  }

  public Map get(Vendor vendor) {
    Optional<Map> map = mapRepo.findByVendor(vendor);
    if (map.isPresent()) {
      return map.get();
    }
    return create(vendor);
  }

}
