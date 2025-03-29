package br.com.bancodigital.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Endereco {

    private String rua;
    private String numero;
    private String cep;
    private String cidade;
    private String estado;

}
