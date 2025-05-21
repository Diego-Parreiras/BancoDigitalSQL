package br.com.bancodigital.service;

import br.com.bancodigital.dao.daoimplements.ContaDaoImplements;
import br.com.bancodigital.dao.daoimplements.TransferenciaDaoImplements;
import br.com.bancodigital.dao.interfaces.ContaDao;
import br.com.bancodigital.exception.JavaException;
import br.com.bancodigital.model.entity.Conta;
import br.com.bancodigital.model.entity.Transferencia;
import br.com.bancodigital.model.dto.TransferenciaPixRequest;
import br.com.bancodigital.model.dto.TransferenciaTedRequest;
import br.com.bancodigital.model.enuns.TipoCliente;
import br.com.bancodigital.model.enuns.TipoConta;
import br.com.bancodigital.constantutils.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class ContaService {
    @Autowired
    private final ContaDao contaDao = new ContaDaoImplements();
    @Autowired
    private final TransferenciaDaoImplements transferenciaDao = new TransferenciaDaoImplements();

    private final Logger logger = LoggerFactory.getLogger(ContaService.class);
    private final Random random = new Random();

    @Transactional
    public void criarConta(Conta conta) {
        try {
            logger.info(ServiceUtils.CADASTRANDO_CONTA);
            verificarContaExiste(conta);
            popularDadosConta(conta);
            contaDao.save(conta);
            logger.info(ServiceUtils.CONTA_CADASTRADA_COM_SUCESSO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public Transferencia transferenciaPix(TransferenciaPixRequest request) {
        /*verifica se o valor existe e se a conta origem existe
         * e se existir faz a transferencia*/
        logger.info(ServiceUtils.INICIANDO_TRANSERENCIA);
        varificarValor(request.getValor());
        Conta contaOrigem = buscarContaPorId(request.getIdContaOrigem());
        if (contaOrigem != null) {
            logger.info(ServiceUtils.CONTA_ORIGEM_ENCONTRADA);
            verificarSaldo(contaOrigem.getSaldo(), request.getValor());

            /*verdica se a conta destino existe*/
            Conta contaDestino = buscarContaPorId(buscarIdConta(request.getChavePix()));
            if (contaDestino != null) {

                if (!contaOrigem.getSenha().equals(request.getSenha())) {
                    logger.info(ServiceUtils.SENHA_INCORRETA);
                    throw new JavaException(ServiceUtils.SENHA_INCORRETA, HttpStatus.UNAUTHORIZED.value());
                }
                sacar(contaOrigem.getId(), request.getValor());
                depositar(contaDestino.getId(), request.getValor());
                logger.info(ServiceUtils.TRANSFERENCIA_REALIZADA_COM_SUCESSO);
                return registrarTransferencia(request.getIdContaOrigem(), contaDestino.getId(), request.getValor());
            }
            logger.info(ServiceUtils.CONTA_DESTINO_NAO_ENCONTRADA);
            throw new JavaException(ServiceUtils.CONTA_DESTINO_NAO_ENCONTRADA, HttpStatus.NOT_FOUND.value());
        }
        logger.info(ServiceUtils.CONTA_DESTINO_ENCONTRADA);
        throw new JavaException(ServiceUtils.CONTA_DESTINO_NAO_ENCONTRADA, HttpStatus.NOT_FOUND.value());
    }

    @Transactional
    public Transferencia transferenciaTed(TransferenciaTedRequest request) {
        /*mesma logica apenas usando agencia e numero da conta*/
        logger.info(ServiceUtils.INICIANDO_TRANSERENCIA);
        varificarValor(request.getValor());
        Conta contaOrigem = buscarContaPorId(request.getIdOrigem());
        if (contaOrigem != null) {
            logger.info(ServiceUtils.CONTA_ORIGEM_ENCONTRADA);
            verificarSaldo(contaOrigem.getSaldo(), request.getValor());

            Conta contaDestino = buscarContaPorId(buscarIdConta(request.getAgenciaDestino(), request.getNumeroContaDestino()));
            if (contaDestino != null) {
                if (!contaOrigem.getSenha().equals(request.getSenha())) {
                    logger.info(ServiceUtils.SENHA_INCORRETA);
                    throw new JavaException(ServiceUtils.SENHA_INCORRETA, HttpStatus.UNAUTHORIZED.value());
                }

                logger.info(ServiceUtils.TRANSFERENCIA_REALIZADA_COM_SUCESSO);
                return contaDao.tranferir(contaOrigem.getId(), contaDestino.getId(), request.getValor());
            }
            logger.info(ServiceUtils.CONTA_DESTINO_NAO_ENCONTRADA);
            throw new JavaException(ServiceUtils.CONTA_DESTINO_NAO_ENCONTRADA, HttpStatus.NOT_FOUND.value());
        }
        logger.info(ServiceUtils.CONTA_ORGEM_NAO_ENCONTRADA);
        throw new JavaException(ServiceUtils.CONTA_ORGEM_NAO_ENCONTRADA, HttpStatus.NOT_FOUND.value());
    }

    @Transactional
    public void fecharConta(Long id) {
        /*procura conta e se existir verifica se o saldo e 0 para fechar a conta*/
        logger.info(ServiceUtils.FECHANDO_CONTA);
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            if (conta.getSaldo() != 0) {
                contaDao.deleteById(id);
                logger.info(ServiceUtils.CONTA_FECHADA_COM_SUCESSO);
            } else {
                logger.info(ServiceUtils.CONTA_COM_SALDO_NAO_PODE_SER_FECHADA);
                throw new JavaException(ServiceUtils.CONTA_COM_SALDO_NAO_PODE_SER_FECHADA, HttpStatus.BAD_REQUEST.value());
            }
        }
        logger.info(ServiceUtils.CONTA_NAO_ENCONTRADA);
        throw new JavaException(ServiceUtils.CONTA_NAO_ENCONTRADA, HttpStatus.NOT_FOUND.value());
    }

    public double exibirSaldo(Long id) {
        logger.info(ServiceUtils.CONSULTANDO_SALDO);
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            logger.info(ServiceUtils.SUCESSO);
            return conta.getSaldo();
        }
        logger.info(ServiceUtils.CONTA_NAO_ENCONTRADA);
        throw new JavaException(ServiceUtils.CONTA_NAO_ENCONTRADA, HttpStatus.NOT_FOUND.value());

    }

    public void depositar(Long id, double valor) {
        logger.info(ServiceUtils.INICIANDO_DEPOSITO);
        varificarValor(valor);
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            contaDao.depositor(id, valor);
            logger.info(ServiceUtils.SUCESSO);
        } else {
            logger.info(ServiceUtils.CONTA_NAO_ENCONTRADA);
            throw new JavaException(ServiceUtils.CONTA_NAO_ENCONTRADA, HttpStatus.NOT_FOUND.value());
        }
    }

    public void sacar(Long id, double valor) {
        logger.info(ServiceUtils.INICIANDO_SACAR);
        varificarValor(valor);
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            verificarSaldo(conta.getSaldo(), valor);
            contaDao.sacar(id,valor);
            logger.info(ServiceUtils.SUCESSO);
        }else{
        logger.info(ServiceUtils.CONTA_NAO_ENCONTRADA);
        throw new JavaException(ServiceUtils.CONTA_NAO_ENCONTRADA, HttpStatus.NOT_FOUND.value());
    }}

    public Conta buscarContaPorId(Long id) {
        logger.info(ServiceUtils.INICIANDO_BUSCA);
        Optional<Conta> conta = contaDao.findById(id);
        if (!conta.isPresent()) {
            logger.info(ServiceUtils.CONTA_NAO_ENCONTRADA);
            throw new JavaException(ServiceUtils.CONTA_NAO_ENCONTRADA, HttpStatus.NOT_FOUND.value());
        }
        logger.info(ServiceUtils.CONTA_ENCONTRADA);
        return conta.get();
    }

    public void aplicicarTaxaManutencao(Long id) {
        /*aplica taxa de manutencao a conta corrente */
        logger.info(ServiceUtils.APLICANDO_TAXA_MANUTENCAO);
        Conta conta = buscarContaPorId(id);
        if (conta.getTipoConta() == TipoConta.CORRENTE) {
            if (conta.getCliente().getTipo() == TipoCliente.COMUM) {
                conta.setSaldo(conta.getSaldo() - 12.00);
            } else if (conta.getCliente().getTipo() == TipoCliente.SUPER) {
                conta.setSaldo(conta.getSaldo() - 8.00);
            }
            contaDao.save(conta);
            logger.info(ServiceUtils.SUCESSO);
        } else {
            logger.info(ServiceUtils.CONTA_NAO_APLICAVEL);
            throw new JavaException(ServiceUtils.CONTA_NAO_APLICAVEL, HttpStatus.BAD_REQUEST.value());
        }
    }

    public void aplicarTaxaRendimento(Long id) {
        logger.info(ServiceUtils.INICIANDO_TAXA_RENDIMENTO);
        /*aplica taxa de rendimento a poupanca*/
        Conta conta = buscarContaPorId(id);
        if (conta.getTipoConta() == TipoConta.POUPANCA) {
            if (conta.getCliente().getTipo() == TipoCliente.COMUM) {
                conta.setSaldo(conta.getSaldo() + (conta.getSaldo() * 0.005));
            } else if (conta.getCliente().getTipo() == TipoCliente.SUPER) {
                conta.setSaldo(conta.getSaldo() + (conta.getSaldo() * 0.007));
            } else if (conta.getCliente().getTipo() == TipoCliente.PREMIUM) {
                conta.setSaldo(conta.getSaldo() + (conta.getSaldo() * 0.009));
            }
            contaDao.save(conta);
            logger.info(ServiceUtils.SUCESSO);
        } else {
            logger.info(ServiceUtils.CONTA_NAO_APLICAVEL);
            throw new JavaException(ServiceUtils.CONTA_NAO_APLICAVEL, HttpStatus.BAD_REQUEST.value());
        }
    }


    private Long buscarIdConta(String chavePix) {
        /*busca conta pela chave pix*/
        logger.info(ServiceUtils.INICIANDO_BUSCA);
        Optional<Conta> conta = contaDao.findByChavePix(chavePix);
        if (conta.isPresent()) {
            logger.info(ServiceUtils.CONTA_ENCONTRADA);
            return conta.get().getId();
        }
        logger.info(ServiceUtils.CONTA_NAO_ENCONTRADA);
        throw new JavaException(ServiceUtils.CONTA_NAO_ENCONTRADA, HttpStatus.NOT_FOUND.value());
    }

    private Long buscarIdConta(Long agencia, Long numero) {
        /*busca comta pela agencia e numero*/
        logger.info(ServiceUtils.INICIANDO_BUSCA);
        Optional<Conta> conta = contaDao.findByAgenciaAndNumero(agencia, numero);
        if (conta.isPresent()) {
            logger.info(ServiceUtils.CONTA_ENCONTRADA);
            return conta.get().getId();
        }
        logger.info(ServiceUtils.CONTA_NAO_ENCONTRADA);
        throw new JavaException(ServiceUtils.CONTA_NAO_ENCONTRADA, HttpStatus.NOT_FOUND.value());
    }

    private void verificarContaExiste(Conta conta) {
        logger.info(ServiceUtils.CADASTRANDO_CONTA);
        Optional<Conta> contaOptional = contaDao.findByAgenciaAndNumero(conta.getAgencia(), conta.getNumero());

        if (contaOptional.isPresent()) {
            logger.info(ServiceUtils.CONTA_JA_CADASTRADA);
            throw new JavaException(ServiceUtils.CONTA_JA_CADASTRADA, HttpStatus.BAD_REQUEST.value());
        }
    }

    private void varificarValor(double valor) {
        if (valor < 0) {
            logger.info(ServiceUtils.VALOR_NAO_PODE_SER_NEGATIVO);
            throw new JavaException(ServiceUtils.VALOR_NAO_PODE_SER_NEGATIVO, HttpStatus.BAD_REQUEST.value());
        }
    }

    private void verificarSaldo(double saldo, double valor) {
        if (saldo < valor) {
            logger.info(ServiceUtils.SALDO_INSUFICIENTE);
            throw new JavaException(ServiceUtils.SALDO_INSUFICIENTE, HttpStatus.BAD_REQUEST.value());
        }
    }

    private void popularDadosConta(Conta conta) {
        /*completa os dados da conta*/
        logger.info(ServiceUtils.CADASTRANDO_CONTA);
        conta.setSaldo(0);
        conta.setNumero(random.nextLong(900000) + 100000);
        conta.setAgencia(1221L);
        String regexCPF = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}";
        if (conta.getChavePix() == null || conta.getChavePix().isEmpty() || !conta.getChavePix().matches(regexCPF)) {
            conta.setChavePix(generateChavePix(30));
        }
        logger.info(ServiceUtils.CONTA_CADASTRADA_COM_SUCESSO);
    }

    private String generateChavePix(int i) {
        /*Gera a chave pix aleatoria com 30 caracteres*/
        final String CARACTERES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i; j++) {
            int index = random.nextInt(CARACTERES.length());
            sb.append(CARACTERES.charAt(index));
        }
        return sb.toString();
    }

    private Transferencia registrarTransferencia(long idContaOrigem, long idContaDestino, double valor) {
        /*retorna como se fosse um cupom de transferencia*/
        Transferencia transferencia = new Transferencia();
        transferencia.setIdContaOrigem(idContaOrigem);
        transferencia.setIdContaDestino(idContaDestino);
        transferencia.setValor(valor);
        transferencia.setDataTransferencia(LocalDateTime.now());
        transferenciaDao.save(transferencia);
        return transferencia;
    }
}