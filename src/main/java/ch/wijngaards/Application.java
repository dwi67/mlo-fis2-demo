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
import io.hawt.springboot.HawtPlugin;
import io.hawt.web.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import java.util.List;

/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application {

    private final static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {

        System.setProperty(AuthenticationFilter.HAWTIO_AUTHENTICATION_ENABLED, "false");

        LOG.info("Properties set");

        SpringApplication.run(Application.class, args);

        //ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        //CamelSpringBootApplicationController applicationController =
        //           applicationContext.getBean(CamelSpringBootApplicationController.class);

        //applicationController.run();
    }

//    @Override
//    public void configure() throws Exception {
//        from("timer://foo?period=5000")
//            .setBody().constant("Hello World")
//            .log(">>> ${body}");
//    }

    /**
     * Loading an example plugin.
     */
    @Bean
    public HawtPlugin samplePlugin() {
        return new HawtPlugin("sample-plugin",
                "/hawtio/plugins",
                "",
                new String[] { "sample-plugin/js/sample-plugin.js" });
    }

    /**
     * Set things up to be in offline mode.
     */
    @Bean
    public ConfigFacade configFacade() {
        System.setProperty("hawtio.offline", "true");
        LOG.info("Configfacade set");
        System.getProperties().forEach((key, value) -> LOG.info(key + "=" + value));
        return ConfigFacade.getSingleton();
    }
 }

