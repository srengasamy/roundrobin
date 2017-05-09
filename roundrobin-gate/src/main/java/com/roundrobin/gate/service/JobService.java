package com.roundrobin.gate.service;

import com.roundrobin.core.api.User;
import com.roundrobin.core.service.GenericService;
import com.roundrobin.gate.api.JobTo;
import com.roundrobin.gate.domain.Job;
import com.roundrobin.gate.domain.UserProfile;
import com.roundrobin.gate.enums.JobStatus;
import com.roundrobin.gate.repository.JobRepository;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.roundrobin.core.common.Preconditions.badRequest;
import static com.roundrobin.gate.common.ErrorCodes.INVALID_JOB_ID;
import static com.roundrobin.gate.common.ErrorCodes.INVALID_PREF_DATE;

@Service
public class JobService implements GenericService<Job, JobTo> {

  @Autowired
  private JobRepository jobRepo;

  @Autowired
  private UserProfileService profileService;

  @Autowired
  private SkillDetailService skillDetailService;

  @Override
  public Job get(User user, String id) {
    Optional<Job> job = jobRepo.findById(id);
    badRequest(job.isPresent()
                    && job.get().isActive()
                    && job.get().getProfile().getId().equals(user.getUserId()),
            INVALID_JOB_ID, "job_id");
    return job.get();
  }

  @Override
  public Job save(Job job) {
    return jobRepo.save(job);
  }

  @Override
  public JobTo read(User user, String id) {
    return convert(get(user, id));
  }

  @Override
  public JobTo create(User user, JobTo jobTo) {
    validatePrefDate(jobTo);
    UserProfile profile = profileService.get(user);
    Job job = new Job();
    job.setActive(true);
    job.setCreated(DateTime.now());
    job.setProfile(profile);
    job.setSkillDetail(skillDetailService.get(jobTo.getSkillDetailId()));
    job.setVendorPref(jobTo.getVendorPref());
    job.setLocation(jobTo.getLocation());
    job.setDesc(jobTo.getDesc());
    job.setStatus(JobStatus.SCHEDULED);
    job.setPreferredStart(jobTo.getPreferredStart());
    job.setPreferredEnd(jobTo.getPreferredEnd());
    return convert(save(job));
  }

  @Override
  public JobTo update(User user, JobTo jobTo) {
    validatePrefDate(jobTo);
    Job job = get(user, jobTo.getId());
    job.setDesc(Optional.ofNullable(jobTo.getDesc()).orElse(job.getDesc()));
    job.setLocation(Optional.ofNullable(jobTo.getLocation()).orElse(job.getLocation()));
    job.setVendorPref(Optional.ofNullable(jobTo.getVendorPref()).orElse(job.getVendorPref()));
    job.setPreferredStart(Optional.ofNullable(jobTo.getPreferredStart()).orElse(job.getPreferredStart()));
    job.setPreferredEnd(Optional.ofNullable(jobTo.getPreferredEnd()).orElse(job.getPreferredEnd()));
    return convert(save(job));
  }

  @Override
  public void delete(User user, String id) {
    Job job = get(user, id);
    job.setActive(false);
    save(job);
  }

  @Override
  public List<JobTo> list(User user) {
    UserProfile profile = profileService.get(user);
    return jobRepo.findAllByProfileAndCreatedBetweenOrderByCreatedDesc(profile,
            DateTime.now().minusDays(1),
            DateTime.now().plusDays(1)
    ).stream().map(j -> convert(j)).collect(Collectors.toList());
  }

  @Override
  public JobTo convert(Job job) {
    JobTo jobTo = new JobTo();
    jobTo.setId(job.getId());
    jobTo.setDesc(job.getDesc());
    jobTo.setSkillDetailId(job.getSkillDetail().getId());
    jobTo.setPreferredStart(job.getPreferredStart());
    jobTo.setPreferredEnd(job.getPreferredEnd());
    jobTo.setVendorPref(job.getVendorPref());
    jobTo.setCreated(job.getCreated());
    jobTo.setLocation(job.getLocation());
    jobTo.setStatus(job.getStatus());
    return jobTo;
  }

  private void validatePrefDate(JobTo jobTo) {
    if (jobTo.getPreferredStart() != null) {
      LocalDate startDate = jobTo.getPreferredStart().toLocalDate();
      badRequest(startDate.isEqual(LocalDate.now()) || startDate.isAfter(LocalDate.now()),
              INVALID_PREF_DATE, "preferred_start");
    }
    if (jobTo.getPreferredEnd() != null) {
      LocalDate endDate = jobTo.getPreferredEnd().toLocalDate();
      badRequest(endDate.isEqual(LocalDate.now()) || endDate.isAfter(LocalDate.now()),
              INVALID_PREF_DATE, "preferred_end");
    }
    if (jobTo.getPreferredStart() != null && jobTo.getPreferredEnd() != null) {
      badRequest(jobTo.getPreferredStart().isBefore(jobTo.getPreferredEnd()),
              INVALID_PREF_DATE, "preferred_date");
    }
  }
}
