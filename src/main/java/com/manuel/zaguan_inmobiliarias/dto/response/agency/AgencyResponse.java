package com.manuel.zaguan_inmobiliarias.dto.response.agency;

import com.manuel.zaguan_inmobiliarias.enums.agency.AgencyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgencyResponse {


    private Long id;

    private String companyName;

    private String publicName;

    private Long ownerId;

    private String address;

    private String webURL;

    private String socials;

    private AgencyStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
