package br.com.bancodigital.exception;


public class ContaException extends RuntimeException {

    public  ContaException(String message) {
        super(message);
    }

    public static  ContaException contaNaoEncontrada() {
        return new ContaException("Conta não encontrada.");
    }
    public static  ContaException contaOrigemNaoEncontrada() {
        return new ContaException("Conta origem não encontrada.");
    }
    public static  ContaException contaDestinoNaoEncontrada() {
        return new ContaException("Conta de destino não encontrada.");
    }

    public static  ContaException senhaIncorreta() {
        return new ContaException("Senha incorreta.");
    }

    public static  ContaException contaJaCadastrada() {
        return new ContaException("Conta já cadastrada.");
    }

    public static  ContaException contaComSaldo() {
        return new ContaException("Conta com saldo não pode ser fechada.");
    }

    public static  ContaException valorInvalido() {
        return new ContaException("O valor não pode ser negativo.");
    }

    public static  ContaException saldoInsuficiente() {
        return new ContaException("Saldo insuficiente.");
    }

    public static  ContaException contaNaoAplicavel() {
        return new ContaException("Tipo de conta não aplicável para esta operação.");
    }
}
