package br.com.bancodigital.dao.daoextends;

import br.com.bancodigital.dao.interfaces.CartaoDao;
import br.com.bancodigital.model.Cartao;
import br.com.bancodigital.model.rowmapper.CartaoRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Optional;

public class CartaoDaoExtends implements CartaoDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CartaoRowMapper cartaoRowMapper;

    @Override
    public boolean existsByNumero(Long numero) {
        Integer count = jdbcTemplate.queryForObject(SqlUtils.SQL_CARTAO_EXISTS_BY_NUMERO, Integer.class, numero);
        return count != null && count > 0;
    }

    @Override
    public Optional<Cartao> findByNumero(Long numero) {
        try {
            Cartao cartao = jdbcTemplate.queryForObject(SqlUtils.SQL_CARTAO_FIND_BY_NUMERO, cartaoRowMapper, numero);
            return Optional.of(cartao);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Cartao> findById(Long id) {
            Cartao cartao = jdbcTemplate.queryForObject(SqlUtils.SQL_CARTAO_FIND_BY_ID, cartaoRowMapper, id);
            return Optional.of(cartao);

    }

    @Override
    public void save(Cartao cartao) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                        .addValue("numero", cartao.getNumero())
                        .addValue("ativo_ou_nao", cartao.isAtivoOuNao())
                        .addValue("senha", cartao.getSenha())
                        .addValue("cvv", cartao.getCvv())
                        .addValue("id_conta", cartao.getConta().getId())
                        .addValue("tipo_cartao", cartao.getClass().getSimpleName());

        jdbcTemplate.update(SqlUtils.SQL_CARTAO_SAVE, sqlParameterSource);
    }

}
