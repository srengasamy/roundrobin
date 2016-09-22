package com.roundrobin.vault.services;

import java.util.Optional;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.vault.api.VendorMapTo;
import com.roundrobin.vault.domain.AvailableSlot;
import com.roundrobin.vault.domain.JobAgenda;
import com.roundrobin.vault.domain.Skill;
import com.roundrobin.vault.domain.UserProfile;
import com.roundrobin.vault.repository.JobAgendaRepository;

@Service
public class AgendaService {

  @Autowired
  private JobAgendaRepository agendaRepo;

  @Autowired
  private UserProfileService userProfileService;

  @Autowired
  private SkillService skillService;

  public JobAgenda get(String vendorId, LocalDate date) {
    Optional<JobAgenda> agenda = agendaRepo.findByVendorIdAndDate(vendorId, date);
    if (agenda.isPresent()) {
      return agenda.get();
    }
    UserProfile userProfile = userProfileService.getByUserId(vendorId);
    JobAgenda jobAgenda = new JobAgenda();
    jobAgenda.setDate(date);
    jobAgenda.setVendor(userProfile);
    jobAgenda.getAvailables().add(new AvailableSlot(userProfile.getStartWindow(), userProfile.getEndWindow()));
    return save(jobAgenda);
  }


  public Optional<AvailableSlot> getAvailableNow(VendorMapTo vendorMapTo, String skillDetailId) {
    Skill skill = skillService.getBySkillDetailId(vendorMapTo.getVendorId(), skillDetailId);
    LocalTime start = LocalTime.now();
    LocalTime end = start.plusMinutes(skill.getTimeToComplete());
    JobAgenda agenda = get(vendorMapTo.getVendorId(), LocalDate.now());
    return agenda.getAvailables().stream().filter(a -> {
      if ((a.getStart().isEqual(start) || a.getStart().isBefore(start))
          && (a.getEnd().isEqual(end) || a.getEnd().isAfter(end))) {
        return true;
      }
      return false;
    }).findFirst();
  }

  private JobAgenda save(JobAgenda jobAgenda) {
    return agendaRepo.save(jobAgenda);
  }
}
