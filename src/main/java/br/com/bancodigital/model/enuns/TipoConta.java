package br.com.bancodigital.model.enuns;
public enum TipoConta {
    POUPANCA(0),
    CORRENTE(1);

    private final int tipo;

    TipoConta(int tipo) {
        this.tipo = tipo;
    }

    public int getValor() {
        return tipo;
    }

    public static TipoConta fromInt(int i) {
        for (TipoConta tc : values()) {
            if (tc.getValor() == i) {
                return tc;
            }
        }
        return null;
    }
}

