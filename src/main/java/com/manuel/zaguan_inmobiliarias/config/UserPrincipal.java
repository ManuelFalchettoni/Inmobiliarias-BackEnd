package com.manuel.zaguan_inmobiliarias.config;

import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private final Long id;
    private final String email;
    private final String username;
    private final UserRol rol;
    private final Long agency_Id;

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROL_" + rol.name()));
    }

    @Override
    public @Nullable String getPassword(){ return  null; }

    @Override
    @NonNull
    public String getUsername() { return this.username; }



}
