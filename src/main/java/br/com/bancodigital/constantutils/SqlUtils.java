package br.com.bancodigital.constantutils;

public class SqlUtils {


    private SqlUtils() {
    }

    /*================ClienteDAO=================*/
    public static final String SQL_CLIENTE_EXISTS_BY_CPF = "SELECT * FROM cliente_exists_by_cpf_v1(?)";
    public static final String SQL_CLIENTE_SAVE = "CALL public.cliente_save_v1(?, ?, ?, ?, ?)";
    public static final String SQL_CLIENTE_FIND_BY_ID = "SELECT * FROM cliente_find_by_id_v1(?)";
    public static final String SQL_CLIENTE_FIND_ALL = "SELECT * FROM cliente_find_all_v1()";
    public static final String SQL_CLIENTE_DELETE = "CALL apagar_cliente_v1(?)";
    public static final String SQL_CLIENTE_UPDATE = "CALL atualizar_cliente_v1(?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?)";

    /*================ContaDAO=================*/
    public static final String SQL_CONTA_INSERT = "CALL public.conta_save_v1(?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_CONTA_DELETE = "CALL public.apagar_conta_v1(?)";
    public static final String SQL_CONTA_FIND_BY_ID = "SELECT * FROM conta_find_by_id_v1(?)";
    public static final String SQL_CONTA_FIND_ALL_CARTOES =" SELECT * FROM conta_find_all_cartao_v1(?)";
    public static final String SQL_CONTA_FIND_BY_PIX = "SELECT * FROM conta_por_chave_pix_v1(?)";
    public static final String SQL_CONTA_FIND_BY_AGENCIA_NUMERO = "SELECT * FROM conta_find_by_agencia_and_numero_v1(?,?);";
    public static final String SQL_CONTA_DEPOSITAR = "CALL conta_depositar_v1(?,?)";
    public static final String SQL_CONTA_SACAR = "CALL conta_sacar_v1(?,?);";
    public static final String SQL_APLICAR_TAXA_MANUTENCAO = "CALL conta_aplicar_taxa_manutencao_v1(?)";
    public static final String SQL_APLICAR_TAXA_RENDIMENTO = "CALL conta_aplicar_taxa_rendimento_v1(?)";
    // feito
    /*================CartaoDAO=================*/
    public static final String SQL_CARTAO_EXISTS_BY_NUMERO = "SELECT cartao_existe_por_numero_v1(?)";
    public static final String SQL_CARTAO_FIND_BY_NUMERO = "SELECT * FROM cartao_por_numero_v1(?)";
    public static final String SQL_CARTAO_FIND_BY_ID = "SELECT * FROM cartao_por_id_v1(?)";
    public static final String SQL_CARTAO_SAVE = "CALL cartao_inserir_v1(?,?,?,?,?,?)";
    public static final String SQL_CARTAO_MUDAR_STATUS = "CALL cartao_mudar_status_v1(?)";
    public static final String SQL_CARTAO_BUSCAR_FATURA = "SELECT * FROM cartao_buscar_fatura_v1(?)";
    public static final String SQL_CARTAO_PAGAR = "CALL cartao_pagamento_v1(?, ? ,?)";
    public static final String SQL_CARTAO_PAGAR_FATURA = "CALL cartao_pagar_fatura_v1(?, ?)";
    public static final String SQL_CARTAO_ATUALIZAR_SENHA = "CALL cartao_atualizar_senha_v1(?, ?)";
    public static final String SQL_CARTAO_AUMENTAR_LIMITE_CREDITO = "CALL cartao_aumentar_limite_credito_v1(?,?)";
    public static final String SQL_CARTAO_AUMENTAR_LIMITE_DEBITO = "CALL cartao_aumentar_limite_debito_v1(?, ?)";


    /*================TransferenciaDAO=================*/
    public static final String SQL_TRANSFERENCIA_SAVE = "SELECT * FROM transferencia_executar_v1(?, ?, ?)";

    /*================EnderecoDAO======================*/
    public static final String SQL_ENDERECO_INSERT = "CALL endereco_inserir_v1(?, ?, ?, ?, ?, ?);";
    public static final String SQL_BUSCAR_ID_ULTIMO_ENDERECO = "SELECT endereco_ultimo_id_v1();";


}
