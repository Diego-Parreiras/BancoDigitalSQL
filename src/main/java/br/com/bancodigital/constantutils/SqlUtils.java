package br.com.bancodigital.constantutils;

public class SqlUtils {


    /*================ClienteDAO=================*/
    public static final String SQL_CLIENTE_EXISTS_BY_CPF = "SELECT * FROM cliente_exists_by_cpf_v1(?)";  // feito
    public static final String SQL_CLIENTE_SAVE = "CALL cliente_save_V (?, ?, ?, ?, ?)";                 // feito
    public static final String SQL_CLIENTE_FIND_BY_ID = "SELECT * FROM cliente_find_by_id_V1(?)";        // feito
    public static final String SQL_CLIENTE_FIND_ALL = "SELECT * FROM cliente_find_all_v1";               // feito
    public static final String SQL_CLIENTE_DELETE = "CALL apagar_cliente_V1(?)";                         // feito

    /*================ContaDAO=================*/
    public static final String SQL_CONTA_INSERT = "SELECT * FROM public.conta_save_v1(?, ?, ?, ?, ?, ?, ?)"; // feito
    public static final String SQL_CONTA_DELETE = "CALL public.apagar_conta_v1(?)";                          // feito
    public static final String SQL_CONTA_FIND_BY_ID = "SELECT * FROM conta_find_by_id_v1(?)";                // feito"
    public static final String SQL_CONTA_FIND_ALL_CARTOES = "SELECT * FROM conta_find_all_cartao(?)";        // feito
    public static final String SQL_CONTA_FIND_BY_PIX = "SELECT * FROM conta WHERE chave_pix = ?";
    public static final String SQL_CONTA_FIND_BY_AGENCIA_NUMERO = "SELECT * FROM conta WHERE agencia = ? AND numero = ?";
    public static final String SQL_CONTA_DEPOSITAR = "UPDATE conta SET saldo = saldo + ? WHERE id = ?";
    public static final String SQL_CONTA_SACAR =     "UPDATE conta SET saldo = saldo - ? WHERE id = ?";

    /*================CartaoDAO=================*/
    public static final String SQL_CARTAO_EXISTS_BY_NUMERO = "SELECT COUNT(*) FROM cartao WHERE numero = ?";
    public static final String SQL_CARTAO_FIND_BY_NUMERO = "SELECT * FROM cartao WHERE numero = ?";
    public static final String SQL_CARTAO_FIND_BY_ID = "SELECT * FROM cartao WHERE id = ?";
    public static final String SQL_CARTAO_SAVE = "INSERT INTO cartao (numero, ativo_ou_nao, senha, cvv, id_conta, tipo_cartao) VALUES (?, ?, ?, ?, ?, ?)";


    /*================TransferenciaDAO=================*/
    public static final String SQL_TRANSFERENCIA_SAVE = "INSERT INTO transferencia (data_transferencia,id_conta_destino,id_conta_origem,  valor) VALUES (?, ?, ?, ?)";

    /*================EnderecoDAO======================*/
    public static final String SQL_ENDERECO_INSERT = "INSERT INTO endereco (rua, numero, complemento, cep, cidade, estado) " + "VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SQL_BUSCAR_ID_ULTIMO_ENDERECO = "SELECT id FROM endereco ORDER BY id DESC LIMIT 1";


}
