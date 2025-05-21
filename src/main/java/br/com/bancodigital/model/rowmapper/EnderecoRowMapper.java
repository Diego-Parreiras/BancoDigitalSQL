package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.model.entity.Endereco;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class EnderecoRowMapper implements RowMapper<Endereco> {
    @Override
    public Endereco mapRow(ResultSet rs, int rowNum) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setId(rs.getLong("id"));
        endereco.setCep(rs.getString("cep"));
        endereco.setCidade(rs.getString("cidade"));
        endereco.setComplemento(rs.getString("complemento"));
        endereco.setEstado(rs.getString("estado"));
        endereco.setNumero(rs.getString("numero"));
        endereco.setRua(rs.getString("rua"));
        return endereco;
    }
}
