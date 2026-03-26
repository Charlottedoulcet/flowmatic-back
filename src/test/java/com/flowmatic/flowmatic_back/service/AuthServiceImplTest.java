package com.flowmatic.flowmatic_back.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.flowmatic.flowmatic_back.dto.request.auth.RegisterRequest;
import com.flowmatic.flowmatic_back.dto.response.auth.AuthResponse;
import com.flowmatic.flowmatic_back.entity.Agency;
import com.flowmatic.flowmatic_back.entity.User;
import com.flowmatic.flowmatic_back.entity.enums.Role;
import com.flowmatic.flowmatic_back.exception.DuplicateResourceException;
import com.flowmatic.flowmatic_back.repository.AgencyRepository;
import com.flowmatic.flowmatic_back.repository.UserRepository;
import com.flowmatic.flowmatic_back.security.JwtTokenService;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AgencyRepository agencyRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_newEmail_createsAgencyAndUserWithBothRoles() {
        RegisterRequest request = buildRegisterRequest("alice@agence.com");

        Agency savedAgency = new Agency();
        savedAgency.setId(1L);

        User savedUser = new User();
        savedUser.setId(10L);
        savedUser.setEmail("alice@agence.com");
        savedUser.setFirstName("Alice");
        savedUser.setLastName("Dupont");
        savedUser.setAgency(savedAgency);
        savedUser.setRoles(Set.of(Role.ADMIN, Role.EMPLOYEE));

        when(userRepository.existsByEmail("alice@agence.com")).thenReturn(false);
        when(agencyRepository.save(any(Agency.class))).thenReturn(savedAgency);
        when(passwordEncoder.encode("secret123")).thenReturn("$2a$hash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtTokenService.createToken(anyString(), anyLong(), anyLong(), anySet()))
                .thenReturn("jwt.token.here");

        AuthResponse response = authService.register(request);

        assertThat(response.getToken()).isEqualTo("Bearer jwt.token.here");
        assertThat(response.getFirstName()).isEqualTo("Alice");
        assertThat(response.getLastName()).isEqualTo("Dupont");
        assertThat(response.getAgencyId()).isEqualTo(1L);
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getRoles()).containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_EMPLOYEE");
        verify(agencyRepository).save(any(Agency.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throws() {
        when(userRepository.existsByEmail("existant@agence.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(buildRegisterRequest("existant@agence.com")))
                .isInstanceOf(DuplicateResourceException.class);

        verify(agencyRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_newEmail_passwordIsEncoded() {
        RegisterRequest request = buildRegisterRequest("bob@agence.com");

        Agency savedAgency = new Agency();
        savedAgency.setId(2L);

        User savedUser = new User();
        savedUser.setId(20L);
        savedUser.setEmail("bob@agence.com");
        savedUser.setFirstName("Bob");
        savedUser.setLastName("Martin");
        savedUser.setAgency(savedAgency);
        savedUser.setRoles(Set.of(Role.ADMIN, Role.EMPLOYEE));

        when(userRepository.existsByEmail("bob@agence.com")).thenReturn(false);
        when(agencyRepository.save(any(Agency.class))).thenReturn(savedAgency);
        when(passwordEncoder.encode("secret123")).thenReturn("$2a$hashed");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtTokenService.createToken(anyString(), anyLong(), anyLong(), anySet())).thenReturn("token");

        authService.register(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("$2a$hashed");
    }

    private RegisterRequest buildRegisterRequest(String email) {
        RegisterRequest request = new RegisterRequest();
        request.setAgencyName("Agence Alice");
        request.setFirstName("Alice");
        request.setLastName("Dupont");
        request.setEmail(email);
        request.setPassword("secret123");
        return request;
    }
}
