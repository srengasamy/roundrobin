package com.roundrobin.map.service;

import java.util.ArrayList;
import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.map.api.TrailTo;
import com.roundrobin.map.api.VendorTo;
import com.roundrobin.map.domain.Trail;
import com.roundrobin.map.domain.Vendor;
import com.roundrobin.map.repository.TrailRepository;
import com.roundrobin.map.repository.VendorRepository;

@Service
public class TrailService {
  @Autowired
  private TrailRepository trailRepo;

  @Autowired
  private VendorRepository vendorRepo;

  @Autowired
  private VendorService vendorService;

  @Autowired
  private MapService mapService;

  public TrailTo create(TrailTo trailTo) {
    Trail trail = new Trail();
    Vendor vendor =getVendor(trailTo);
    trail.setPoint(trailTo.getPoint());
    trail.setVendor(vendor);
    trail.setCreated(DateTime.now());
    mapService.update(vendor, trailTo.getPoint());
    return convert(trailRepo.save(trail));
  }

  private Vendor getVendor(TrailTo trailTo) {
    String vendorId = trailTo.getVendorId();
    Optional<Vendor> vendor = vendorRepo.findById(vendorId);
    if (vendor.isPresent()) {
      return vendor.get();
    }
    VendorTo vendorTo = new VendorTo();
    vendorTo.setVendorId(vendorId);
    vendorTo.setLocation(trailTo.getPoint());
    vendorTo.setSkills(new ArrayList<>());
    vendorService.create(vendorTo);
    return vendorService.get(vendorId);
  }

  private TrailTo convert(Trail trail) {
    TrailTo trailTo = new TrailTo();
    trailTo.setTrailId(trail.getId());
    return trailTo;
  }
}
