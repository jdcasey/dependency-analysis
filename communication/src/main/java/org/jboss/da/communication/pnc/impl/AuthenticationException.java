package org.jboss.da.communication.pnc.impl;

import javax.validation.constraints.NotNull;

import lombok.Getter;

/**
 * Exception, which is used to report issues with authentication
 * 
 * @author Jakub Bartecek <jbartece@redhat.com>
 *
 */
public class AuthenticationException extends Exception {

    /**
     * Access token with which authentication failed
     */
    @NotNull
    @Getter
    private final String accessToken;

    public AuthenticationException(String accessToken) {
        this.accessToken = accessToken;
    }

    public AuthenticationException(String msg, String accessToken) {
        super(msg);
        this.accessToken = accessToken;
    }

    public AuthenticationException(String msg, Throwable ex, String accessToken) {
        super(msg, ex);
        this.accessToken = accessToken;
    }

}
