package com.kaoyaya.jenkins.cd.utils;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.common.AbstractIdCredentialsListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardCredentials;

import javax.annotation.Nonnull;

public class CredentialsListBoxModel
        extends AbstractIdCredentialsListBoxModel<CredentialsListBoxModel, StandardCredentials> {
    private static final long serialVersionUID = 1L;

    @Nonnull
    protected String describe(@Nonnull StandardCredentials c) {
        return CredentialsNameProvider.name(c);
    }
}
