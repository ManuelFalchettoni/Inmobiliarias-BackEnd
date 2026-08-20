package com.manuel.zaguan_inmobiliarias.dto.response.property.photo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PropertyPhotoResponse {
    private long id;

    private String url;

    private String photoName;

    private int position;


}
