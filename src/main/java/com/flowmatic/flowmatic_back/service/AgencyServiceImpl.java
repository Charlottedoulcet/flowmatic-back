package com.flowmatic.flowmatic_back.service;

import org.springframework.stereotype.Service;

import com.flowmatic.flowmatic_back.dto.request.agency.AgencyUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.agency.AgencyResponse;
import com.flowmatic.flowmatic_back.entity.Agency;
import com.flowmatic.flowmatic_back.entity.User;
import com.flowmatic.flowmatic_back.exception.ResourceNotFoundException;
import com.flowmatic.flowmatic_back.mapper.AgencyMapper;
import com.flowmatic.flowmatic_back.repository.AgencyRepository;
import com.flowmatic.flowmatic_back.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AgencyServiceImpl implements AgencyService {

  private final AgencyRepository agencyRepository;
  private final UserRepository userRepository;
  private final AgencyMapper agencyMapper;

  public AgencyServiceImpl(AgencyRepository agencyRepository, UserRepository userRepository,
      AgencyMapper agencyMapper) {
    this.agencyRepository = agencyRepository;
    this.userRepository = userRepository;
    this.agencyMapper = agencyMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public AgencyResponse getAgency(String userEmail) {
    User user = findUserByEmail(userEmail);
    return agencyMapper.toResponse(user.getAgency());
  }

  @Override
  @Transactional
  public AgencyResponse updateAgency(AgencyUpdateRequest request, String userEmail) {
    User user = findUserByEmail(userEmail);
    Agency agency = user.getAgency();
    agencyMapper.updateEntity(agency, request);
    Agency saved = agencyRepository.save(agency);
    return agencyMapper.toResponse(saved);
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé : " + email));
  }

}
