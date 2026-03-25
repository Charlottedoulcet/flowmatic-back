package com.flowmatic.flowmatic_back.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmatic.flowmatic_back.dto.request.user.UserCreateRequest;
import com.flowmatic.flowmatic_back.dto.request.user.UserUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.user.UserResponse;
import com.flowmatic.flowmatic_back.entity.User;
import com.flowmatic.flowmatic_back.entity.enums.Role;
import com.flowmatic.flowmatic_back.exception.BadRequestException;
import com.flowmatic.flowmatic_back.exception.DuplicateResourceException;
import com.flowmatic.flowmatic_back.exception.ResourceNotFoundException;
import com.flowmatic.flowmatic_back.mapper.UserMapper;
import com.flowmatic.flowmatic_back.repository.QuoteRepository;
import com.flowmatic.flowmatic_back.repository.UserRepository;
import com.flowmatic.flowmatic_back.security.UserDetail;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final QuoteRepository quoteRepository;

  public UserServiceImpl(UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      UserMapper userMapper,
      QuoteRepository quoteRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
    this.quoteRepository = quoteRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
        .map(UserDetail::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponse> getAllUsers(String userEmail) {
    User currentUser = findUserByEmail(userEmail);
    return userRepository.findByAgencyId(currentUser.getAgency().getId())
        .stream()
        .map(userMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional
  public UserResponse createUser(UserCreateRequest request, String userEmail) {
    User currentUser = findUserByEmail(userEmail);

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateResourceException("Email déjà utilisé : " + request.getEmail());
    }

    Set<Role> roles = parseRoles(request.getRoles());

    User user = new User();
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setAgency(currentUser.getAgency());
    user.setRoles(roles);

    return userMapper.toResponse(userRepository.save(user));
  }

  @Override
  @Transactional
  public UserResponse updateUser(Long id, UserUpdateRequest request, String userEmail) {
    User currentUser = findUserByEmail(userEmail);
    User user = userRepository.findByIdAndAgencyId(id, currentUser.getAgency().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé : " + id));

    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());

    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    Set<Role> roles = parseRoles(request.getRoles());

    if (user.getEmail().equals(userEmail)
        && user.getRoles().contains(Role.ADMIN)
        && !roles.contains(Role.ADMIN)) {
      throw new BadRequestException("Vous ne pouvez pas vous retirer le rôle administrateur");
    }

    user.getRoles().clear();
    user.getRoles().addAll(roles);

    return userMapper.toResponse(userRepository.save(user));
  }

  @Override
  @Transactional
  public void deleteUser(Long id, String userEmail) {
    User currentUser = findUserByEmail(userEmail);
    User user = userRepository.findByIdAndAgencyId(id, currentUser.getAgency().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé : " + id));

    if (user.getEmail().equals(userEmail)) {
      throw new BadRequestException("Vous ne pouvez pas supprimer votre propre compte");
    }

    quoteRepository.nullifyCreatedBy(id);
    userRepository.delete(user);
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé : " + email));
  }

  private Set<Role> parseRoles(Set<String> roleNames) {
    return roleNames.stream()
        .map(this::parseRole)
        .collect(Collectors.toSet());
  }

  private Role parseRole(String roleName) {
    try {
      return Role.valueOf(roleName);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Rôle invalide : " + roleName + ". Valeurs acceptées : ADMIN, EMPLOYEE");
    }
  }
}
