package ch.wijngaards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // expose Spring Actuator security role for re-use with Jolokia

    private static final Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);

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

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("admin").password("admin").roles("admin").build());

        LOG.info(manager.toString());

        return manager;
    }

}