package com.github.RandyDpoe45.amilozdemo.persistence.model.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.user.dtos.UserResponseViews;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AuthenticationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(UserResponseViews.UserResponseView.class)
    private Long id;

    @Column(unique = true, nullable = false)
    @JsonView(UserResponseViews.UserResponseView.class)
    private String username;

    @Column(nullable = false, length = 500)
    private String password;

    @JsonView(UserResponseViews.UserResponseView.class)
    private boolean enabled;

    @OneToOne(targetEntity = AmilozUser.class, mappedBy = "authenticationUser")
    @JsonView(UserResponseViews.UserResponseView.class)
    private AmilozUser amilozUser;

    @ManyToMany(targetEntity = UserRole.class, fetch = FetchType.EAGER)
    @JsonView(UserResponseViews.UserResponseView.class)
    private List<UserRole> userRoles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles.stream()
                .map(x -> new SimpleGrantedAuthority(x.getName()))
                .toList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
