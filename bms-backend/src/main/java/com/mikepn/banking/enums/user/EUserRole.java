package com.mikepn.banking.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mikepn.banking.enums.user.Permissions.*;

@RequiredArgsConstructor
@AllArgsConstructor
public enum EUserRole {
    CUSTOMER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    Permissions.ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    STAFF_READ,
                    STAFF_UPDATE,
                    STAFF_DELETE,
                    STAFF_CREATE
            )
    ),
    STAFF(
            Set.of(
                    STAFF_READ,
                    STAFF_UPDATE,
                    STAFF_DELETE,
                    STAFF_CREATE
            )
    )
    ;

    @Getter
    private Set<Permissions> permissions;


    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
