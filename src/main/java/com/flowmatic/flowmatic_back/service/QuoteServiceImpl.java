package com.flowmatic.flowmatic_back.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmatic.flowmatic_back.dto.response.quote.QuoteListResponse;
import com.flowmatic.flowmatic_back.entity.Quote;
import com.flowmatic.flowmatic_back.entity.User;
import com.flowmatic.flowmatic_back.entity.enums.QuoteStatus;
import com.flowmatic.flowmatic_back.exception.InvalidStatusTransitionException;
import com.flowmatic.flowmatic_back.exception.ResourceNotFoundException;
import com.flowmatic.flowmatic_back.mapper.QuoteMapper;
import com.flowmatic.flowmatic_back.repository.QuoteRepository;
import com.flowmatic.flowmatic_back.repository.UserRepository;

@Service
public class QuoteServiceImpl implements QuoteService {

  private final QuoteRepository quoteRepository;
  private final UserRepository userRepository;
  private final QuoteMapper quoteMapper;

  public QuoteServiceImpl(QuoteRepository quoteRepository, UserRepository userRepository, QuoteMapper quoteMapper) {
    this.quoteRepository = quoteRepository;
    this.userRepository = userRepository;
    this.quoteMapper = quoteMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public List<QuoteListResponse> getAllByAgency(String userEmail) {
    User user = findUserByEmail(userEmail);

    return quoteRepository.findByAgencyId(user.getAgency().getId())
        .stream()
        .map(quoteMapper::toListResponse)
        .toList();
  }

  @Override
  @Transactional
  public QuoteListResponse updateStatus(Long quoteId, QuoteStatus newStatus, String userEmail) {
    User user = findUserByEmail(userEmail);
    Long agencyId = user.getAgency().getId();

    Quote quote = quoteRepository.findByIdAndAgencyId(quoteId, agencyId)
        .orElseThrow(() -> new ResourceNotFoundException("Devis avec l'id " + quoteId + " introuvable"));

    validateStatusTransition(quote.getStatus(), newStatus);
    quote.setStatus(newStatus);
    Quote saved = quoteRepository.save(quote);

    return quoteMapper.toListResponse(saved);
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable: " + email));
  }

  private void validateStatusTransition(QuoteStatus current, QuoteStatus next) {
    boolean valid = switch (current) {

      case PENDING -> Set.of(QuoteStatus.SENT, QuoteStatus.CANCELLED).contains(next);
      case SENT -> Set.of(QuoteStatus.SIGNED, QuoteStatus.CANCELLED).contains(next);
      case SIGNED -> Set.of(QuoteStatus.PAID).contains(next);
      case PAID, CANCELLED -> false;
    };

    if (!valid) {
      throw new InvalidStatusTransitionException("Transition invalide : " + current + " → " + next);
    }
  }
}
