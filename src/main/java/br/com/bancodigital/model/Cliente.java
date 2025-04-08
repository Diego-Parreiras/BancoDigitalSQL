package br.com.bancodigital.model;

import br.com.bancodigital.model.enuns.TipoCliente;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;

    @OneToOne
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;
    private String dataNascimento;
    private TipoCliente tipo;

    @OneToMany(mappedBy = "cliente")
    @JsonManagedReference
    private List<Conta> contas;


}