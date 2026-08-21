package com.manuel.zaguan_inmobiliarias.dto.request.agency;

import com.manuel.zaguan_inmobiliarias.enums.agency.AgencyStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgencyRequest {
    @NotBlank
    @Size(min = 11, max = 13, message = "Cuit must be between 11 and 13 characters.")
    private String cuit;

    @NotBlank
    @Size(min = 3, max = 30, message = "Company name must be between 3 and 30 characters." )
    private String companyName;

    @NotBlank
    @Size(min = 3, max = 30, message = "Public name must be between 3 and 30 characters.")
    private String publicName;

    @NotBlank
    @Email
    @Size(min = 3, max = 20, message = "Email must be between 3 and 20 characters.")
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    private String password;

    @NotNull
    @Size(min = 8, max = 15, message = "Phone number must be between 8 and 15 characters.")
    private int phoneNumber;

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
