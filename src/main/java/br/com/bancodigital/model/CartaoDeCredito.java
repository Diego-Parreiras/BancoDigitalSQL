package br.com.bancodigital.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartaoDeCredito extends Cartao{
    private double limiteCredito;
    private double fatura;
}
