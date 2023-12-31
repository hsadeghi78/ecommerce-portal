package com.hs.ec.portal.security;

import com.hs.ec.portal.domain.User;
import com.hs.ec.portal.repository.UserRepository;
import com.hs.ec.portal.service.ResourceAuthorityService;
import java.util.*;
import java.util.stream.Collectors;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements ReactiveUserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;
    private final ResourceAuthorityService resourceAuthorityService;

    public DomainUserDetailsService(UserRepository userRepository, ResourceAuthorityService resourceAuthorityService) {
        this.userRepository = userRepository;
        this.resourceAuthorityService = resourceAuthorityService;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserDetails> findByUsername(final String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return userRepository
                .findOneWithAuthoritiesByEmailIgnoreCase(login)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User with email " + login + " was not found in the database")))
                .flatMap(user -> createSpringSecurityUser(login, user));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        /*return userRepository
            .findOneWithAuthoritiesByLogin(lowercaseLogin)
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database")))
            .flatMap(user -> createSpringSecurityUser(lowercaseLogin, user));*/

        return userRepository
            .findOneWithEagerRelationships(lowercaseLogin)
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database")))
            .flatMap(user -> createSpringSecurityUser(lowercaseLogin, user));
    }

    /*private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user
            .getAuthorities()
            .stream()
            .map(Authority::getName)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthorities);
    }*/

    private Mono<UserDetails> createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user
            .getAuthorities()
            .stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());

        List<String> ids = new ArrayList<>();
        grantedAuthorities.forEach(grantedAuthority -> {
            ids.add(grantedAuthority.getAuthority());
        });

        return resourceAuthorityService
            .findByAuthorities(ids, Pageable.unpaged())
            .collectList()
            .flatMap(resAuths -> {
                // Retrieve the UserDetails object for the current Role
                PortalUser userDetails = new PortalUser(
                    user.getLogin(),
                    user.getPassword(),
                    user.isActivated(),
                    true,
                    true,
                    true,
                    grantedAuthorities,
                    user.getParty().getId(),
                    user.getParty().getTitle(),
                    resAuths,
                    user
                );

                // Convert the UserDetails object to a Mono
                return Mono.just(userDetails);
            });
    }
}
