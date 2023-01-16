package com.prgrms.bdbks.config.jpa;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        /**
         * todo : 현재 authenticate 된 객체를 가져와서 Id값을 꺼낸다. Security 적용시 적용 필요.
         * need : UserDetails를 구현한 Principal 객체가 필요.
         * example)
         *
         * Principal 객체 user = (Principal 객체)authentication
         *
         * return Optional.of(user.getId());
         *
         */

        return Optional.empty();
    }
}
