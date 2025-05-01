package br.com.bancodigital.service;

import br.com.bancodigital.dao.interfaces.ClienteDao;
import br.com.bancodigital.dao.interfaces.EnderecoDao;
import br.com.bancodigital.model.Cliente;
import br.com.bancodigital.model.Endereco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteDao clienteDao;
    @Autowired
    private EnderecoDao enderecoDao;

    public void cadastrar(Cliente cliente) {
        verificarDadosCliente(cliente);
        clienteDao.save(cliente);
    }

    private void verificarDadosCliente(Cliente cliente) {
        /*Verifica os dados do cliente estao compativel*/
        verificarClienteExiste(cliente);
        validarNome(cliente.getNome());
        validarCpf(cliente.getCpf());
        validarDataDeNascimento(cliente.getDataNascimento());
        validarEndereco(cliente.getEndereco());
    }

    private void verificarClienteExiste(Cliente cliente) {
        if (clienteDao.existsByCpf(cliente.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
    }

    private void validarEndereco(Endereco endereco) {
        /*Verifica o endereco do cliente*/
        String regexCep = "^\\d{5}-\\d{3}$";

        if ((endereco.getCidade() == null || endereco.getCidade().isEmpty()) ||
                (endereco.getEstado() == null || endereco.getEstado().isEmpty()) ||
                (endereco.getRua() == null || endereco.getRua().isEmpty()) ||
                (endereco.getNumero() == null || endereco.getNumero().isEmpty())) {

            throw new RuntimeException("Endereço inválido");
        }
        enderecoDao.save(endereco);
    }

    private void validarDataDeNascimento(String data) {
        /*Valida se o cliente e maior de idade*/
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        Date dataNascimentoDate = null;
        try {
            dataNascimentoDate = sdf.parse(data);
        } catch (Exception e) {
            throw new RuntimeException("Formato de data inválido");
        }
        Date dataAtual = new Date();
        int idade = dataAtual.getYear() - dataNascimentoDate.getYear();
        if (dataAtual.getMonth() < dataNascimentoDate.getMonth() ||
                (dataAtual.getMonth() == dataNascimentoDate.getMonth() && dataAtual.getDay() < dataNascimentoDate.getDay())) {
            idade--;
        }

        if (idade < 18) {
            throw new RuntimeException("O cliente deve ter mais de 18 anos");
        }
    }

    private void validarCpf(String CPF) {
        /*Validador de cpf com regex para formatar o cpf*/
        String regex = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}";

        if (!CPF.matches(regex)) {
            throw new RuntimeException("Formato de CPF inválido");
        }
        CPF = CPF.replace(".", "").replace("-", "");

        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            throw new RuntimeException("CPF inválido");

        char dig10, dig11;
        int sm, i, r, num, peso;


        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere "0" no inteiro 0
                // (48 eh a posicao de "0" na tabela ASCII)
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char) (r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
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

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 != CPF.charAt(9)) && (dig11 != CPF.charAt(10)))
                throw new RuntimeException("CPF inválido");

        } catch (InputMismatchException erro) {
            throw new RuntimeException("CPF inválido");
        }
    }

    public static String imprimeCPF(String CPF) {
        return (CPF.substring(0, 3) + "." + CPF.substring(3, 6) + "." +
                CPF.substring(6, 9) + "-" + CPF.substring(9, 11));
    }

    private void validarNome(String nome) {
        /*Valida se o nome tem apenas letras*/
        String regex = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$";

        if (!nome.matches(regex)) {
            throw new RuntimeException("O nome deve conter apenas letras.");
        } else if (nome.length() < 2 || nome.length() > 100) {
            throw new RuntimeException("O nome deve ter entre 2 e 100 caracteres.");
        }
    }

    public Cliente buscarId(Long id) {
        Optional<Cliente> cliente = clienteDao.findById(id);
        if (cliente.isPresent()) {
            return cliente.get();
        }
        throw new RuntimeException("Cliente não encontrado");
    }

    public void atualizar(Long id, Cliente cliente) {
        /*Atualiza os dados do cliente*/
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
        } catch (Exception e) {
            throw new RuntimeException("Cliente nao atualizado " + e.getMessage());     //fazer classe de erros com msgs
        }


    }

    public void apagar(Long id) {
        /*Verifica se o cliente existe para entao deletar*/
        Optional<Cliente> optional = clienteDao.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("Cliente não encontrado");
        }

        clienteDao.delete(optional.get().getId());
    }

    public Object buscarTodos() {
        return clienteDao.findAll();
    }
}
