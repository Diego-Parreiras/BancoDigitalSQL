package br.com.bancodigital.model.entity;

import br.com.bancodigital.model.enuns.TipoConta;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Conta implements Serializable {
    private static final long serialVersionUID =1L;
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
