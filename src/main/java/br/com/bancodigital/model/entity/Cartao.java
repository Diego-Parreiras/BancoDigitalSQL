package br.com.bancodigital.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cartao implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long numero;
    private boolean ativoOuNao;
    private Long senha;
    private Long cvv;

    @JsonBackReference
    private Conta conta;
    public short getTipoCartao() {
        if (this instanceof CartaoDeDebito) {
            return 1; // Debito
        } else if (this instanceof CartaoDeCredito) {
            return 2; // Credito
        } else {
            throw new IllegalArgumentException("Tipo de cartão desconhecido");
        }
    }



}
