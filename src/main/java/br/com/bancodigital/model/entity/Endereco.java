package br.com.bancodigital.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Endereco implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String rua;
    private String numero;
    private String cep;
    private String complemento;
    private String cidade;
    private String estado;

}
