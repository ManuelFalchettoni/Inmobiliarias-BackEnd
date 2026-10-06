package com.manuel.zaguan_inmobiliarias.dto.request.crm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//Mismo body para POST y PUT. El lead sale de la URL, no del body
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmAlertRequest {
    //El agente al que le llega el recordatorio
    @NotNull
    private Long userId;

    //Sin @Size: la columna es TEXT
    @NotBlank
    private String message;

    @NotNull
    private LocalDateTime alertDate;

    //Boolean y no boolean: con el primitivo Lombok arma isRead() y el JSON sale como "read"
    @NotNull
    private Boolean isRead;
}
