package br.com.bancodigital.model;

import br.com.bancodigital.model.enuns.TipoConta;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Conta {
    private Long numero;
    private Long agencia;
    private Long senha;
    private double saldo;
    private Long id;

    @JsonBackReference
    private Cliente cliente;
    private String chavePix;
    private TipoConta tipoConta;

    @JsonManagedReference
    private List<Cartao> listaCartoes;
}
