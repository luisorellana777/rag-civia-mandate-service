package com.civia.mandate.dto.inout;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ClusterMandateResponse extends MandateResponse{

    private Float centroidProximity;
}
