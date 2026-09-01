package com.manuel.zaguan_inmobiliarias.dto.request.agency;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
public class AgencyUpdaterRequest {

    private String companyName;
    private String publicName;
    private String address;
    private String webURL;
    private String socials;
}
