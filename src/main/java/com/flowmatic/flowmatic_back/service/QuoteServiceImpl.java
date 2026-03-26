package com.flowmatic.flowmatic_back.service;

import java.time.Year;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmatic.flowmatic_back.dto.request.quote.PaymentConditionRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteAccomodationRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteCreateRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteDayRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteInclusionRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteSupplementRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteListResponse;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteResponse;
import com.flowmatic.flowmatic_back.entity.PaymentCondition;
import com.flowmatic.flowmatic_back.entity.Quote;
import com.flowmatic.flowmatic_back.entity.QuoteAccommodation;
import com.flowmatic.flowmatic_back.entity.QuoteDay;
import com.flowmatic.flowmatic_back.entity.QuoteInclusion;
import com.flowmatic.flowmatic_back.entity.QuoteSupplement;
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
    return quoteRepository.findByAgencyIdWithCreatedBy(user.getAgency().getId())
        .stream()
        .map(quoteMapper::toListResponse)
        .toList();
  }

  @Override
  @Transactional
  public QuoteListResponse updateStatus(Long quoteId, QuoteStatus newStatus, String userEmail) {
    User user = findUserByEmail(userEmail);
    Quote quote = findQuoteForAgency(quoteId, user.getAgency().getId());
    validateStatusTransition(quote.getStatus(), newStatus);
    quote.setStatus(newStatus);
    return quoteMapper.toListResponse(quoteRepository.save(quote));
  }

  @Override
  @Transactional
  public QuoteResponse createQuote(QuoteCreateRequest request, String userEmail) {
    User user = findUserByEmail(userEmail);
    Quote quote = quoteMapper.toEntity(request, user.getAgency(), user);
    Quote saved = quoteRepository.save(quote);
    saved.setReferenceNumber(generateReferenceNumber(saved.getId()));
    saved = quoteRepository.save(saved);
    return quoteMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public QuoteResponse getQuoteById(Long id, String userEmail) {
    User user = findUserByEmail(userEmail);
    Quote quote = findQuoteForAgency(id, user.getAgency().getId());
    return quoteMapper.toResponse(quote);
  }

  @Override
  @Transactional
  public QuoteResponse updateQuote(Long id, QuoteUpdateRequest request, String userEmail) {
    User user = findUserByEmail(userEmail);
    Quote quote = findQuoteForAgency(id, user.getAgency().getId());

    quoteMapper.updateEntity(quote, request);

    replaceDays(quote, request);
    replaceInclusions(quote, request);
    replacePaymentConditions(quote, request);
    replaceSupplements(quote, request);
    replaceAccommodations(quote, request);

    Quote saved = quoteRepository.save(quote);
    return quoteMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public void deleteQuote(Long id, String userEmail) {
    User user = findUserByEmail(userEmail);
    Quote quote = findQuoteForAgency(id, user.getAgency().getId());
    quoteRepository.delete(quote);
  }

  private void replaceDays(Quote quote, QuoteUpdateRequest request) {
    quote.getDays().clear();
    if (request.getDays() == null)
      return;
    for (QuoteDayRequest d : request.getDays()) {
      QuoteDay day = new QuoteDay();
      day.setQuote(quote);
      day.setDayNumber(d.getDayNumber());
      day.setDate(d.getDate());
      day.setTitle(d.getTitle());
      day.setSummary(d.getSummary());
      day.setNightLocation(d.getNightLocation());
      day.setDescription(d.getDescription());
      day.setTransportDuration(d.getTransportDuration());
      day.setAccommodationName(d.getAccommodationName());
      day.setAccommodationLocation(d.getAccommodationLocation());
      day.setRoomType(d.getRoomType());
      day.setMeals(d.getMeals());
      day.setIncludedInDay(d.getIncludedInDay());
      quote.getDays().add(day);
    }
  }

  private void replaceInclusions(Quote quote, QuoteUpdateRequest request) {
    quote.getInclusions().clear();
    if (request.getInclusions() == null)
      return;
    for (QuoteInclusionRequest i : request.getInclusions()) {
      QuoteInclusion inc = new QuoteInclusion();
      inc.setQuote(quote);
      inc.setDescription(i.getDescription());
      inc.setIncluded(i.getIncluded());
      quote.getInclusions().add(inc);
    }
  }

  private void replacePaymentConditions(Quote quote, QuoteUpdateRequest request) {
    quote.getPaymentConditions().clear();
    if (request.getPaymentConditions() == null)
      return;
    for (PaymentConditionRequest p : request.getPaymentConditions()) {
      PaymentCondition cond = new PaymentCondition();
      cond.setQuote(quote);
      cond.setDescription(p.getDescription());
      cond.setPercentage(p.getPercentage());
      cond.setDueDateDescription(p.getDueDateDescription());
      quote.getPaymentConditions().add(cond);
    }
  }

  private void replaceSupplements(Quote quote, QuoteUpdateRequest request) {
    quote.getSupplements().clear();
    if (request.getSupplements() == null)
      return;
    for (QuoteSupplementRequest s : request.getSupplements()) {
      QuoteSupplement sup = new QuoteSupplement();
      sup.setQuote(quote);
      sup.setDescription(s.getDescription());
      sup.setPricePerPerson(s.getPricePerPerson());
      sup.setTotalPrice(s.getTotalPrice());
      sup.setCurrency(s.getCurrency() != null ? s.getCurrency() : "EUR");
      quote.getSupplements().add(sup);
    }
  }

  private void replaceAccommodations(Quote quote, QuoteUpdateRequest request) {
    quote.getAccommodations().clear();
    if (request.getAccommodations() == null)
      return;
    for (QuoteAccomodationRequest a : request.getAccommodations()) {
      QuoteAccommodation acc = new QuoteAccommodation();
      acc.setQuote(quote);
      acc.setName(a.getName());
      acc.setLocation(a.getLocation());
      acc.setRating(a.getRating());
      acc.setDescription(a.getDescription());
      acc.setDisplayOrder(a.getDisplayOrder());
      quote.getAccommodations().add(acc);
    }
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable: " + email));
  }

  private Quote findQuoteForAgency(Long quoteId, Long agencyId) {
    return quoteRepository.findByIdAndAgencyId(quoteId, agencyId)
        .orElseThrow(() -> new ResourceNotFoundException("Devis avec l'id " + quoteId + " introuvable"));
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

  private String generateReferenceNumber(Long quoteId) {
    return "DEV-%d-%04d".formatted(Year.now().getValue(), quoteId);
  }
}
