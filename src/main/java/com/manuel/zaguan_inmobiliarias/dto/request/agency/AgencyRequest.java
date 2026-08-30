package com.manuel.zaguan_inmobiliarias.dto.request.agency;

import com.manuel.zaguan_inmobiliarias.enums.agency.AgencyStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgencyRequest {

    @NotBlank
    @Size(min = 3, max = 30, message = "Company name must be between 3 and 30 characters." )
    private String companyName;

    @NotBlank
    @Size(min = 3, max = 30, message = "Public name must be between 3 and 30 characters.")
    private String publicName;

    @NotNull
    @Min(value = 1, message = "Owner Id must be a positive number.")
    private Long ownerId;

    @NotBlank
    @Size(min = 6, max = 40, message = "Address must be between 6 and 40 characters.")
    private String address;

    @NotBlank
    private String webURL;

    @NotBlank
    private String socials;

    @NotNull
    private AgencyStatus status;
}
