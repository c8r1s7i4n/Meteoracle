package de.omnp.meteoracle;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.omnp.meteoracle.domain.ddd.DomainService;

@Configuration
@ComponentScan(basePackages = { "de.omnp.meteoracle.application" }, includeFilters = {
        @ComponentScan.Filter(type = ANNOTATION, classes = { DomainService.class }) })
public class SpringConfiguration {

}
