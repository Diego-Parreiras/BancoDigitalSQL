package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.model.Cartao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class CartaoRowMapper implements RowMapper<Cartao> {

    @Override
    public Cartao mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cartao cartao = new Cartao();
        cartao.setId(rs.getLong("id"));
        cartao.setNumero(rs.getLong("numero"));
        cartao.setAtivoOuNao(rs.getBoolean("ativo_ou_nao"));
        cartao.setSenha(rs.getLong("senha"));
        cartao.setCvv(rs.getLong("cvv"));
        return cartao;
    }
}
