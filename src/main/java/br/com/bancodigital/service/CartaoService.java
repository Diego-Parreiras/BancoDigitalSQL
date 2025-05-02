package br.com.bancodigital.service;

import br.com.bancodigital.dao.daoextends.CartaoDaoExtends;
import br.com.bancodigital.dao.daoextends.ContaDaoExtends;
import br.com.bancodigital.dao.interfaces.CartaoDao;
import br.com.bancodigital.dao.interfaces.ContaDao;
import br.com.bancodigital.model.Cartao;
import br.com.bancodigital.model.CartaoDeCredito;
import br.com.bancodigital.model.CartaoDeDebito;
import br.com.bancodigital.model.dto.PagamentoCartaoRequest;
import br.com.bancodigital.model.enuns.TipoCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class CartaoService {
    @Autowired
    CartaoDaoExtends cartaoDao;
    @Autowired
    ContaDaoExtends contadao;
    private final Random random = new Random();

    public void novoCartao(Cartao cartao) {
        validarDados(cartao);
        popularCartao(cartao);
        cartaoDao.save(cartao);
    }

    private void popularCartao(Cartao cartao) {
        /*Completa os dados do cartao */
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

    }

    public Cartao buscarId(Long id) {
        Optional<Cartao> cartaoOptional = cartaoDao.findById(id);
        if (!cartaoOptional.isPresent()) {
            throw new RuntimeException("Cartao nao encontrado");
        }
        return cartaoOptional.get();
    }

    public void pagar(long id, PagamentoCartaoRequest pagamento) {
        /*Metodo de pagamento padrao do cartao*/
        Cartao cartao = buscarId(id);
        verStatus(cartao);
        verificarValor(cartao, pagamento);
    }

    public void aumentarLimiteCredito(Long id, double valor) {
        /*Pede um novo limite do cartao de credito*/
        if (valor < 0) {
            throw new RuntimeException("Valor nao pode ser negativo");
        }
        Cartao cartao = buscarId(id);
        verStatus(cartao);
        if (cartao instanceof CartaoDeCredito) {
            ((CartaoDeCredito) cartao).setLimiteCredito(valor);
        }
        throw new RuntimeException("Seu cartao nao e de credito");
    }

    public void aumentarLimiteDebito(Long id, double valor) {

        if (valor < 0) {
            throw new RuntimeException("Valor nao pode ser negativo");
        }
        Cartao cartao = buscarId(id);
        verStatus(cartao);
        if (cartao instanceof CartaoDeDebito) {
            ((CartaoDeDebito) cartao).setLimiteDiario(valor);
        }
        throw new RuntimeException("Seu cartao nao e de credito");
    }

    public void mudarStatus(Long id) {
        /*Desativa ou aticva o cartao*/
        Cartao cartao = buscarId(id);

        if (cartao != null) {
            if (cartao instanceof CartaoDeCredito) {
                if (((CartaoDeCredito) cartao).getFatura() != 0) {
                    throw new RuntimeException("Preciso pagar a fatura primeiro");
                }
            }
            cartao.setAtivoOuNao(!cartao.isAtivoOuNao());
            cartaoDao.save(cartao);
        } else {
            throw new RuntimeException("Cartao nao encontrado");
        }
    }

    private void validarDados(Cartao cartao) {
        /*Verifica se o cartao ja esta cadastrado*/
        if (cartaoDao.existsByNumero(cartao.getNumero())) {
            throw new RuntimeException("Cartao ja cadastrado");
        }
    }

    private void verStatus(Cartao cartao) {
        /*verifica se o cartao esta ativo*/
        if (!cartao.isAtivoOuNao()) {
            throw new RuntimeException("Cartao inativo");
        }
    }

    private void verificarValor(Cartao cartao, PagamentoCartaoRequest pagamento) {
        /*Verifica se o valor e valido para pagamemto */
        if (!pagamento.getSenha().equals(cartao.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }
        if (pagamento.getPagamento() < 0) {
            throw new RuntimeException("Valor nao pode ser negativo");
        }
        if (cartao instanceof CartaoDeCredito) {
            if (pagamento.getPagamento() > ((CartaoDeCredito) cartao).getLimiteCredito()) {
                throw new RuntimeException("Limite insuficiente");
            }
            ((CartaoDeCredito) cartao).setFatura(((CartaoDeCredito) cartao).getFatura() + pagamento.getPagamento());
            cartaoDao.save(cartao);
        } else if (cartao instanceof CartaoDeDebito) {
            if (pagamento.getPagamento() > ((CartaoDeDebito) cartao).getLimiteDiario()) {
                throw new RuntimeException("Limite insuficiente");
            }
            if (cartao.getConta().getSaldo() < pagamento.getPagamento()) {
                throw new RuntimeException("Saldo insuficiente");
            }

            ((CartaoDeDebito) cartao).setLimiteDiario(((CartaoDeDebito) cartao).getLimiteDiario() - pagamento.getPagamento());
            cartao.getConta().setSaldo(cartao.getConta().getSaldo() - pagamento.getPagamento());

            contadao.save(cartao.getConta());
            cartaoDao.save(cartao);
        }

    }

    public void atualizarSenha(Long id, long senha) {
        /*Atualiza a senha do cartao*/
        Cartao cartao = buscarId(id);
        cartao.setSenha(senha);
        cartaoDao.save(cartao);
    }

    public double buscarFatura(Long id) {
        /*Encontra a fatura*/
        Cartao cartao = buscarId(id);
        if (cartao instanceof CartaoDeCredito) {
            return ((CartaoDeCredito) cartao).getFatura();
        }
        throw new RuntimeException("Cartao de debito");
    }

    public void pagarFatura(Long id) {
        /*Paga a fatura*/
        Cartao cartao = buscarId(id);
        if (cartao instanceof CartaoDeCredito) {
            if (((CartaoDeCredito) cartao).getFatura() == 0) {
                throw new RuntimeException("Fatura ja paga");
            }
            if (((CartaoDeCredito) cartao).getFatura() >= ((CartaoDeCredito) cartao).getLimiteCredito() * 0.8) {
                ((CartaoDeCredito) cartao).setFatura(((CartaoDeCredito) cartao).getFatura() + ((CartaoDeCredito) cartao).getLimiteCredito() * 0.05);
            }
            if (((CartaoDeCredito) cartao).getFatura() > cartao.getConta().getSaldo()) {
                throw new RuntimeException("Saldo insuficiente");
            } else {
                cartao.getConta().setSaldo(cartao.getConta().getSaldo() - ((CartaoDeCredito) cartao).getFatura());
                ((CartaoDeCredito) cartao).setFatura(0);
                cartaoDao.save(cartao);
                contadao.save(cartao.getConta());

            }
        }
    }
}