package br.com.bancodigital.dao.daoextends;

public class SqlUtils {

    public SqlUtils() {}
    /*================ClienteDAO=================*/
    public static final String SQL_CLIENTE_EXISTS_BY_CPF = "SELECT COUNT(*) FROM cliente WHERE cpf = ?";
    public static final String SQL_CLIENTE_SAVE = "INSERT INTO cliente (nome, cpf, data_nascimento) VALUES (?, ?, ?)";
    public static final String SQL_CLIENTE_FIND_BY_ID = "SELECT * FROM cliente WHERE id = ?";
    public static final String SQL_CLIENTE_FIND_ALL = "SELECT * FROM cliente";
    public static final String SQL_CLIENTE_DELETE = "DELETE FROM cliente WHERE id = ?";

    /*================CartaoDAO=================*/
    public static final String SQL_CARTAO_EXISTS_BY_NUMERO = "SELECT COUNT(*) FROM cartao WHERE numero = ?";
    public static final String SQL_CARTAO_FIND_BY_NUMERO = "SELECT * FROM cartao WHERE numero = ?";
    public static final String SQL_CARTAO_FIND_BY_ID = "SELECT * FROM cartao WHERE id = ?";
    public static final String SQL_CARTAO_SAVE = "INSERT INTO cartao (numero, ativo_ou_nao, senha, cvv, id_conta, tipo_cartao) VALUES (?, ?, ?, ?, ?, ?)";

    /*================ContaDAO=================*/



}
