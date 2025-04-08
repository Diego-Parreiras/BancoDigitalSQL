package br.com.bancodigital.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransferenciaPixRequest {
    private String chavePix;
    private Long idContaOrigem;
    private double valor;
    private Long senha;
}
