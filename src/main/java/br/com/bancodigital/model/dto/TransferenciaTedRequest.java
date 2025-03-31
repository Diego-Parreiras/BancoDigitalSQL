package br.com.bancodigital.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransferenciaTedRequest {
    private long idOrigem;
    private long agenciaDestino;
    private long numeroContaDestino;
    private double valor;
    private long senha;

}
