package br.com.bancodigital.dao.daoimplements;

import br.com.bancodigital.dao.interfaces.EnderecoDao;
import br.com.bancodigital.constantutils.SqlUtils;
import br.com.bancodigital.exception.JavaException;
import br.com.bancodigital.model.entity.Endereco;
import br.com.bancodigital.constantutils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EnderecoDaoImplements implements EnderecoDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Long save(Endereco endereco) {
        try {
            jdbcTemplate.update(
                    SqlUtils.SQL_ENDERECO_INSERT,
                    endereco.getRua(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getCep(),
                    endereco.getCidade(),
                    endereco.getEstado()
            );
            return jdbcTemplate.queryForObject(SqlUtils.SQL_BUSCAR_ID_ULTIMO_ENDERECO, Long.class);

        } catch (Exception e) {
            throw new JavaException(ServiceUtils.ERRO_AO_SALVAR_ENDERECO, HttpStatus.NOT_ACCEPTABLE.value());
        }
    }
}
