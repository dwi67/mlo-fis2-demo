/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package ch.wijngaards;

import io.hawt.config.ConfigFacade;
import io.hawt.web.AuthenticationFilter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@SpringBootApplication
@EnableGlobalMethodSecurity
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends RouteBuilder {

    public static void main(String[] args) throws Exception {

        System.setProperty(AuthenticationFilter.HAWTIO_AUTHENTICATION_ENABLED, "true");

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        CamelSpringBootApplicationController applicationController =
                   applicationContext.getBean(CamelSpringBootApplicationController.class);

        applicationController.run();
    }

    @Override
    public void configure() throws Exception {
        from("timer://foo?period=5000")
            .setBody().constant("Hello World")
            .log(">>> ${body}");
    }

    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("admin").password("admin").roles("ADMIN").build());
        return manager;
    }
 }

