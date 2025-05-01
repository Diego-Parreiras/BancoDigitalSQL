package br.com.bancodigital.dao.daoextends;

import br.com.bancodigital.dao.interfaces.EnderecoDao;
import br.com.bancodigital.model.Endereco;
import br.com.bancodigital.model.rowmapper.EnderecoRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class EnderecoDaoExtends implements EnderecoDao {
    @Autowired
    private EnderecoRowMapper enderecoRowMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(Endereco endereco) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("rua", endereco.getRua())
                .addValue("numero", endereco.getNumero())
                .addValue("cep", endereco.getCep())
                .addValue("complemento", endereco.getComplemento())
                .addValue("cidade", endereco.getCidade())
                .addValue("estado", endereco.getEstado());
        jdbcTemplate.update(SqlUtils.SQL_ENDERECO_INSERT, sqlParameterSource);

    }
}
