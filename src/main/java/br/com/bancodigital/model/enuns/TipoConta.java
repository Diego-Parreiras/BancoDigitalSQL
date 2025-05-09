package br.com.bancodigital.model.enuns;

public enum TipoConta {
    POUPANCA(0),
    CORRENTE(1);
    private int tipo;

    TipoConta(int i) {
    }
    public int getValor() {
        return tipo;
    }

    public static TipoConta fromInt(int i) {
        for(TipoConta tc : values()) {
            if(tc.getValor() == i) {
                return tc;
            }
        }
        return null;
    }
}
