package br.com.bancodigital.model;

import br.com.bancodigital.model.enuns.TipoConta;
import jakarta.persistence.*;
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
    private long senha;
    private double saldo;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    private String chavePix;
    private TipoConta tipoConta;
}
