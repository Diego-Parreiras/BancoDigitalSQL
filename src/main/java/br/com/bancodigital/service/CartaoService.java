package br.com.bancodigital.service;

import br.com.bancodigital.dao.daoimplements.CartaoDaoImplements;
import br.com.bancodigital.dao.daoimplements.ContaDaoImplements;
import br.com.bancodigital.model.Cartao;
import br.com.bancodigital.model.CartaoDeCredito;
import br.com.bancodigital.model.CartaoDeDebito;
import br.com.bancodigital.model.dto.PagamentoCartaoRequest;
import br.com.bancodigital.model.enuns.TipoCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
public class CartaoService {

    @Autowired
    CartaoDaoImplements cartaoDao;
    @Autowired
    ContaDaoImplements contadao;
    private final Random random = new Random();

    private static final Logger logger = LoggerFactory.getLogger(CartaoService.class);

    @Transactional
    public void novoCartao(Cartao cartao) {
        logger.info("Iniciando cadastro de cartao");
        validarDados(cartao);
        popularCartao(cartao);
        cartaoDao.save(cartao);
        logger.info("Cartao cadastrado com sucesso");
    }

    public Cartao buscarId(Long id) {
        logger.info("Buscando cartao");
        Optional<Cartao> cartaoOptional = cartaoDao.findById(id);
        if (!cartaoOptional.isPresent()) {
            logger.info("Cartao nao encontrado");
            throw new RuntimeException("Cartao nao encontrado");
        }
        logger.info("Cartao encontrado");
        return cartaoOptional.get();
    }

    @Transactional
    public void pagar(long id, PagamentoCartaoRequest pagamento) {
        /*Metodo de pagamento padrao do cartao*/
        logger.info("Iniciando pagamento com cartao de ID " + id);
        Cartao cartao = buscarId(id);
        verStatus(cartao);
        verificarValor(cartao, pagamento);
    }

    public void aumentarLimiteCredito(Long id, double valor) {
        /*Pede um novo limite do cartao de credito*/
        logger.info("Aumentando limite do cartao de ID " + id);
        if (valor < 0) {
            logger.info("Valor nao pode ser negativo");
            throw new RuntimeException("Valor nao pode ser negativo");
        }
        Cartao cartao = buscarId(id);
        verStatus(cartao);
        if (cartao instanceof CartaoDeCredito) {
            logger.info("Cartao de Credito selecionado de ID " + cartao.getId());
            ((CartaoDeCredito) cartao).setLimiteCredito(valor);
            logger.info("Limite aumentado com sucesso");
        }
        logger.info("Cartao nao e de credito");
        throw new RuntimeException("Seu cartao nao e de credito");
    }

    public void aumentarLimiteDebito(Long id, double valor) {
        logger.info("Aumentando limite do cartao de ID " + id);
        if (valor < 0) {
            logger.info("Valor negativo");
            throw new RuntimeException("Valor nao pode ser negativo");
        }
        Cartao cartao = buscarId(id);
        verStatus(cartao);
        if (cartao instanceof CartaoDeDebito) {
            logger.info("Cartao de Debito selecionado de ID " + cartao.getId());
            ((CartaoDeDebito) cartao).setLimiteDiario(valor);
            logger.info("Limite aumentado com sucesso");
        }
        logger.info("Cartao nao e de credito");
        throw new RuntimeException("Seu cartao nao e de credito");
    }

    public void mudarStatus(Long id) {
        /*Desativa ou aticva o cartao*/
        logger.info("Mudando status do cartao de ID " + id);
        Cartao cartao = buscarId(id);

        if (cartao != null) {
            if (cartao instanceof CartaoDeCredito) {
                logger.info("Cartao de Credito selecionado de ID " + cartao.getId());
                if (((CartaoDeCredito) cartao).getFatura() != 0) {
                    logger.info("Fatura Pendente");
                    throw new RuntimeException("Preciso pagar a fatura primeiro");
                }
            }
            cartao.setAtivoOuNao(!cartao.isAtivoOuNao());
            cartaoDao.save(cartao);
            logger.info("Status alterado com sucesso");
        } else {
            logger.info("Cartao nao encontrado");
            throw new RuntimeException("Cartao nao encontrado");
        }
    }

    public void atualizarSenha(Long id, long senha) {
        /*Atualiza a senha do cartao*/
        logger.info("Atualizando senha do cartao de ID " + id);
        Cartao cartao = buscarId(id);
        cartao.setSenha(senha);
        cartaoDao.save(cartao);
        logger.info("Senha atualizada com sucesso");
    }

