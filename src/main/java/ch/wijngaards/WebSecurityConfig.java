package ch.wijngaards;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // expose Spring Actuator security role for re-use with Jolokia

    @Value("${management.security.role}")
    private String managementSecurityRole;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // fix incompatibility with Hawt.io
                .csrf().disable()
                // once we're on Spring Security 4.0: .csrf().ignoringAntMatchers("/hawtio/**")

                /* now configure auth - secure /jolokia and rest secured by @Secured annotation */
                .authorizeRequests()

                // role required by Jolokia / Hawt.io
                .antMatchers("/jolokia", "/hawtio/**").hasRole(managementSecurityRole)

                .anyRequest().permitAll()

                // HTTP basic auth required by Hawt.io
                .and().httpBasic()
        ;
    }
}