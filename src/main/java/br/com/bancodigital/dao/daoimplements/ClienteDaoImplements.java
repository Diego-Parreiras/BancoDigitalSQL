package br.com.bancodigital.dao.daoimplements;

import br.com.bancodigital.dao.interfaces.ClienteDao;
import br.com.bancodigital.constantutils.SqlUtils;
import br.com.bancodigital.exception.JavaException;
import br.com.bancodigital.model.entity.Cliente;
import br.com.bancodigital.model.rowmapper.ClienteRowMapper;
import br.com.bancodigital.constantutils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return jdbcTemplate.queryForObject(SqlUtils.SQL_CLIENTE_EXISTS_BY_CPF, Boolean.class, cpf);
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
            String msg = e.getCause().getMessage();
            msg = msg.replace("ERRO", "");
            throw new JavaException(ServiceUtils.ERRO_AO_SALVAR + msg, HttpStatus.NOT_ACCEPTABLE.value());
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
            String msg = e.getCause().getMessage();
            msg = msg.replace("ERRO", "");
            throw new JavaException(ServiceUtils.ERRO_AO_DELETAR + msg, HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    @Override
    public void atualizar(Cliente cliente) {
        try {
            jdbcTemplate.update(SqlUtils.SQL_CLIENTE_UPDATE,
                    cliente.getId(),
                    cliente.getCpf(),
                    cliente.getDataNascimento(),
                    cliente.getNome(),
                    cliente.getTipo().getValor(),
                    cliente.getEndereco().getCep(),
                    cliente.getEndereco().getCidade(),
                    cliente.getEndereco().getComplemento(),
                    cliente.getEndereco().getEstado(),
                    cliente.getEndereco().getNumero(),
                    cliente.getEndereco().getRua()
            );
        } catch (Exception e) {
            String msg = e.getCause().getMessage();
            msg = msg.replace("ERRO", "");
            throw new JavaException(ServiceUtils.ERRO_AO_ATUALIZAR + msg, HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

}

