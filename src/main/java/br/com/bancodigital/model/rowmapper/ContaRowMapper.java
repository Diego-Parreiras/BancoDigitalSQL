package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.model.Conta;
import br.com.bancodigital.model.enuns.TipoConta;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class ContaRowMapper implements RowMapper<Conta> {
    @Override
    public Conta mapRow(ResultSet rs, int rowNum) throws SQLException {
        Conta conta = new Conta();
        conta.setId(rs.getLong("id"));
        conta.setAgencia(rs.getLong("agencia"));
        conta.setChavePix(rs.getString("chave_pix"));
        conta.setNumero(rs.getLong("numero"));
        conta.setSaldo(rs.getDouble("saldo"));
        conta.setSenha(rs.getLong("senha"));
        conta.setTipoConta(TipoConta.fromInt(rs.getInt("tipo_conta")));
        conta.setListaCartoes(rs.getLong("lista_cartoes"));
        conta.getListaCartoes().set(rs.getLong("id"));
        conta.getListaCartoes().geAtivoOuNao(rs.getBoolean("ativo_ou_nao"));
        conta.getListaCartoes().setCvv(rs.getLong("cvv"));
        conta.getListaCartoes().setLimiteCredito(rs.getDouble("limite_credito"));
        conta.getListaCartoes().setLimiteDiario(rs.getDouble("limite_diario"));
        conta.getListaCartoes().setNumero(rs.getLong("numero"));
        conta.getListaCartoes().setSenha(rs.getLong("senha"));
        conta.getListaCartoes().setTipoCartao(rs.getString("tipo_cartao"));
        conta.getListaCartoes().setFatura(rs.getDouble("fatura"));




        return conta;
    }
}
