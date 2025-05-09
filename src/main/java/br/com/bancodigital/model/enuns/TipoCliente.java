package br.com.bancodigital.model.enuns;

public enum TipoCliente {
    COMUM(0),
    SUPER(1),
    PREMIUM(2);
    private int tipo;

    TipoCliente(int i) {
    }

    public int getValor() {
        return tipo;
    }

    public static TipoCliente fromInt(int i) {
        for(TipoCliente tc : values()) {
            if(tc.getValor() == i) {
                return tc;
            }
        }
        return null;
    }
}
