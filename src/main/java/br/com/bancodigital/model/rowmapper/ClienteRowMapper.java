package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.model.Cliente;
import br.com.bancodigital.model.Endereco;
import br.com.bancodigital.model.enuns.TipoCliente;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClienteRowMapper implements RowMapper<Cliente> {
    @Override
    public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setDataNascimento(rs.getString("data_nascimento"));


        cliente.setTipo(TipoCliente.fromInt(rs.getInt("tipo")));// transforma o int em enum


        cliente.setEndereco(new Endereco());
        cliente.getEndereco().setId(rs.getLong("id_endereco"));
        cliente.getEndereco().setCep(rs.getString("cep"));
        cliente.getEndereco().setCidade(rs.getString("cidade"));
        cliente.getEndereco().setComplemento(rs.getString("complemento"));
        cliente.getEndereco().setEstado(rs.getString("estado"));
        cliente.getEndereco().setNumero(rs.getString("numero"));
        cliente.getEndereco().setRua(rs.getString("rua"));
        return cliente;
    }
}
