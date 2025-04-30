package br.com.bancodigital.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@DiscriminatorValue("CREDITO")
public class CartaoDeCredito extends Cartao{
    private double limiteCredito;
    private double fatura;
}
