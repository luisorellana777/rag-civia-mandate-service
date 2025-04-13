package com.civia.mandate.service.geocoder;

import com.civia.mandate.dto.MandateDto;
import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GeocoderService {

    private JOpenCageGeocoder geocoder;
    public List<MandateDto> getCoordinates(List<MandateDto> mandatesDto) {

        return mandatesDto.stream().map(mandateDto-> {

            var request = new JOpenCageForwardRequest(mandateDto.getAddress());
            var response = geocoder.forward(request);
            double lat = response.getResults().get(0).getGeometry().getLat();
            double lng = response.getResults().get(0).getGeometry().getLng();

            return mandateDto.toBuilder().location(MandateDto.Location.builder().type("Point").coordinates(List.of(lat, lng)).build()).build();
        }).toList();
    }
}
