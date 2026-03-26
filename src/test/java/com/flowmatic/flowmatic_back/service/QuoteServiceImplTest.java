package com.flowmatic.flowmatic_back.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flowmatic.flowmatic_back.dto.request.quote.QuoteCreateRequest;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteResponse;
import com.flowmatic.flowmatic_back.entity.Agency;
import com.flowmatic.flowmatic_back.entity.Quote;
import com.flowmatic.flowmatic_back.entity.User;
import com.flowmatic.flowmatic_back.exception.ResourceNotFoundException;
import com.flowmatic.flowmatic_back.mapper.QuoteMapper;
import com.flowmatic.flowmatic_back.repository.QuoteRepository;
import com.flowmatic.flowmatic_back.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class QuoteServiceImplTest {

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuoteMapper quoteMapper;

    @InjectMocks
    private QuoteServiceImpl quoteService;

    private Agency agency;
    private User user;

    @BeforeEach
    void setUp() {
        agency = new Agency();
        agency.setId(1L);
        agency.setName("Agence Test");

        user = new User();
        user.setId(10L);
        user.setEmail("employe@test.com");
        user.setFirstName("Alice");
        user.setLastName("Dupont");
        user.setAgency(agency);
    }

    @Test
    void createQuote_validRequest_createsQuoteWithReferenceNumber() {
        QuoteCreateRequest request = buildCreateRequest();
        Quote quote = new Quote();
        quote.setAgency(agency);
        QuoteResponse expectedResponse = new QuoteResponse();

        when(userRepository.findByEmail("employe@test.com")).thenReturn(Optional.of(user));
        when(quoteMapper.toEntity(request, agency, user)).thenReturn(quote);
        when(quoteRepository.countByAgencyIdLocked(1L)).thenReturn(0L);
        when(quoteRepository.save(quote)).thenReturn(quote);
        when(quoteMapper.toResponse(quote)).thenReturn(expectedResponse);

        QuoteResponse result = quoteService.createQuote(request, "employe@test.com");

        assertThat(quote.getReferenceNumber()).isNotNull();
        assertThat(quote.getReferenceNumber()).startsWith("DEV-");
        assertThat(result).isEqualTo(expectedResponse);
        verify(quoteRepository).save(quote);
    }

    @Test
    void createQuote_unknownEmail_throws() {
        when(userRepository.findByEmail("inconnu@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quoteService.createQuote(buildCreateRequest(), "inconnu@test.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteQuote_ownAgency_deletesSuccessfully() {
        Quote quote = buildQuote();

        when(userRepository.findByEmail("employe@test.com")).thenReturn(Optional.of(user));
        when(quoteRepository.findByIdAndAgencyId(1L, 1L)).thenReturn(Optional.of(quote));

        quoteService.deleteQuote(1L, "employe@test.com");

        verify(quoteRepository).delete(quote);
    }

    @Test
    void deleteQuote_otherAgency_throws() {
        when(userRepository.findByEmail("employe@test.com")).thenReturn(Optional.of(user));
        when(quoteRepository.findByIdAndAgencyId(99L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quoteService.deleteQuote(99L, "employe@test.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Quote buildQuote() {
        Quote quote = new Quote();
        quote.setId(1L);
        quote.setAgency(agency);
        quote.setCreatedBy(user);
        quote.setTitle("Voyage au Maroc");
        quote.setClientName("Jean Martin");
        return quote;
    }

    private QuoteCreateRequest buildCreateRequest() {
        QuoteCreateRequest request = new QuoteCreateRequest();
        request.setTitle("Voyage au Portugal");
        request.setClientName("Marie Curie");
        request.setParticipantCount(2);
        request.setDestination("Lisbonne");
        request.setStartDate(LocalDate.of(2026, 6, 1));
        request.setEndDate(LocalDate.of(2026, 6, 10));
        request.setPricePerPerson(new BigDecimal("1500.00"));
        return request;
    }
}
