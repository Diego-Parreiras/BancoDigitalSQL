package br.com.bancodigital.dao.daoimplements;

import br.com.bancodigital.dao.interfaces.CartaoDao;
import br.com.bancodigital.dao.utils.SqlUtils;
import br.com.bancodigital.exception.JavaException;
import br.com.bancodigital.model.Cartao;
import br.com.bancodigital.model.rowmapper.CartaoRowMapper;
import br.com.bancodigital.service.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.smartcardio.CardException;
import java.util.Optional;

@Repository
public class CartaoDaoImplements implements CartaoDao {
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
        try {
            jdbcTemplate.update(SqlUtils.SQL_CARTAO_SAVE,
                    cartao.getNumero(),
                    cartao.isAtivoOuNao(),
                    cartao.getSenha(),
                    cartao.getCvv(),
                    cartao.getConta().getId(),
                    cartao.getClass().getSimpleName());

        } catch (Exception e) {
            throw new JavaException(ServiceUtils.ERRO_AO_SALVAR, HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

}
