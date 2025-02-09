package edu.oregonstate.cs467.travelplanner.config;

import edu.oregonstate.cs467.travelplanner.util.CatchAllExceptionResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

// disable temporarily
//@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new CatchAllExceptionResolver());
    }
}
