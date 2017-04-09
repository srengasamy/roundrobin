package com.roundrobin.vault.service;

import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.roundrobin.vault.api.JobTo;
import com.roundrobin.vault.domain.AvailableSlot;
import com.roundrobin.vault.domain.Job;

import com.roundrobin.vault.domain.JobTimeline;
import com.roundrobin.vault.enums.JobStatus;
import com.roundrobin.vault.enums.VendorPreference;
import com.roundrobin.vault.repository.JobRepository;

@Service
public class JobService {

  @Autowired
  private VendorService vendorService;

  @Autowired
  private JobRepository jobRepo;

  @Autowired
  private UserProfileService userProfileService;

  @Autowired
  private SkillDetailService skillDetailService;

  public JobTo create(JobTo jobTo) {
    Job job = new Job();
    job.setLocation(jobTo.getLocation());
    job.setUserProfile(userProfileService.getByUserId(jobTo.getUser().getUserId()));
    job.setStatus(JobStatus.REQUESTED);
    job.setEndWindow(jobTo.getEndDate());
    job.setStartWindow(jobTo.getStartDate());
    job.setVendorPref(jobTo.getVendorPref());
    job.setSkillDetail(skillDetailService.get(jobTo.getSkillDetailId()));
    JobTimeline timeline = new JobTimeline();
    timeline.setCreated(DateTime.now());
    job.setTimeline(timeline);
    return convert(jobRepo.save(job));
  }

  // TODO Provide an api to list the vendors for query
  @Async
  public boolean schedule(Job job) {
    Optional<AvailableSlot> availableSlot = Optional.empty();
    if (job.getVendorPref() == VendorPreference.URGENT) {
      availableSlot = vendorService.getUrgentVendor(job);
    }
    if (availableSlot.isPresent()) {
      
    }
    return false;
  }

  public Job save(Job job) {
    return jobRepo.save(job);
  }

  public JobTo convert(Job job) {
    JobTo jobTo = new JobTo();
    jobTo.setJobId(job.getId());
    jobTo.setSkillDetailId(job.getSkillDetail().getId());
    jobTo.setLocation(job.getLocation());
    jobTo.setStatus(job.getStatus());
    return jobTo;
  }
}
