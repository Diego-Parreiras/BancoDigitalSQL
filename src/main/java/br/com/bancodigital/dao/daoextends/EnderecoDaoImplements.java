package br.com.bancodigital.dao.daoextends;

import br.com.bancodigital.dao.interfaces.EnderecoDao;
import br.com.bancodigital.model.Endereco;
import br.com.bancodigital.model.rowmapper.EnderecoRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class EnderecoDaoImplements implements EnderecoDao {
    @Autowired
    private EnderecoRowMapper enderecoRowMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(Endereco endereco) {
        try{
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("rua", endereco.getRua())
                .addValue("numero", endereco.getNumero())
                .addValue("complemento", endereco.getComplemento())
                .addValue("cep", endereco.getCep())
                .addValue("cidade", endereco.getCidade())
                .addValue("estado", endereco.getEstado());
        jdbcTemplate.update(SqlUtils.SQL_ENDERECO_INSERT, sqlParameterSource);

    }catch (Exception e){
            throw new RuntimeException("Endereco nao cadastrado " + e.getMessage());     //fazer classe de erros com msgs
        }
    }
}
