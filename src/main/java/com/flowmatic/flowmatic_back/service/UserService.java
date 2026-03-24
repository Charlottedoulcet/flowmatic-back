package com.flowmatic.flowmatic_back.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    // UserDetailsService déclare déjà :
    // UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    // On ajoutera les méthodes CRUD User ici plus tard (T-5.1)
}
