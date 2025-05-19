package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.exception.JavaException;
import br.com.bancodigital.model.Cartao;
import br.com.bancodigital.model.CartaoDeCredito;
import br.com.bancodigital.model.CartaoDeDebito;
import br.com.bancodigital.service.utils.ServiceUtils;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CartaoRowMapper implements RowMapper<Cartao> {

    @Override
    public Cartao mapRow(ResultSet rs, int rowNum) throws SQLException {
        String tipo = rs.getString("tipo_cartao");
        Cartao cartao;

        if ("CartaoDeCredito".equalsIgnoreCase(tipo)) {
            CartaoDeCredito credito = new CartaoDeCredito();
            credito.setLimiteCredito(rs.getDouble("limite_credito"));
            credito.setFatura(rs.getDouble("fatura"));
            cartao = credito;
        } else if ("CartaoDeDebito".equalsIgnoreCase(tipo)) {
            CartaoDeDebito debito = new CartaoDeDebito();
            debito.setLimiteDiario(rs.getDouble("limite_diario"));
            cartao = debito;
        } else {
            throw new JavaException(ServiceUtils.CARTAO_NAO_ENCONTRADO + HttpStatus.NOT_FOUND.value());
        }

        cartao.setId(rs.getLong("id"));
        cartao.setAtivoOuNao(rs.getBoolean("ativo_ou_nao"));
        cartao.setCvv(rs.getLong("cvv"));
        cartao.setNumero(rs.getLong("numero"));
        cartao.setSenha(rs.getLong("senha"));
        return cartao;
    }
}
