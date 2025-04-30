package br.com.bancodigital.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@DiscriminatorValue("DEBITO")
public class CartaoDeDebito extends Cartao{
    private double limiteDiario;
}
