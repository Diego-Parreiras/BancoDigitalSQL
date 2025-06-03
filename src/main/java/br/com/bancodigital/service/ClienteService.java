package br.com.bancodigital.service;

import br.com.bancodigital.dao.daoimplements.ClienteDaoImplements;
import br.com.bancodigital.dao.interfaces.EnderecoDao;
import br.com.bancodigital.exception.JavaException;
import br.com.bancodigital.model.entity.Cliente;
import br.com.bancodigital.model.entity.Endereco;
import br.com.bancodigital.constantutils.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteDaoImplements clienteDao;
    @Autowired
    private EnderecoDao enderecoDao;

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @Transactional
    public void cadastrar(Cliente cliente) {
        logger.info(ServiceUtils.CADASTRANDO_CLIENTE);
        verificarDadosCliente(cliente);
        Long idEndereco = enderecoDao.save(cliente.getEndereco());
        cliente.getEndereco().setId(idEndereco);
        clienteDao.save(cliente);
        logger.info(ServiceUtils.CLIENTE_CADASTRADO);
    }

    public Cliente buscarId(Long id) {
        logger.info(ServiceUtils.BUSCANDO_CLIENTE);
        Optional<Cliente> cliente = clienteDao.findById(id);
        if (cliente.isPresent()) {
            logger.info(ServiceUtils.CLIENTE_ENCONTRADO);
            return cliente.get();
        }
        logger.info(ServiceUtils.NAO_ENCONTRADO);
        throw new JavaException(ServiceUtils.NAO_ENCONTRADO, HttpStatus.NOT_FOUND.value());
    }

    @Transactional
    public void atualizar(Long id, Cliente cliente) {
        logger.info(ServiceUtils.ATUALIZANDO_CLIENTE);
        validarNome(cliente.getNome());
        validarCpf(cliente.getCpf());
        validarDataDeNascimento(cliente.getDataNascimento());
        validarEndereco(cliente.getEndereco());
        cliente.setId(id);
        clienteDao.atualizar(cliente);
        logger.info(ServiceUtils.CLIENTE_ATUALIZADO);
    }


    @Transactional
    public void apagar(Long id) {
        logger.info(ServiceUtils.REMOVENDO_CLIENTE);
        Optional<Cliente> optional = clienteDao.findById(id);
        if (!optional.isPresent()) {
            logger.info(ServiceUtils.NAO_ENCONTRADO);
            throw new JavaException(ServiceUtils.NAO_ENCONTRADO, HttpStatus.NOT_FOUND.value());
        }
        logger.info(ServiceUtils.CLIENTE_REMOVIDO);
        clienteDao.delete(optional.get().getId());
    }

    public Object buscarTodos() {
        logger.info(ServiceUtils.BUSCANDO_TODOS);
        return clienteDao.findAll();
    }

    private void verificarDadosCliente(Cliente cliente) {
        logger.info(ServiceUtils.VALIDANDO_DADOS_CLIENTE);
        verificarClienteExiste(cliente);
        validarNome(cliente.getNome());
        validarCpf(cliente.getCpf());
        validarDataDeNascimento(cliente.getDataNascimento());
        validarEndereco(cliente.getEndereco());
    }

    private void verificarClienteExiste(Cliente cliente) {
        if (clienteDao.existsByCpf(cliente.getCpf())) {
            logger.info(ServiceUtils.CPF_INVALIDO);
            throw new JavaException(ServiceUtils.CPF_INVALIDO, HttpStatus.BAD_REQUEST.value());
        }
    }

    private void validarEndereco(Endereco endereco) {
        logger.info(ServiceUtils.VALIDANDO_DADOS_CLIENTE);
        String regexCep = "^\\d{5}-\\d{3}$";

        if ((endereco.getCidade() == null || endereco.getCidade().isEmpty()) ||
                (endereco.getEstado() == null || endereco.getEstado().isEmpty()) ||
                (endereco.getRua() == null || endereco.getRua().isEmpty()) ||
                (endereco.getNumero() == null || endereco.getNumero().isEmpty())) {
            logger.info(ServiceUtils.ENDERECO_INVALIDO);
            throw new JavaException(ServiceUtils.ENDERECO_INVALIDO, HttpStatus.BAD_REQUEST.value());
        }
        logger.info(ServiceUtils.SUCESSO);
    }

    private void validarDataDeNascimento(String data) {
        logger.info(ServiceUtils.VALIDANDO_DADOS_CLIENTE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        Date dataNascimentoDate = null;
        try {
            dataNascimentoDate = sdf.parse(data);
        } catch (Exception e) {
            logger.info(ServiceUtils.DATA_NASCIMENTO_INVALIDA);
            throw new JavaException(ServiceUtils.DATA_NASCIMENTO_INVALIDA, HttpStatus.BAD_REQUEST.value());
        }
        Date dataAtual = new Date();
        int idade = dataAtual.getYear() - dataNascimentoDate.getYear();
        if (dataAtual.getMonth() < dataNascimentoDate.getMonth() ||
                (dataAtual.getMonth() == dataNascimentoDate.getMonth() && dataAtual.getDay() < dataNascimentoDate.getDay())) {
            idade--;
        }

        if (idade < 18) {
            logger.info(ServiceUtils.IDADE_INVALIDA);
            throw new JavaException(ServiceUtils.IDADE_INVALIDA, HttpStatus.BAD_REQUEST.value());
        }
    }

    private void validarCpf(String CPF) {
        logger.info(ServiceUtils.VALIDANDO_DADOS_CLIENTE);
        String regex = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}";

        if (!CPF.matches(regex)) {
            throw new JavaException(ServiceUtils.CPF_INVALIDO, HttpStatus.BAD_REQUEST.value());
        }
        CPF = CPF.replace(".", "").replace("-", "");

        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            throw new JavaException(ServiceUtils.CPF_INVALIDO, HttpStatus.BAD_REQUEST.value());

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char) (r + 48);

            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char) (r + 48);

            if ((dig10 != CPF.charAt(9)) && (dig11 != CPF.charAt(10))) {
                logger.info(ServiceUtils.CPF_INVALIDO);
                throw new JavaException(ServiceUtils.CPF_INVALIDO, HttpStatus.BAD_REQUEST.value());
            }

        } catch (InputMismatchException erro) {
            logger.info(ServiceUtils.CPF_INVALIDO);
            throw new JavaException(ServiceUtils.CPF_INVALIDO, HttpStatus.BAD_REQUEST.value());
        }
    }

    private void validarNome(String nome) {
        String regex = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$";

        if (!nome.matches(regex)) {
            logger.info(ServiceUtils.NOME_APENAS_LETRAS);
            throw new JavaException(ServiceUtils.NOME_APENAS_LETRAS, HttpStatus.BAD_REQUEST.value());
        } else if (nome.length() < 2 || nome.length() > 100) {
            logger.info(ServiceUtils.NOME_TAMANHO_INVALIDO);
            throw new JavaException(ServiceUtils.NOME_TAMANHO_INVALIDO, HttpStatus.BAD_REQUEST.value());
        }
    }
}
