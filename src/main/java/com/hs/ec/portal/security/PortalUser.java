package com.hs.ec.portal.security;

import com.hs.ec.portal.domain.User;
import com.hs.ec.portal.service.dto.ResourceAuthorityDTO;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Flux;

/**
 * @author Hossein Sadeghi on 9/4/2019.
 */
public class PortalUser extends org.springframework.security.core.userdetails.User {

    private List<ResourceAuthorityDTO> resourceAuthorities;
    private Long partyId;
    private String partyTitle;
    private User user;

    public PortalUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public PortalUser(
        String username,
        String password,
        boolean enabled,
        boolean accountNonExpired,
        boolean credentialsNonExpired,
        boolean accountNonLocked,
        Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public PortalUser(
        String username,
        String password,
        boolean enabled,
        boolean accountNonExpired,
        boolean credentialsNonExpired,
        boolean accountNonLocked,
        Collection<? extends GrantedAuthority> authorities,
        Long partyId,
        String partyTitle,
        List<ResourceAuthorityDTO> resourceAuthorities,
        User user
    ) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.resourceAuthorities = resourceAuthorities;
        this.partyId = partyId;
        this.partyTitle = partyTitle;
        this.user = user;
    }

    public PortalUser(
        String username,
        String password,
        boolean enabled,
        boolean accountNonExpired,
        boolean credentialsNonExpired,
        boolean accountNonLocked,
        Collection<? extends GrantedAuthority> authorities,
        Long partyId,
        String partyTitle,
        Flux<ResourceAuthorityDTO> resourceAuthorities,
        User user
    ) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        resourceAuthorities.collectList().map(resourceAuthorityDTOS -> this.resourceAuthorities = resourceAuthorityDTOS);
        this.partyId = partyId;
        this.partyTitle = partyTitle;
        this.user = user;
    }

    public List<ResourceAuthorityDTO> getResourceAuthorities() {
        return resourceAuthorities;
    }

    public void setResourceAuthorities(List<ResourceAuthorityDTO> resourceAuthorities) {
        this.resourceAuthorities = resourceAuthorities;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPartyTitle() {
        return partyTitle;
    }

    public void setPartyTitle(String partyTitle) {
        this.partyTitle = partyTitle;
    }
}
