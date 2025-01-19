package com.mikepn.banking.enums.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissions {

    STAFF_READ("admin:read"),
    STAFF_UPDATE("admin:update"),
    STAFF_CREATE("admin:create"),
    STAFF_DELETE("admin:delete"),

    ADMIN_READ("manager:read"),
    ADMIN_UPDATE("manager:update"),
    ADMIN_CREATE("manager:create"),
    ADMIN_DELETE("manager:delete");

    @Getter
    private final String permission;


}

