package br.com.bancodigital.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Endereco {
    private Long id;
    private String rua;
    private String numero;
    private String cep;
    private String complemento;
    private String cidade;
    private String estado;

}
