package ch.wijngaards;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by wijngaards on 06.09.17.
 */
@Component
public class SimpleTimerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:foo").to("log:SimpleTimerRoute");
    }
}

