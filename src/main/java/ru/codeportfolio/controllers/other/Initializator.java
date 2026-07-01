package ru.codeportfolio.controllers.other;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Initializator implements WebApplicationInitializer {

    private static final String DISPATCHER = "dispatcher";

    @Override
    public void onStartup(ServletContext servletContext){
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SpringMVCConfig.class);
        servletContext.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet(
                        DISPATCHER, new DispatcherServlet(context));
        servletRegistration.addMapping("/");
        servletRegistration.setLoadOnStartup(1);


    }
}
