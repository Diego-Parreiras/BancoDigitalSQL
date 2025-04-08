package br.com.bancodigital.service;

import br.com.bancodigital.dao.ContaDao;
import br.com.bancodigital.dao.TransferenciaDao;
import br.com.bancodigital.model.Conta;
import br.com.bancodigital.model.Transferencia;
import br.com.bancodigital.model.dto.TransferenciaPixRequest;
import br.com.bancodigital.model.dto.TransferenciaTedRequest;
import br.com.bancodigital.model.enuns.TipoCliente;
import br.com.bancodigital.model.enuns.TipoConta;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class ContaService {

    @Autowired
    private ContaDao contaDao;

    @Autowired
    private TransferenciaDao transferenciaDao;

    private final Random random = new Random();

    public void criarConta(Conta conta) {
        /*Verifica de a conta ja existe, se nao, completa os dados da conta e salva*/
        verificarContaExiste(conta);
        popularDadosConta(conta);
        contaDao.save(conta);
    }

    private void popularDadosConta(Conta conta) {
        /*completa os dados da conta*/
        conta.setSaldo(0);
        conta.setNumero(random.nextLong(900000) + 100000);
        conta.setAgencia(1221L);
        String regexCPF = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}";
        if (conta.getChavePix() == null || conta.getChavePix().isEmpty() || !conta.getChavePix().matches(regexCPF)) {
            conta.setChavePix(generateChavePix(30));
        }
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

    @Transactional
    public Transferencia transferenciaPix(TransferenciaPixRequest request) {
        /*verifica se o valor existe e se a conta origem existe
         * e se existir faz a transferencia*/
        varificarValor(request.getValor());
        Conta contaOrigem = buscarContaPorId(request.getIdContaOrigem());
        if (contaOrigem != null) {
            verificarSaldo(contaOrigem.getSaldo(), request.getValor());

            /*verdica se a conta destino existe*/
            Conta contaDestino = buscarContaPorId(buscarIdConta(request.getChavePix()));
            if (contaDestino != null) {

                if (contaOrigem.getSenha() != request.getSenha()) {
                    throw new RuntimeException("Senha incorreta");
                }
                sacar(contaOrigem.getId(), request.getValor());
                depositar(contaDestino.getId(), request.getValor());

                return registrarTransferencia(request.getIdContaOrigem(), contaDestino.getId(), request.getValor());
            }
            throw new RuntimeException("ContaDestino não encontrada");
        }
        throw new RuntimeException("ContaOrigem não encontrada");
    }

    @Transactional
    public Transferencia transferenciaTed(TransferenciaTedRequest request) {
        /*mesma logica apenas usando agencia e numero da conta*/

        varificarValor(request.getValor());
        Conta contaOrigem = buscarContaPorId(request.getIdOrigem());
        if (contaOrigem != null) {
            verificarSaldo(contaOrigem.getSaldo(), request.getValor());

            Conta contaDestino = buscarContaPorId(buscarIdConta(request.getAgenciaDestino(), request.getNumeroContaDestino()));
            if (contaDestino != null) {
                if (contaOrigem.getSenha() != request.getSenha()) {
                    throw new RuntimeException("Senha incorreta");
                }

                sacar(contaOrigem.getId(), request.getValor());
                depositar(contaDestino.getId(), request.getValor());

                return registrarTransferencia(request.getIdOrigem(), contaDestino.getId(), request.getValor());
            }
            throw new RuntimeException("ContaDestino não encontrada");
        }
        throw new RuntimeException("ContaOrigem não encontrada");
    }

    public void fecharConta(Long id) {
        /*procura conta e se existir verifica se o saldo e 0 para fechar a conta*/
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            if (conta.getSaldo() != 0) {
                contaDao.deleteById(id);
            } else {
                throw new RuntimeException("Conta com saldo não pode ser fechada");
            }
        }
    }

    public double exibirSaldo(Long id) {
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            return conta.getSaldo();
        }
        throw new RuntimeException("Conta não encontrada");
    }

    public void depositar(Long id, double valor) {
        varificarValor(valor);
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            conta.setSaldo(conta.getSaldo() + valor);
            contaDao.save(conta);
        }
    }

    public void sacar(Long id, double valor) {
        varificarValor(valor);
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            ;
            verificarSaldo(conta.getSaldo(), valor);
            conta.setSaldo(conta.getSaldo() - valor);
            contaDao.save(conta);
        }
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

    private Long buscarIdConta(String chavePix) {
        /*busca conta pela chave pix*/
        Optional<Conta> conta = contaDao.findByChavePix(chavePix);
        if (conta.isPresent()) {
            return conta.get().getId();
        }
        throw new RuntimeException("Conta não encontrada");
    }

    private Long buscarIdConta(Long agencia, Long numero) {
        /*busca comta pela agencia e numero*/
        Optional<Conta> conta = contaDao.findByAgenciaAndNumero(agencia, numero);
        if (conta.isPresent()) {
            return conta.get().getId();
        }
        throw new RuntimeException("Conta não encontrada");
    }

    private void verificarContaExiste(Conta conta) {

        Optional<Conta> contaOptional = contaDao.findByAgenciaAndNumero(conta.getAgencia(), conta.getNumero());

        if (contaOptional.isPresent()) {
            throw new RuntimeException("Conta já cadastrada");
        }

    }

    private void varificarValor(double valor) {
        if (valor < 0) {
            throw new RuntimeException("Valor não pode ser negativo");
        }
    }

    private void verificarSaldo(double saldo, double valor) {
        if (saldo >= valor) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }

    public Conta buscarContaPorId(Long id) {
        Optional<Conta> conta = contaDao.findById(id);
        if (!conta.isPresent()) {
            throw new RuntimeException("Conta não encontrada");
        }
        return conta.get();
    }

    public void aplicicarTaxaManutencao(Long id) {
        /*aplica taxa de manutencao a conta corrente */
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
        } else {
            throw new RuntimeException("Sua Conta e poupanca.");
        }
    }

    public void aplicarTaxaRendimento(Long id) {
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
        } else {
            throw new RuntimeException("Sua Conta nao e poupanca.");
        }
    }
}