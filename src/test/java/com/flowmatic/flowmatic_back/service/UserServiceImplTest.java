package com.flowmatic.flowmatic_back.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.flowmatic.flowmatic_back.dto.request.user.UserCreateRequest;
import com.flowmatic.flowmatic_back.dto.request.user.UserUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.user.UserResponse;
import com.flowmatic.flowmatic_back.entity.Agency;
import com.flowmatic.flowmatic_back.entity.User;
import com.flowmatic.flowmatic_back.entity.enums.Role;
import com.flowmatic.flowmatic_back.exception.BadRequestException;
import com.flowmatic.flowmatic_back.exception.DuplicateResourceException;
import com.flowmatic.flowmatic_back.exception.ResourceNotFoundException;
import com.flowmatic.flowmatic_back.mapper.UserMapper;
import com.flowmatic.flowmatic_back.repository.QuoteRepository;
import com.flowmatic.flowmatic_back.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private QuoteRepository quoteRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private Agency agency;
    private User admin;

    @BeforeEach
    void setUp() {
        agency = new Agency();
        agency.setId(1L);
        agency.setName("Agence Test");

        admin = new User();
        admin.setId(1L);
        admin.setEmail("admin@test.com");
        admin.setAgency(agency);
        admin.setRoles(new HashSet<>(Set.of(Role.ADMIN, Role.EMPLOYEE)));
    }

    @Test
    void createUser_validRequest_savesUserWithEncodedPassword() {
        UserCreateRequest request = buildCreateRequest("nouvel@test.com", Set.of("EMPLOYEE"));

        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(userRepository.existsByEmail("nouvel@test.com")).thenReturn(false);
        when(passwordEncoder.encode("motdepasse123")).thenReturn("$2a$hash");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toResponse(any(User.class))).thenReturn(new UserResponse());

        userService.createUser(request, "admin@test.com");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("$2a$hash");
        assertThat(captor.getValue().getAgency()).isEqualTo(agency);
    }

    @Test
    void createUser_duplicateEmail_throws() {
        UserCreateRequest request = buildCreateRequest("existant@test.com", Set.of("EMPLOYEE"));

        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(userRepository.existsByEmail("existant@test.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request, "admin@test.com"))
                .isInstanceOf(DuplicateResourceException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_invalidRole_throws() {
        UserCreateRequest request = buildCreateRequest("nouvel@test.com", Set.of("SUPERADMIN"));

        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(userRepository.existsByEmail("nouvel@test.com")).thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(request, "admin@test.com"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("SUPERADMIN");
    }

    @Test
    void deleteUser_otherEmployee_nullifiesQuotesAndDeletes() {
        User employe = buildEmployee(99L, "employe@test.com");

        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(userRepository.findByIdAndAgencyId(99L, 1L)).thenReturn(Optional.of(employe));

        userService.deleteUser(99L, "admin@test.com");

        verify(quoteRepository).nullifyCreatedBy(99L);
        verify(userRepository).delete(employe);
    }

    @Test
    void deleteUser_self_throws() {
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(userRepository.findByIdAndAgencyId(1L, 1L)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> userService.deleteUser(1L, "admin@test.com"))
                .isInstanceOf(BadRequestException.class);

        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUser_otherAgency_throws() {
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(userRepository.findByIdAndAgencyId(999L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(999L, "admin@test.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateUser_removeSelfAdminRole_throws() {
        UserUpdateRequest request = buildUpdateRequest("admin@test.com", Set.of("EMPLOYEE"));

        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(userRepository.findByIdAndAgencyId(1L, 1L)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> userService.updateUser(1L, request, "admin@test.com"))
                .isInstanceOf(BadRequestException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_withoutPassword_doesNotReencodePassword() {
        User employe = buildEmployee(99L, "employe@test.com");
        employe.setRoles(new HashSet<>(Set.of(Role.EMPLOYEE)));
        String originalPassword = employe.getPassword();

        UserUpdateRequest request = buildUpdateRequest("employe@test.com", Set.of("EMPLOYEE"));
        request.setPassword(null);

        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(userRepository.findByIdAndAgencyId(99L, 1L)).thenReturn(Optional.of(employe));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toResponse(any(User.class))).thenReturn(new UserResponse());

        userService.updateUser(99L, request, "admin@test.com");

        verify(passwordEncoder, never()).encode(anyString());
        assertThat(employe.getPassword()).isEqualTo(originalPassword);
    }

    private UserCreateRequest buildCreateRequest(String email, Set<String> roles) {
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("Marie");
        request.setLastName("Curie");
        request.setEmail(email);
        request.setPassword("motdepasse123");
        request.setRoles(roles);
        return request;
    }

    private UserUpdateRequest buildUpdateRequest(String email, Set<String> roles) {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("Marie");
        request.setLastName("Curie");
        request.setEmail(email);
        request.setRoles(roles);
        return request;
    }

    private User buildEmployee(Long id, String email) {
        User employe = new User();
        employe.setId(id);
        employe.setEmail(email);
        employe.setPassword("$2a$encoded");
        employe.setAgency(agency);
        employe.setRoles(new HashSet<>(Set.of(Role.EMPLOYEE)));
        return employe;
    }
}
