package br.com.bancodigital.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transferencia implements Serializable {
    private static final long serialVersionUID = 1L;  // numero de serial para caso de futuras alterações
    private Long id;
    private Long idContaOrigem;
    private Long idContaDestino;
    private double valor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataTransferencia;

}
