package br.com.bancodigital.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Conta {
    private long numero;
    private long agencia;
    private String senha;
    private double saldo;
    private long id;
    private long idCliente;
    private String chavePix;
}
