package br.com.bancodigital.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagamentoCartaoRequest {
    private double pagamento;
    private Long senha;
}
