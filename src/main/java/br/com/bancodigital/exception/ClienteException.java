package br.com.bancodigital.exception;


public class ClienteException extends RuntimeException {
    public ClienteException(String mensagem) {
        super(mensagem);
    }

    public static ClienteException clienteNaoEncontrado() {
        return new ClienteException("Cliente não encontrado");
    }

    public static ClienteException cpfJaCadastrado() {
        return new ClienteException("CPF já cadastrado");
    }

    public static ClienteException enderecoInvalido() {
        return new ClienteException("Endereço inválido");
    }

    public static ClienteException formatoDataInvalido() {
        return new ClienteException("Formato de data inválido");
    }

    public static ClienteException menorDeIdade() {
        return new ClienteException("O cliente deve ter mais de 18 anos");
    }

    public static ClienteException cpfInvalido() {
        return new ClienteException("CPF inválido");
    }

    public static ClienteException formatoCpfInvalido() {
        return new ClienteException("Formato de CPF inválido");
    }

    public static ClienteException nomeInvalido() {
        return new ClienteException("O nome deve conter apenas letras.");
    }

    public static ClienteException nomeTamanhoInvalido() {
        return new ClienteException("O nome deve ter entre 2 e 100 caracteres.");
    }

    public static ClienteException clienteNaoAtualizado(String detalhe) {
        return new ClienteException("Cliente não atualizado: " + detalhe);
    }
}

