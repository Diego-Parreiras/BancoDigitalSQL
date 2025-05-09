package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.model.Cartao;
import br.com.bancodigital.model.CartaoDeCredito;
import br.com.bancodigital.model.CartaoDeDebito;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CartaoRowMapper implements RowMapper<Cartao> {

    @Override
    public Cartao mapRow(ResultSet rs, int rowNum) throws SQLException {
        String tipo = rs.getString("tipo"); // precisa existir no banco
        Cartao cartao;

        if ("credito".equalsIgnoreCase(tipo)) {
            CartaoDeCredito credito = new CartaoDeCredito();
            credito.setLimiteCredito(rs.getDouble("limite_credito"));
            credito.setFatura(rs.getDouble("fatura"));
            cartao = credito;
        } else if ("debito".equalsIgnoreCase(tipo)) {
            CartaoDeDebito debito = new CartaoDeDebito();
            debito.setLimiteDiario(rs.getDouble("limite_diario"));
            cartao = debito;
        } else {
            throw new SQLException("Tipo de cartão desconhecido: " + tipo);
        }

        cartao.setId(rs.getLong("id"));
        cartao.setNumero(rs.getLong("numero"));
        cartao.setAtivoOuNao(rs.getBoolean("ativo_ou_nao"));
        cartao.setSenha(rs.getLong("senha"));
        cartao.setCvv(rs.getLong("cvv"));

        return cartao;
    }
}
