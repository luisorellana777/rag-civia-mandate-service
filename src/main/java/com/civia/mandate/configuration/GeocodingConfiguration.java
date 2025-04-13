package com.civia.mandate.configuration;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeocodingConfiguration {

    @Value("${OPENCAGE_KEY}")
    private String openCageKey;

    @Bean
    public JOpenCageGeocoder geocoder(){

        return new JOpenCageGeocoder(openCageKey);
    }
}
