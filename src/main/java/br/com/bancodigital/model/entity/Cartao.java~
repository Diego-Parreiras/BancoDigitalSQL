package br.com.bancodigital.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cartao {
    private Long id;
    private Long numero;
    private boolean ativoOuNao;
    private Long senha;
    private Long cvv;

    @JsonBackReference
    private Conta conta;

}
