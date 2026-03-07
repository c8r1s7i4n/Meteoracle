package de.omnp.meteoracle;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.omnp.meteoracle.application.domain.ddd.DomainService;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

@Configuration
@ComponentScan(basePackages = {"de.omnp.meteoracle.application"},
                includeFilters = {@ComponentScan.Filter(type = ANNOTATION, classes = {DomainService.class})})
public class SpringConfiguration {
    
}
