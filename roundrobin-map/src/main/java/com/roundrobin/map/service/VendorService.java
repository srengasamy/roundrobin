package com.roundrobin.map.service;

import static com.roundrobin.conditions.Preconditions.checkArgument;
import static com.roundrobin.map.error.MapErrorCode.INVALID_VENDOR_ID;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.exception.BadRequestException;
import com.roundrobin.map.api.VendorTo;
import com.roundrobin.map.domain.Vendor;
import com.roundrobin.map.repository.VendorRepository;

@Service
public class VendorService {

  @Autowired
  private VendorRepository vendorRepo;

  public VendorTo create(VendorTo vendorTo) {
    Optional<Vendor> existing = vendorRepo.findById(vendorTo.getVendorId());
    if(existing.isPresent()){
      return update(vendorTo);
    }
    Vendor vendor = new Vendor();
    vendor.setId(vendorTo.getVendorId());
    vendor.setSkills(vendorTo.getSkills());
    vendor.setLocation(vendorTo.getLocation());
    return convert(vendorRepo.save(vendor));
  }

  public VendorTo update(VendorTo vendorTo) {
    Vendor vendor = get(vendorTo.getVendorId());
    vendor.setLocation(vendorTo.getLocation());
    vendor.setSkills(vendorTo.getSkills());
    return convert(vendorRepo.save(vendor));
  }

  public Vendor get(String vendorId) {
    Optional<Vendor> vendor = vendorRepo.findById(vendorId);
    checkArgument(vendor.isPresent(), new BadRequestException(INVALID_VENDOR_ID, "vendor_id"));
    return vendor.get();
  }

  public VendorTo convert(Vendor vendor) {
    VendorTo vendorTo = new VendorTo();
    vendorTo.setVendorId(vendor.getId());
    return vendorTo;
  }
}
