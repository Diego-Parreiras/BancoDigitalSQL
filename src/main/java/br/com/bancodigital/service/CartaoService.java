package br.com.bancodigital.service;

import br.com.bancodigital.dao.daoimplements.CartaoDaoImplements;
import br.com.bancodigital.dao.daoimplements.ContaDaoImplements;
import br.com.bancodigital.exception.JavaException;
import br.com.bancodigital.model.entity.Cartao;
import br.com.bancodigital.model.entity.CartaoDeCredito;
import br.com.bancodigital.model.entity.CartaoDeDebito;
import br.com.bancodigital.model.dto.PagamentoCartaoRequest;
import br.com.bancodigital.model.enuns.TipoCliente;
import br.com.bancodigital.constantutils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@CacheConfig(cacheNames = "Cartao")
public class CartaoService {
    @Autowired
    CartaoDaoImplements cartaoDao;
    @Autowired
    ContaDaoImplements contadao;
    private double fatura;

    private final Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(CartaoService.class);

    @Transactional
    @CacheEvict(allEntries = true)
    public void novoCartao(Cartao cartao) {
        logger.info(ServiceUtils.CADASTRANDO_CARTAO);
        popularCartao(cartao);
        cartaoDao.save(cartao);
        logger.info(ServiceUtils.SUCESSO);
    }

    @Cacheable(key = "#id")
    public Cartao buscarId(Long id) {
        logger.info(ServiceUtils.INICIANDO_BUSCA);
        Optional<Cartao> cartaoOptional = cartaoDao.findById(id);
        if (!cartaoOptional.isPresent()) {
            logger.info(ServiceUtils.CARTAO_NAO_ENCONTRADO);
            throw new JavaException(ServiceUtils.CARTAO_NAO_ENCONTRADO, HttpStatus.NOT_FOUND.value());
        }
        logger.info(ServiceUtils.SUCESSO);
        return cartaoOptional.get();
    }

    @Transactional
    @CacheEvict(cacheNames = {"Cartao", "Fatura", "Conta"}, allEntries = true)
    public void pagar(long id, PagamentoCartaoRequest pagamento) {
        logger.info(ServiceUtils.VERIFICANDO_DADOS_DO_PAGAMENTO);
        cartaoDao.pagar(id, pagamento);
        logger.info(ServiceUtils.SUCESSO);
    }

    @CacheEvict(allEntries = true)
    public void aumentarLimiteCredito(Long id, double valor) {
        logger.info(ServiceUtils.VERIFICANDO_DADOS_DO_PAGAMENTO);
        cartaoDao.aumentarLimiteCredito(id, valor);
        logger.info(ServiceUtils.SUCESSO);
    }

    @CacheEvict(allEntries = true)
    public void aumentarLimiteDebito(Long id, double valor) {
        logger.info(ServiceUtils.VERIFICANDO_DADOS_DO_PAGAMENTO);
        cartaoDao.aumentarLimiteDebito(id, valor);
        logger.info(ServiceUtils.SUCESSO);
    }

    @CacheEvict(allEntries = true)
    public void mudarStatus(Long id) {
        logger.info(ServiceUtils.MUDANDO_STATUS + id);
        cartaoDao.mudarStatus(id);
        logger.info(ServiceUtils.STATUS_ALTERADO);
    }

    @CacheEvict(allEntries = true)
    public void atualizarSenha(Long id, long senha) {
        logger.info(ServiceUtils.ATUALIZANDO_SENHA + id);
        cartaoDao.atualizarSenha(id, senha);
        logger.info(ServiceUtils.SENHA_ATUALIZADA);
    }

    @Cacheable(value = "Fatura", key = "#id")
    public double buscarFatura(Long id) {
        logger.info(ServiceUtils.BUSCANDO_FATURA + id);
        fatura = cartaoDao.buscarFatura(id);
        logger.info(ServiceUtils.SUCESSO);
        return fatura;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "Cartao", allEntries = true),
            @CacheEvict(value = "Fatura", key = "#id")
    })
    public void pagarFatura(Long id, Long senha) {
        logger.info(ServiceUtils.PAGANDO_FATURA, id);
        cartaoDao.pagarFatura(id, senha);
        logger.info(ServiceUtils.SUCESSO);

    }

    private void verStatus(Cartao cartao) {
        logger.info(ServiceUtils.VERIFICANDO_DADOS_DO_CARTAO);
        if (!cartao.isAtivoOuNao()) {
            logger.info(ServiceUtils.CARTAO_INATIVO);
            throw new JavaException(ServiceUtils.CARTAO_INATIVO, HttpStatus.BAD_REQUEST.value());
        }
        logger.info(ServiceUtils.SUCESSO);
    }

    private void verificarValor(Cartao cartao, PagamentoCartaoRequest pagamento) {
        logger.info(ServiceUtils.VERIFICANDO_DADOS_DO_PAGAMENTO);
        if (!pagamento.getSenha().equals(cartao.getSenha())) {
            logger.info(ServiceUtils.SENHA_INCORRETA);
            throw new JavaException(ServiceUtils.SENHA_INCORRETA, HttpStatus.UNAUTHORIZED.value());
        }
        if (pagamento.getPagamento() < 0) {
            logger.info(ServiceUtils.VALOR_NEGATIVO);
            throw new JavaException(ServiceUtils.VALOR_NEGATIVO, HttpStatus.NOT_ACCEPTABLE.value());
        }

        if (cartao instanceof CartaoDeCredito) {
            logger.info(ServiceUtils.CARTAO_DE_CREDITO_SELECIONADO + cartao.getId());
            if (pagamento.getPagamento() > ((CartaoDeCredito) cartao).getLimiteCredito()) {
                logger.info(ServiceUtils.LIMITE_INSUFICIENTE);
                throw new JavaException(ServiceUtils.LIMITE_INSUFICIENTE, HttpStatus.BAD_REQUEST.value());
            }

            ((CartaoDeCredito) cartao).setFatura(((CartaoDeCredito) cartao).getFatura() + pagamento.getPagamento());
            cartaoDao.save(cartao);
            logger.info(ServiceUtils.SUCESSO);

        } else if (cartao instanceof CartaoDeDebito) {
            logger.info(ServiceUtils.CARTAO_DE_DEBITO_SELECIONADO, cartao.getId());

            if (pagamento.getPagamento() > ((CartaoDeDebito) cartao).getLimiteDiario()) {
                logger.info(ServiceUtils.LIMITE_INSUFICIENTE);
                throw new JavaException(ServiceUtils.LIMITE_INSUFICIENTE, HttpStatus.NOT_ACCEPTABLE.value());
            }

            if (cartao.getConta().getSaldo() < pagamento.getPagamento()) {
                logger.info(ServiceUtils.SALDO_INSUFICIENTE);
                throw new JavaException(ServiceUtils.SALDO_INSUFICIENTE, HttpStatus.NOT_ACCEPTABLE.value());
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
}