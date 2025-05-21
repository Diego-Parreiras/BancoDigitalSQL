package br.com.bancodigital.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartaoDeDebito extends Cartao {
    private double limiteDiario;
}
