package br.com.bancodigital.constantutils;

public class ServiceUtils {

    public static final String SUCESSO = "Sucesso";

    //-------------------CONTA--------------------------//
    public static final String CONTA_NAO_ENCONTRADA = "A Conta não foi encontrada";
    public static final String CONTA_ORGEM_NAO_ENCONTRADA = "A Conta origem não foi encontrada";
    public static final String CONTA_DESTINO_NAO_ENCONTRADA = "A Conta destino não foi encontrada";
    public static final String SENHA_INCORRETA = "Senha incorreta";
    public static final String TRANSFERENCIA_REALIZADA_COM_SUCESSO = "Sua Transferencia foi realizada com sucesso";
    public static final String CONTA_COM_SALDO_NAO_PODE_SER_FECHADA = "Conta com saldo nao pode ser fechada, transfira ou saque o valor para fechar a conta";
    public static final String CONTA_FECHADA_COM_SUCESSO = "Sua Conta foi fechada com sucesso";
    public static final String CONTA_CADASTRADA_COM_SUCESSO = "Sua Conta foi cadastrada com sucesso";
    public static final String CADASTRANDO_CONTA = "Cadastrando sua Conta";
    public static final String SALDO_INSUFICIENTE = "Saldo Insuficiente";
    public static final String VALOR_NAO_PODE_SER_NEGATIVO = "O valor nao pode ser negativo";
    public static final String CONTA_JA_CADASTRADA = "Conta ja cadastrada";
    public static final String CONTA_ENCONTRADA = "Conta encontrada com sucesso";
    public static final String INICIANDO_BUSCA = "Iniciando Busca";
    public static final String INICIANDO_TRANSERENCIA = "Iniciando transferencia";
    public static final String CONTA_ORIGEM_ENCONTRADA = "Conta origem encontrada";
    public static final String CONTA_DESTINO_ENCONTRADA = "ContaOrigem não encontrada";
    public static final String FECHANDO_CONTA = "Fechando Conta";
    public static final String CONSULTANDO_SALDO = "Consultando Saldo";
    public static final String INICIANDO_DEPOSITO = "Iniciando Deposito";
    public static final String INICIANDO_SACAR = "Iniciando saque";
    public static final String APLICANDO_TAXA_MANUTENCAO = "Apliicando Taxa de Manutenção";
    public static final String CONTA_NAO_APLICAVEL = "Conta nao aplicavel";
    public static final String INICIANDO_TAXA_RENDIMENTO = "Iniciando Taxa de Rendimento";

    //--------------CLIENTE--------------------//
    public static final String CADASTRANDO_CLIENTE = "Cadastrando cliente";
    public static final String CLIENTE_CADASTRADO= "Cliente cadastrado com sucesso";
    public static final String NAO_ENCONTRADO = " Não encontrado";
    public static final String CLIENTE_ENCONTRADO = "Cliente encontrado";
    public static final String BUSCANDO_CLIENTE = "Iniciando busca do cliente";
    public static final String ATUALIZANDO_CLIENTE = "Atualizando cliente de ID ";
    public static final String CLIENTE_ATUALIZADO= "Cliente atualizado com sucesso";
    public static final String REMOVENDO_CLIENTE = "Removendo cliente de ID ";
    public static final String CLIENTE_REMOVIDO= "Cliente removido com sucesso";
    public static final String VALIDANDO_DADOS_CLIENTE = "Validando dados do cliente";
    public static final String CPF_INVALIDO = "CPF inválido";
    public static final String ENDERECO_INVALIDO = "Endereço inválido";
    public static final String DATA_NASCIMENTO_INVALIDA = "Data de nascimento inválida";
    public static final String CLIENTE_NAO_ATUALIZADO = "Cliente nao atualizado";
    public static final String BUSCANDO_TODOS = "Buscando todos os clientes";
    public static final String IDADE_INVALIDA =  "O cliente deve ter mais de 18 anos";
    public static final String NOME_APENAS_LETRAS = "O nome deve conter apenas letras";
    public static final String NOME_TAMANHO_INVALIDO = "O nome deve ter entre 2 e 100 caracteres.";

    //--------------CARTÃO----------------------//
    public static final String CARTAO_JA_CADASTRADO = "Cartao ja cadastrado";
    public static final String CADASTRANDO_CARTAO = "Iniciando cadastro";
    public static final String POPULANDO_CARTAO = "Completando dados do cartao";
    public static final String LIMITE_INSUFICIENTE = "Limite insuficiente";
    public static final String CARTAO_DE_DEBITO_SELECIONADO = "Cartao de Debito selecionado de ID ";
    public static final String CARTAO_DE_CREDITO_SELECIONADO = "Cartao de Credito selecionado de ID ";
    public static final String VALOR_NEGATIVO = "O valor nao pode ser negativo";
    public static final String VERIFICANDO_DADOS_DO_PAGAMENTO = "Verificando dados do pagamento";
    public static final String CARTAO_NAO_ENCONTRADO = "Cartao não encontrado";
    public static final String CARTAO_INATIVO = "Cartao inativo";
    public static final String FATURA_PENDENTE = "Fatura pendente";
    public static final String FATURA_JA_PAGA = "Fatura ja paga";
    public static final String CARTAO_NAO_E_CREDITO = "Seu cartao nao e de credito";
    public static final String CARTAO_NAO_E_DEBITO = "Seu cartao nao e de debito";
    public static final String SENHA_ATUALIZADA = "Senha atualizada com sucesso";
    public static final String STATUS_ALTERADO = "Status do cartao alterado com sucesso";
    public static final String PAGANDO_FATURA =  "Iniciando pagamento da fatura do cartao de ID " ;
    public static final String VERIFICANDO_DADOS_DO_CARTAO = "Verificando dados do cartao";
    public static final String BUSCANDO_FATURA = "Buscando fatura do cartao de ID " ;
    public static final String ATUALIZANDO_SENHA = "Atualizando senha do cartao de ID ";
    public static final String MUDANDO_STATUS = "Mudando status do cartao de ID" ;

    //--------------GERAL/ERROS----------------------//
    public static final String ERRO_AO_SALVAR= "Nao pode ser salvo";
    public static final String ERRO_AO_DELETAR = "Houve um erro ao deletar ";
    public static final String NAO_FOI_POSSIVEL_REALIZAR_ACAO = "Não foi possível realizar a ação";
    public static final String NENHUM_CARTAO_ENCONTRADO = "Nenhum cartão encontrado";
    public static final String ERRO_AO_SALVAR_ENDERECO = "Erro ao salvar endereço";
    public static final String ERRO_AO_SALVAR_TRANSFERENCIA = "Erro ao salvar transferencia";
    public static final String ERRO_AO_ATUALIZAR = "Erro ao atualizar o client e";

    void serviceUtils() {
        //ignorable
    }
}