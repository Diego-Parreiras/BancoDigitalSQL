package br.com.bancodigital.model;

import br.com.bancodigital.model.enuns.TipoCliente;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cliente {

    private Long id;
    private String nome;
    private String cpf;
    private Endereco endereco;
    private String dataNascimento;
    private TipoCliente tipo;
    private List<Conta> contas;


}
