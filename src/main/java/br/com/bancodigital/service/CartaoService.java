package br.com.bancodigital.service;

import br.com.bancodigital.dao.daoimplements.CartaoDaoImplements;
import br.com.bancodigital.dao.daoimplements.ContaDaoImplements;
import br.com.bancodigital.model.Cartao;
import br.com.bancodigital.model.CartaoDeCredito;
import br.com.bancodigital.model.CartaoDeDebito;
import br.com.bancodigital.model.dto.PagamentoCartaoRequest;
import br.com.bancodigital.model.enuns.TipoCliente;
import br.com.bancodigital.service.utils.ServiceUtils;
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
        logger.info(ServiceUtils.CADASTRANDO_CARTAO);
        validarDados(cartao);
        popularCartao(cartao);
        cartaoDao.save(cartao);
        logger.info(ServiceUtils.SUCESSO);
    }

    public Cartao buscarId(Long id) {
        logger.info(ServiceUtils.INICIANDO_BUSCA);
        Optional<Cartao> cartaoOptional = cartaoDao.findById(id);
        if (!cartaoOptional.isPresent()) {
            logger.info(ServiceUtils.CARTAO_NAO_ENCONTRADO);
            throw new RuntimeException(ServiceUtils.CARTAO_NAO_ENCONTRADO);
        }
        logger.info(ServiceUtils.SUCESSO);
        return cartaoOptional.get();
    }

    @Transactional
    public void pagar(long id, PagamentoCartaoRequest pagamento) {
        logger.info(ServiceUtils.VERIFICANDO_DADOS_DO_PAGAMENTO);
        Cartao cartao = buscarId(id);
        verStatus(cartao);
        verificarValor(cartao, pagamento);
    }

    public void aumentarLimiteCredito(Long id, double valor) {
        logger.info(ServiceUtils.VERIFICANDO_DADOS_DO_PAGAMENTO);
        if (valor < 0) {
            logger.info(ServiceUtils.VALOR_NEGATIVO);
            throw new RuntimeException(ServiceUtils.VALOR_NEGATIVO);
        }
        Cartao cartao = buscarId(id);
        verStatus(cartao);
        if (cartao instanceof CartaoDeCredito) {
            logger.info(ServiceUtils.CARTAO_DE_CREDITO_SELECIONADO + cartao.getId());
            ((CartaoDeCredito) cartao).setLimiteCredito(valor);
            logger.info(ServiceUtils.SUCESSO);
            return;
        }
        logger.info(ServiceUtils.CARTAO_NAO_E_CREDITO);
        throw new RuntimeException(ServiceUtils.CARTAO_NAO_E_CREDITO);
    }

    public void aumentarLimiteDebito(Long id, double valor) {
        logger.info(ServiceUtils.VERIFICANDO_DADOS_DO_PAGAMENTO);
        if (valor < 0) {
            logger.info(ServiceUtils.VALOR_NEGATIVO);
            throw new RuntimeException(ServiceUtils.VALOR_NEGATIVO);
        }
        Cartao cartao = buscarId(id);
        verStatus(cartao);
        if (cartao instanceof CartaoDeDebito) {
            logger.info(ServiceUtils.CARTAO_DE_DEBITO_SELECIONADO + cartao.getId());
            ((CartaoDeDebito) cartao).setLimiteDiario(valor);
            logger.info(ServiceUtils.SUCESSO);
            return;
        }
        logger.info(ServiceUtils.CARTAO_NAO_E_DEBITO);
        throw new RuntimeException(ServiceUtils.CARTAO_NAO_E_DEBITO);
    }

    public void mudarStatus(Long id) {
        logger.info("Mudando status do cartao de ID " + id);
        Cartao cartao = buscarId(id);
        if (cartao instanceof CartaoDeCredito) {
            logger.info(ServiceUtils.CARTAO_DE_CREDITO_SELECIONADO + cartao.getId());
            if (((CartaoDeCredito) cartao).getFatura() != 0) {
                logger.info(ServiceUtils.FATURA_PENDENTE);
                throw new RuntimeException(ServiceUtils.FATURA_PENDENTE);
            }
        }
        cartao.setAtivoOuNao(!cartao.isAtivoOuNao());
        cartaoDao.save(cartao);
        logger.info(ServiceUtils.STATUS_ALTERADO);
    }

    public void atualizarSenha(Long id, long senha) {
        logger.info("Atualizando senha do cartao de ID " + id);
        Cartao cartao = buscarId(id);
        cartao.setSenha(senha);
        cartaoDao.save(cartao);
        logger.info(ServiceUtils.SENHA_ATUALIZADA);
    }

    public double buscarFatura(Long id) {
        logger.info("Buscando fatura do cartao de ID " + id);
        Cartao cartao = buscarId(id);
        if (cartao instanceof CartaoDeCredito) {
            logger.info(ServiceUtils.CARTAO_DE_CREDITO_SELECIONADO + cartao.getId());
            return ((CartaoDeCredito) cartao).getFatura();
        }
        logger.info(ServiceUtils.CARTAO_NAO_E_CREDITO);
        throw new RuntimeException(ServiceUtils.CARTAO_NAO_E_CREDITO);
    }

    @Transactional
    public void pagarFatura(Long id) {
        logger.info("Pagando fatura do cartao de ID " + id);
        Cartao cartao = buscarId(id);
        if (cartao instanceof CartaoDeCredito) {
            logger.info(ServiceUtils.CARTAO_DE_CREDITO_SELECIONADO + cartao.getId());
            CartaoDeCredito cc = (CartaoDeCredito) cartao;

            if (cc.getFatura() == 0) {
                logger.info(ServiceUtils.FATURA_JA_PAGA);
                throw new RuntimeException(ServiceUtils.FATURA_JA_PAGA);
            }

            if (cc.getFatura() >= cc.getLimiteCredito() * 0.8) {
                cc.setFatura(cc.getFatura() + cc.getLimiteCredito() * 0.05);
            }

            if (cc.getFatura() > cartao.getConta().getSaldo()) {
                logger.info(ServiceUtils.SALDO_INSUFICIENTE);
                throw new RuntimeException(ServiceUtils.SALDO_INSUFICIENTE);
            }

            cartao.getConta().setSaldo(cartao.getConta().getSaldo() - cc.getFatura());
            cc.setFatura(0);
            cartaoDao.save(cartao);
            contadao.save(cartao.getConta());

            logger.info(ServiceUtils.SUCESSO);
        }
    }

    private void verStatus(Cartao cartao) {
        logger.info("Verificando status do cartao");
        if (!cartao.isAtivoOuNao()) {
            logger.info(ServiceUtils.CARTAO_INATIVO);
            throw new RuntimeException(ServiceUtils.CARTAO_INATIVO);
        }
        logger.info(ServiceUtils.SUCESSO);
    }

    private void verificarValor(Cartao cartao, PagamentoCartaoRequest pagamento) {
        logger.info(ServiceUtils.VERIFICANDO_DADOS_DO_PAGAMENTO);
        if (!pagamento.getSenha().equals(cartao.getSenha())) {
            logger.info(ServiceUtils.SENHA_INCORRETA);
            throw new RuntimeException(ServiceUtils.SENHA_INCORRETA);
        }
        if (pagamento.getPagamento() < 0) {
            logger.info(ServiceUtils.VALOR_NEGATIVO);
            throw new RuntimeException(ServiceUtils.VALOR_NEGATIVO);
        }

        if (cartao instanceof CartaoDeCredito) {
            logger.info(ServiceUtils.CARTAO_DE_CREDITO_SELECIONADO + cartao.getId());
            if (pagamento.getPagamento() > ((CartaoDeCredito) cartao).getLimiteCredito()) {
                logger.info(ServiceUtils.LIMITE_INSUFICIENTE);
                throw new RuntimeException(ServiceUtils.LIMITE_INSUFICIENTE);
            }

            ((CartaoDeCredito) cartao).setFatura(((CartaoDeCredito) cartao).getFatura() + pagamento.getPagamento());
            cartaoDao.save(cartao);
            logger.info(ServiceUtils.SUCESSO);

        } else if (cartao instanceof CartaoDeDebito) {
            logger.info(ServiceUtils.CARTAO_DE_DEBITO_SELECIONADO + cartao.getId());

            if (pagamento.getPagamento() > ((CartaoDeDebito) cartao).getLimiteDiario()) {
                logger.info(ServiceUtils.LIMITE_INSUFICIENTE);
                throw new RuntimeException(ServiceUtils.LIMITE_INSUFICIENTE);
            }

            if (cartao.getConta().getSaldo() < pagamento.getPagamento()) {
                logger.info(ServiceUtils.SALDO_INSUFICIENTE);
                throw new RuntimeException(ServiceUtils.SALDO_INSUFICIENTE);
            }

            ((CartaoDeDebito) cartao).setLimiteDiario(((CartaoDeDebito) cartao).getLimiteDiario() - pagamento.getPagamento());
            cartao.getConta().setSaldo(cartao.getConta().getSaldo() - pagamento.getPagamento());

            contadao.save(cartao.getConta());
            cartaoDao.save(cartao);
            logger.info(ServiceUtils.SUCESSO);
        }
    }

    private void popularCartao(Cartao cartao) {
        logger.info(ServiceUtils.POPULANDO_CARTAO);
        cartao.setAtivoOuNao(true);
        cartao.setNumero(random.nextLong(900000) + 100000);
        cartao.setCvv(random.nextLong(900) + 100);

        if (cartao instanceof CartaoDeCredito) {
            TipoCliente tipo = cartao.getConta().getCliente().getTipo();
            if (tipo == TipoCliente.COMUM) ((CartaoDeCredito) cartao).setLimiteCredito(1000);
            else if (tipo == TipoCliente.SUPER) ((CartaoDeCredito) cartao).setLimiteCredito(5000);
            else if (tipo == TipoCliente.PREMIUM) ((CartaoDeCredito) cartao).setLimiteCredito(10000);
        } else if (cartao instanceof CartaoDeDebito) {
            ((CartaoDeDebito) cartao).setLimiteDiario(1000);
        }

        logger.info(ServiceUtils.SUCESSO);
    }

    private void validarDados(Cartao cartao) {
        logger.info(ServiceUtils.CADASTRANDO_CARTAO);
        if (cartaoDao.existsByNumero(cartao.getNumero())) {
            throw new RuntimeException(ServiceUtils.CARTAO_JA_CADASTRADO);
        }
    }
}