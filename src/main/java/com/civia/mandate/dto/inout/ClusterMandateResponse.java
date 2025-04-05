package com.civia.mandate.dto.inout;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ClusterMandateResponse extends MandateResponse{

    private Float centroidProximity;
}
