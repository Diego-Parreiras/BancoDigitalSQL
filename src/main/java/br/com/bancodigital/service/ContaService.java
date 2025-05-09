package br.com.bancodigital.service;

import br.com.bancodigital.dao.daoimplements.ContaDaoImplements;
import br.com.bancodigital.dao.daoimplements.TransferenciaDaoImplements;
import br.com.bancodigital.model.Conta;
import br.com.bancodigital.model.Transferencia;
import br.com.bancodigital.model.dto.TransferenciaPixRequest;
import br.com.bancodigital.model.dto.TransferenciaTedRequest;
import br.com.bancodigital.model.enuns.TipoCliente;
import br.com.bancodigital.model.enuns.TipoConta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class ContaService {

    @Autowired
    private ContaDaoImplements contaDao;
    @Autowired
    private TransferenciaDaoImplements transferenciaDao;

    private final Logger logger = LoggerFactory.getLogger(ContaService.class);
    private final Random random = new Random();
    @Transactional
    public void criarConta(Conta conta) {
        try {
            /*Verifica de a conta ja existe, se nao, completa os dados da conta e salva*/
            logger.info("Iniciando cadastro de conta");
            verificarContaExiste(conta);
            popularDadosConta(conta);
            contaDao.save(conta);
            logger.info("Conta cadastrada com sucesso");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Transactional
    public Transferencia transferenciaPix(TransferenciaPixRequest request) {
        /*verifica se o valor existe e se a conta origem existe
         * e se existir faz a transferencia*/
        logger.info("Iniciando transferencia");
        varificarValor(request.getValor());
        Conta contaOrigem = buscarContaPorId(request.getIdContaOrigem());
        if (contaOrigem != null) {
            logger.info("ContaOrigem encontrada");
            verificarSaldo(contaOrigem.getSaldo(), request.getValor());

            /*verdica se a conta destino existe*/
            Conta contaDestino = buscarContaPorId(buscarIdConta(request.getChavePix()));
            if (contaDestino != null) {

                if (contaOrigem.getSenha() != request.getSenha()) {
                    logger.info("Senha incorreta");
                    throw new RuntimeException("Senha incorreta");
                }
                sacar(contaOrigem.getId(), request.getValor());
                depositar(contaDestino.getId(), request.getValor());
                logger.info("Transferencia realizada com sucesso");
                return registrarTransferencia(request.getIdContaOrigem(), contaDestino.getId(), request.getValor());
            }
            logger.info("ContaDestino não encontrada");
            throw new RuntimeException("ContaDestino não encontrada");
        }
        logger.info("ContaOrigem não encontrada");
        throw new RuntimeException("ContaOrigem não encontrada");
    }

    @Transactional
    public Transferencia transferenciaTed(TransferenciaTedRequest request) {
        /*mesma logica apenas usando agencia e numero da conta*/
        logger.info("Iniciando transferencia");
        varificarValor(request.getValor());
        Conta contaOrigem = buscarContaPorId(request.getIdOrigem());
        if (contaOrigem != null) {
            logger.info("ContaOrigem encontrada");
            verificarSaldo(contaOrigem.getSaldo(), request.getValor());

            Conta contaDestino = buscarContaPorId(buscarIdConta(request.getAgenciaDestino(), request.getNumeroContaDestino()));
            if (contaDestino != null) {
                if (contaOrigem.getSenha() != request.getSenha()) {
                    logger.info("Senha incorreta");
                    throw new RuntimeException("Senha incorreta");
                }

                sacar(contaOrigem.getId(), request.getValor());
                depositar(contaDestino.getId(), request.getValor());
                logger.info("Transferencia realizada com sucesso");
                return registrarTransferencia(request.getIdOrigem(), contaDestino.getId(), request.getValor());
            }
            logger.info("ContaDestino não encontrada");
            throw new RuntimeException("ContaDestino não encontrada");
        }
        logger.info("ContaOrigem não encontrada");
        throw new RuntimeException("ContaOrigem não encontrada");
    }
    @Transactional
    public void fecharConta(Long id) {
        /*procura conta e se existir verifica se o saldo e 0 para fechar a conta*/
        logger.info("Iniciando fechamento de conta");
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            if (conta.getSaldo() != 0) {
                contaDao.deleteById(id);
                logger.info("Conta fechada com sucesso");
            } else {
                logger.info("Conta com saldo não pode ser fechada");
                throw new RuntimeException("Conta com saldo não pode ser fechada");
            }
        }
        logger.info("Conta não encontrada");
        throw new RuntimeException("Conta não encontrada");
    }

    public double exibirSaldo(Long id) {
        logger.info("Iniciando consulta de saldo");
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            logger.info("Saldo consultado com sucesso");
            return conta.getSaldo();
        }
        logger.info("Conta não encontrada");
        throw new RuntimeException("Conta não encontrada");
    }

    public void depositar(Long id, double valor) {
        logger.info("Iniciando deposito");
        varificarValor(valor);
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            conta.setSaldo(conta.getSaldo() + valor);
            contaDao.save(conta);
            logger.info("Deposito realizado com sucesso");
        }
        logger.info("Conta não encontrada");
        throw new RuntimeException("Conta não encontrada");
    }

    public void sacar(Long id, double valor) {
        logger.info("Iniciando saque");
        varificarValor(valor);
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            verificarSaldo(conta.getSaldo(), valor);
            conta.setSaldo(conta.getSaldo() - valor);
            contaDao.save(conta);
            logger.info("Saque realizado com sucesso");
        }
        logger.info("Conta não encontrada");
        throw new RuntimeException("Conta não encontrada");
    }

    public Conta buscarContaPorId(Long id) {
        logger.info("Iniciando busca de conta");
        Optional<Conta> conta = contaDao.findById(id);
        if (!conta.isPresent()) {
            logger.info("Conta não encontrada");
            throw new RuntimeException("Conta não encontrada");
        }
        logger.info("Conta encontrada com sucesso");
        return conta.get();
    }

    public void aplicicarTaxaManutencao(Long id) {
        /*aplica taxa de manutencao a conta corrente */
        logger.info("Iniciando aplicação de taxa de manutenção");
        Conta conta = buscarContaPorId(id);
        if (conta.getTipoConta() == TipoConta.CORRENTE) {
            System.out.println(conta.getCliente().getTipo());
            if (conta.getCliente().getTipo() == TipoCliente.COMUM) {
                System.out.println("Passei por aqui");
                conta.setSaldo(conta.getSaldo() - 12.00);
            } else if (conta.getCliente().getTipo() == TipoCliente.SUPER) {
                conta.setSaldo(conta.getSaldo() - 8.00);
            }
            System.out.println(conta.getSaldo());
            contaDao.save(conta);
            logger.info("Taxa de manutenção aplicada com sucesso");
        } else {
            logger.info("Sua Conta e poupanca.");
            throw new RuntimeException("Sua Conta e poupanca.");
        }
    }

    public void aplicarTaxaRendimento(Long id) {
        logger.info("Iniciando aplicação de taxa de rendimento");
        /*aplica taxa de rendimento a poupanca*/
        Conta conta = buscarContaPorId(id);
        if (conta.getTipoConta() == TipoConta.POUPANCA) {
            System.out.println(conta.getCliente().getTipo());
            if (conta.getCliente().getTipo() == TipoCliente.COMUM) {
                conta.setSaldo(conta.getSaldo() + (conta.getSaldo() * 0.005));
            } else if (conta.getCliente().getTipo() == TipoCliente.SUPER) {
                conta.setSaldo(conta.getSaldo() + (conta.getSaldo() * 0.007));
            } else if (conta.getCliente().getTipo() == TipoCliente.PREMIUM) {
                conta.setSaldo(conta.getSaldo() + (conta.getSaldo() * 0.009));
            }
            System.out.println(conta.getSaldo());
            contaDao.save(conta);
            logger.info("Taxa de rendimento aplicada com sucesso");
        } else {
            logger.info("Sua Conta nao e poupanca.");
            throw new RuntimeException("Sua Conta nao e poupanca.");
        }
    }


    private Long buscarIdConta(String chavePix) {
        /*busca conta pela chave pix*/
        logger.info("Iniciando busca de conta");
        Optional<Conta> conta = contaDao.findByChavePix(chavePix);
        if (conta.isPresent()) {
            logger.info("Conta encontrada com sucesso");
            return conta.get().getId();
        }
        logger.info("Conta não encontrada");
        throw new RuntimeException("Conta não encontrada");
    }

     private Long buscarIdConta(Long agencia, Long numero) {
        /*busca comta pela agencia e numero*/
        logger.info("Iniciando busca de conta");
        Optional<Conta> conta = contaDao.findByAgenciaAndNumero(agencia, numero);
        if (conta.isPresent()) {
            logger.info("Conta encontrada com sucesso");
            return conta.get().getId();
        }
        logger.info("Conta não encontrada");
        throw new RuntimeException("Conta não encontrada");
    }

    private void verificarContaExiste(Conta conta) {
        logger.info("Iniciando verificação de conta");
        Optional<Conta> contaOptional = contaDao.findByAgenciaAndNumero(conta.getAgencia(), conta.getNumero());

        if (contaOptional.isPresent()) {
            logger.info("Conta já cadastrada");
            throw new RuntimeException("Conta já cadastrada");
        }

    }

    private void varificarValor(double valor) {
        if (valor < 0) {
            logger.info("Valor não pode ser negativo");
            throw new RuntimeException("Valor não pode ser negativo");
        }
    }

    private void verificarSaldo(double saldo, double valor) {
        if (saldo >= valor) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }

    private void popularDadosConta(Conta conta) {
        /*completa os dados da conta*/
        logger.info("Iniciando cadastro de conta");
        conta.setSaldo(0);
        conta.setNumero(random.nextLong(900000) + 100000);
        conta.setAgencia(1221L);
        String regexCPF = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}";
        if (conta.getChavePix() == null || conta.getChavePix().isEmpty() || !conta.getChavePix().matches(regexCPF)) {
            conta.setChavePix(generateChavePix(30));
        }
        logger.info("Conta cadastrada com sucesso");
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