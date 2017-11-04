package com.solofeed.genesis.core.security.model;

/**
 * Authorities scopes
 */
public enum Scope {
    REFRESH_TOKEN;

    public String authority() {
        return "ROLE_" + this.name();
    }
}