    public double buscarFatura(Long id) {
        /*Encontra a fatura*/
        logger.info("Buscando fatura do cartao de ID " + id);
        Cartao cartao = buscarId(id);
        if (cartao instanceof CartaoDeCredito) {
            logger.info("Cartao de Credito selecionado de ID " + cartao.getId());
            return ((CartaoDeCredito) cartao).getFatura();

        }
        logger.info("Cartao de debito");
        throw new RuntimeException("Cartao de debito");
    }
    @Transactional
    public void pagarFatura(Long id) {
        /*Paga a fatura*/
        logger.info("Pagando fatura do cartao de ID " + id);
        Cartao cartao = buscarId(id);
        if (cartao instanceof CartaoDeCredito) {
            logger.info("Cartao de Credito selecionado de ID " + cartao.getId());
            if (((CartaoDeCredito) cartao).getFatura() == 0) {
                logger.info("Fatura ja paga");
                throw new RuntimeException("Fatura ja paga");
            }
            if (((CartaoDeCredito) cartao).getFatura() >= ((CartaoDeCredito) cartao).getLimiteCredito() * 0.8) {
                ((CartaoDeCredito) cartao).setFatura(((CartaoDeCredito) cartao).getFatura() + ((CartaoDeCredito) cartao).getLimiteCredito() * 0.05);
            }
            if (((CartaoDeCredito) cartao).getFatura() > cartao.getConta().getSaldo()) {
                logger.info("Saldo insuficiente");
                throw new RuntimeException("Saldo insuficiente");
            } else {
                cartao.getConta().setSaldo(cartao.getConta().getSaldo() - ((CartaoDeCredito) cartao).getFatura());
                ((CartaoDeCredito) cartao).setFatura(0);
                cartaoDao.save(cartao);
                contadao.save(cartao.getConta());
                logger.info("Fatura paga com sucesso");

            }
        }
    }

    private void verStatus(Cartao cartao) {
        /*verifica se o cartao esta ativo*/
        logger.info("Verificando status do cartao");
        if (!cartao.isAtivoOuNao()) {
            logger.info("Cartao inativo");
            throw new RuntimeException("Cartao inativo");
        }
        logger.info("Cartao ativo");
    }

    private void verificarValor(Cartao cartao, PagamentoCartaoRequest pagamento) {
        /*Verifica se o valor e valido para pagamemto */
        logger.info("Verificando dados do pagamento");
        if (!pagamento.getSenha().equals(cartao.getSenha())) {
            logger.info("Senha incorreta");
            throw new RuntimeException("Senha incorreta");
        }
        if (pagamento.getPagamento() < 0) {
            logger.info("Valor negativo");
            throw new RuntimeException("Valor nao pode ser negativo");
        }
        if (cartao instanceof CartaoDeCredito) {
            logger.info("Cartao de Credito selecionado de ID " + cartao.getId());
            if (pagamento.getPagamento() > ((CartaoDeCredito) cartao).getLimiteCredito()) {
                logger.info("Limite insuficiente");
                throw new RuntimeException("Limite insuficiente");
            }
            ((CartaoDeCredito) cartao).setFatura(((CartaoDeCredito) cartao).getFatura() + pagamento.getPagamento());
            cartaoDao.save(cartao);
            logger.info("Pagamento realizado com sucesso");
        } else if (cartao instanceof CartaoDeDebito) {
            logger.info("Cartao de Debito selecionado de ID " + cartao.getId());
            if (pagamento.getPagamento() > ((CartaoDeDebito) cartao).getLimiteDiario()) {
                logger.info("Limite insuficiente");
                throw new RuntimeException("Limite insuficiente");
            }
            if (cartao.getConta().getSaldo() < pagamento.getPagamento()) {
                logger.info("Saldo insuficiente");
                throw new RuntimeException("Saldo insuficiente");
            }

            ((CartaoDeDebito) cartao).setLimiteDiario(((CartaoDeDebito) cartao).getLimiteDiario() - pagamento.getPagamento());
            cartao.getConta().setSaldo(cartao.getConta().getSaldo() - pagamento.getPagamento());

            contadao.save(cartao.getConta());
            cartaoDao.save(cartao);
            logger.info("Pagamento realizado com sucesso");
        }

    }

    private void popularCartao(Cartao cartao) {
        /*Completa os dados do cartao */
        logger.info("Completando dados do cartao");
        cartao.setAtivoOuNao(true);
        cartao.setNumero(random.nextLong(900000) + 100000);
        cartao.setCvv(random.nextLong(900) + 100);

        if (cartao instanceof CartaoDeCredito) {
            if (cartao.getConta().getCliente().getTipo() == TipoCliente.COMUM) {
                ((CartaoDeCredito) cartao).setLimiteCredito(1000);
            }
            if (cartao.getConta().getCliente().getTipo() == TipoCliente.SUPER) {
                ((CartaoDeCredito) cartao).setLimiteCredito(5000);
            }
            if (cartao.getConta().getCliente().getTipo() == TipoCliente.PREMIUM) {
                ((CartaoDeCredito) cartao).setLimiteCredito(10000);
            }

        } else if (cartao instanceof CartaoDeDebito) {
            ((CartaoDeDebito) cartao).setLimiteDiario(1000);
        }
        logger.info("Cartao populado com sucesso");
    }

    private void validarDados(Cartao cartao) {
        /*Verifica se o cartao ja esta cadastrado*/
        logger.info("Validando dados do cartao");
        if (cartaoDao.existsByNumero(cartao.getNumero())) {
            throw new RuntimeException("Cartao ja cadastrado");
        }
    }
}