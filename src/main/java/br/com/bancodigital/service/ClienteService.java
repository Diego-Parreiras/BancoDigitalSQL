package br.com.bancodigital.service;

import br.com.bancodigital.dao.daoimplements.ClienteDaoImplements;
import br.com.bancodigital.dao.interfaces.EnderecoDao;
import br.com.bancodigital.exception.ClienteException;
import br.com.bancodigital.model.Cliente;
import br.com.bancodigital.model.Endereco;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        logger.info("Iniciando cadastro de cliente");
        verificarDadosCliente(cliente);
        Long idEndereco = enderecoDao.save(cliente.getEndereco());
        cliente.getEndereco().setId(idEndereco);
        clienteDao.save(cliente);
        logger.info("Cliente cadastrado com sucesso");
    }

    public Cliente buscarId(Long id) {
        logger.info("Buscando cliente");
        Optional<Cliente> cliente = clienteDao.findById(id);
        if (cliente.isPresent()) {
            logger.info("Cliente encontrado");
            return cliente.get();
        }
        logger.info("Cliente nao encontrado");
        throw ClienteException.clienteNaoEncontrado();
    }

    @Transactional
    public void atualizar(Long id, Cliente cliente) {
        logger.info("Atualizando cliente");
        Cliente clienteAtualizar = buscarId(id);
        try {
            validarNome(cliente.getNome());
            validarCpf(cliente.getCpf());
            validarDataDeNascimento(cliente.getDataNascimento());
            validarEndereco(cliente.getEndereco());
            clienteAtualizar.setNome(cliente.getNome());
            clienteAtualizar.setCpf(cliente.getCpf());
            clienteAtualizar.setDataNascimento(cliente.getDataNascimento());
            clienteAtualizar.setEndereco(cliente.getEndereco());
            clienteDao.save(clienteAtualizar);
            logger.info("Cliente atualizado com sucesso");
        } catch (Exception e) {
            logger.info("Cliente nao atualizado " + e.getMessage());
            throw ClienteException.clienteNaoAtualizado(e.getMessage());
        }
    }

    @Transactional
    public void apagar(Long id) {
        logger.info("Deletando cliente");
        Optional<Cliente> optional = clienteDao.findById(id);
        if (!optional.isPresent()) {
            logger.info("Cliente nao encontrado");
            throw ClienteException.clienteNaoEncontrado();
        }
        logger.info("Cliente deletado com sucesso");
        clienteDao.delete(optional.get().getId());
    }

    public Object buscarTodos() {
        logger.info("Buscando todos os clientes");
        return clienteDao.findAll();
    }

    private void verificarDadosCliente(Cliente cliente) {
        logger.info("Verificando dados do cliente");
        verificarClienteExiste(cliente);
        validarNome(cliente.getNome());
        validarCpf(cliente.getCpf());
        validarDataDeNascimento(cliente.getDataNascimento());
        validarEndereco(cliente.getEndereco());
    }

    private void verificarClienteExiste(Cliente cliente) {
        if (clienteDao.existsByCpf(cliente.getCpf())) {
            logger.info("CPF já cadastrado");
            throw ClienteException.cpfJaCadastrado();
        }
    }

    private void validarEndereco(Endereco endereco) {
        logger.info("Validando endereço");
        String regexCep = "^\\d{5}-\\d{3}$";

        if ((endereco.getCidade() == null || endereco.getCidade().isEmpty()) ||
                (endereco.getEstado() == null || endereco.getEstado().isEmpty()) ||
                (endereco.getRua() == null || endereco.getRua().isEmpty()) ||
                (endereco.getNumero() == null || endereco.getNumero().isEmpty())) {
            logger.info("Endereço inválido");
            throw ClienteException.enderecoInvalido();
        }
        logger.info("Endereço válido");
    }

    private void validarDataDeNascimento(String data) {
        logger.info("Validando data de nascimento");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        Date dataNascimentoDate = null;
        try {
            dataNascimentoDate = sdf.parse(data);
        } catch (Exception e) {
            logger.info("Formato de data inválido");
            throw ClienteException.formatoDataInvalido();
        }
        Date dataAtual = new Date();
        int idade = dataAtual.getYear() - dataNascimentoDate.getYear();
        if (dataAtual.getMonth() < dataNascimentoDate.getMonth() ||
                (dataAtual.getMonth() == dataNascimentoDate.getMonth() && dataAtual.getDay() < dataNascimentoDate.getDay())) {
            idade--;
        }

        if (idade < 18) {
            logger.info("O cliente deve ter mais de 18 anos");
            throw ClienteException.menorDeIdade();
        }
    }

    private void validarCpf(String CPF) {
        logger.info("Validando CPF");
        String regex = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}";

        if (!CPF.matches(regex)) {
            throw ClienteException.formatoCpfInvalido();
        }
        CPF = CPF.replace(".", "").replace("-", "");

        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            throw ClienteException.cpfInvalido();

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
                logger.info("CPF inválido");
                throw ClienteException.cpfInvalido();
            }

        } catch (InputMismatchException erro) {
            logger.info("CPF inválido");
            throw ClienteException.cpfInvalido();
        }
    }

    private void validarNome(String nome) {
        String regex = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$";

        if (!nome.matches(regex)) {
            logger.info("O nome deve conter apenas letras.");
            throw ClienteException.nomeInvalido();
        } else if (nome.length() < 2 || nome.length() > 100) {
            logger.info("O nome deve ter entre 2 e 100 caracteres.");
            throw ClienteException.nomeTamanhoInvalido();
        }
    }
}
