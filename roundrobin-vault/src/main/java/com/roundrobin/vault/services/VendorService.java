package com.roundrobin.vault.services;

import static com.roundrobin.conditions.Preconditions.checkArgument;
import static com.roundrobin.vault.error.VaultErrorCode.INVALID_URGENT_GROUP;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.exception.BadRequestException;
import com.roundrobin.vault.api.VendorMapTo;
import com.roundrobin.vault.domain.AvailableSlot;
import com.roundrobin.vault.domain.Job;
import com.roundrobin.vault.helper.MapHelper;
import com.roundrobin.vault.repository.BlockedRepository;
import com.roundrobin.vault.repository.JobAvoidedRepository;

@Service
public class VendorService {
  @Autowired
  private MapHelper mapHelper;

  @Autowired
  private AgendaService agendaService;

  @Autowired
  private JobAvoidedRepository avoidedRepo;

  @Autowired
  private BlockedRepository blockedRepo;

  private static final List<Integer> urgentGroups = Arrays.asList(5, 10, 15, 20);


  public Optional<AvailableSlot> getUrgentVendor(Job job) {
    List<VendorMapTo> vendors = mapHelper.getNearByVendors(job.getLocation(), job.getSkillDetail().getId(),
        urgentGroups.get(urgentGroups.size() - 1));
    vendors = filterVendors(job, vendors);
    Random randomizer = new Random();
    Map<Integer, List<VendorMapTo>> nearestVendors =
        vendors.stream().collect(Collectors.groupingBy(v -> getUrgentGroup(v)));
    for (Integer distance : nearestVendors.keySet()) {
      List<AvailableSlot> slots =
          nearestVendors.get(distance).stream().map(v -> agendaService.getAvailableNow(v, job.getSkillDetail().getId()))
              .filter(v -> v.isPresent()).map(a -> a.get()).collect(Collectors.toList());
      if (slots.isEmpty()) {
        continue;
      }
      return Optional.of(slots.get(randomizer.nextInt(slots.size())));
    }
    return Optional.empty();
  }

  private Integer getUrgentGroup(VendorMapTo vendorMapTo) {
    Optional<Integer> band = urgentGroups.stream().filter(b -> b.intValue() >= vendorMapTo.getMinutes()).findFirst();
    checkArgument(band.isPresent(), new BadRequestException(INVALID_URGENT_GROUP));
    return band.get();
  }

  public List<VendorMapTo> filterVendors(Job job, List<VendorMapTo> vendors) {
    return filterAvoidedVendors(job, filterBlockedVendors(job, vendors));
  }

  private List<VendorMapTo> filterBlockedVendors(Job job, List<VendorMapTo> vendors) {
    List<String> vendorIds = vendors.stream().map(v -> v.getVendorId()).collect(Collectors.toList());
    Set<String> blockedVendors = blockedRepo.findAllByVendorIds(job.getUserProfile().getId(), vendorIds).stream()
        .map(b -> b.getVendor().getId()).collect(Collectors.toSet());
    return vendors.stream().filter(v -> blockedVendors.contains(v.getVendorId())).collect(Collectors.toList());
  }

  private List<VendorMapTo> filterAvoidedVendors(Job job, List<VendorMapTo> vendors) {
    List<String> vendorIds = vendors.stream().map(v -> v.getVendorId()).collect(Collectors.toList());
    Set<String> avoidedVendors = avoidedRepo.findAllByJobAndVendorIds(job, vendorIds).stream()
        .map(a -> a.getVendor().getId()).collect(Collectors.toSet());
    return vendors.stream().filter(v -> !avoidedVendors.contains(v.getVendorId())).collect(Collectors.toList());
  }
  /**
   * Urgent logic.
   * 
   * 1. Find nearest vendors(5 miles) 2. Filter the blocked vendors 3. Filter the cancelled vendors
   * 3. Group them by bands 4. Iterate through agenda of smaller group 5. Find all vendors who's
   * schedule is free 6. Select a vendor randomly 7. Create job with start time as how long it will
   * take to reach the destination from their destination
   * 
   */
}
