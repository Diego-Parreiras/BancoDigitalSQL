package br.com.bancodigital.dao.daoimplements;

import br.com.bancodigital.dao.interfaces.ClienteDao;
import br.com.bancodigital.dao.utils.SqlUtils;
import br.com.bancodigital.model.Cliente;
import br.com.bancodigital.model.rowmapper.ClienteRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class ClienteDaoImplements implements ClienteDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ClienteRowMapper clienteRowMapper;

    @Override
    public boolean existsByCpf(String cpf) {
        Integer count = jdbcTemplate.queryForObject(SqlUtils.SQL_CLIENTE_EXISTS_BY_CPF, Integer.class, cpf);
        return count != null && count > 0;
    }

    @Override
    public void save(Cliente cliente) {
        try {
            jdbcTemplate.update(SqlUtils.SQL_CLIENTE_SAVE,
                    cliente.getNome(),
                    cliente.getCpf(),
                    cliente.getDataNascimento(),
                    cliente.getEndereco().getId(),
                    cliente.getTipo().getValor()
                    );
        } catch (Exception e) {
            throw new RuntimeException("Cliente nao cadastrado " + e.getMessage());     //fazer classe de erros com msgs
        }
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        Cliente cliente = jdbcTemplate.queryForObject(SqlUtils.SQL_CLIENTE_FIND_BY_ID, clienteRowMapper, id);
        return Optional.of(cliente);
    }

    @Override
    public List<Cliente> findAll() {
        return jdbcTemplate.query(SqlUtils.SQL_CLIENTE_FIND_ALL, clienteRowMapper);
    }

    @Override
    public void delete(Long id) {
        try {
            jdbcTemplate.update(SqlUtils.SQL_CLIENTE_DELETE, id);
        } catch (Exception e) {
            throw new RuntimeException("Cliente nao deletado " + e.getMessage());     //fazer classe de erros com msgs
        }
    }
}
