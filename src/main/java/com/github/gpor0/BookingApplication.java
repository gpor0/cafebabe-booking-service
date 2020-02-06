package com.github.gpor0;

import com.github.gpor0.resources.BookingResource;
import io.narayana.lra.filter.FilterRegistration;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@ApplicationPath("/")
public class BookingApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> classes = new HashSet<>();
        classes.add(BookingResource.class);
        classes.add(FilterRegistration.class);
        classes.add(JacksonFeature.class);
        classes.add(RequestFilter.class);

        return classes;
    }

}
