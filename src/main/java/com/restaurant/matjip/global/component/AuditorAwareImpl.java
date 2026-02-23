package com.restaurant.matjip.global.component;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth == null || !auth.isAuthenticated()
//                || auth instanceof AnonymousAuthenticationToken) {
//            return Optional.of(1L);
//        }

        return Optional.of(1L);
    }
}
