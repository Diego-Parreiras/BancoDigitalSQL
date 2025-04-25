# 💳 Projeto Banco Digital — Código de Base

Este é um projeto desenvolvido como parte do curso **Código de Base** da **EDUC360**, simulando um sistema bancário completo com funcionalidades essenciais como cadastro de clientes, gerenciamento de contas e emissão de cartões, tudo baseado em regras reais de negócio para bancos digitais.

---

## 📌 Descrição

O projeto consiste em uma **API REST** desenvolvida com **Java + Spring Boot** que simula operações bancárias comuns. Cada cliente pode possuir múltiplas contas (corrente ou poupança) e cartões (crédito e/ou débito), com regras específicas de manutenção, rendimento, limites e taxas, dependendo do seu tipo de cliente: **Comum, Super ou Premium**.

---

## 📋 Funcionalidades

### 👥 Clientes
- Cadastro, atualização, exclusão e busca de clientes.
- Classificação do cliente em Comum, Super ou Premium.
- **Validações implementadas:**
  - CPF único e válido.
  - Nome com letras e espaços, 2 a 100 caracteres.
  - Data de nascimento válida e maior de 18 anos.
  - Endereço completo com CEP válido (xxxxx-xxx).

### 🏦 Contas
- Criação de contas Corrente e Poupança.
- Operações disponíveis:
  - Consulta de saldo
  - Saques e depósitos
  - Transferências via Pix e TED
  - Fechamento de conta
- **Taxas e Rendimentos:**
  - Conta Corrente: taxa mensal automática (R$12 Comum, R$8 Super, isento Premium)
  - Conta Poupança: rendimento mensal baseado em juros compostos (0.5% a 0.9% ao ano)

### 💳 Cartões
- Emissão de cartões de crédito e/ou débito.
- Operações disponíveis:
  - Realizar pagamento
  - Consultar e pagar fatura (crédito)
  - Alterar senha
  - Ativar e desativar cartão
  - Ajustar limite de crédito e limite diário (débito)

---

## 🔗 Endpoints da API (usados no Postman)

### 🧑‍💼 Clientes
- `POST /clientes/cadastrar` — Cadastrar novo cliente
- `GET /clientes/buscar/{id}` — Buscar cliente por ID
- `PUT /clientes/atualizar/{id}` — Atualizar cliente
- `DELETE /clientes/apagar/{id}` — Remover cliente
- `GET /clientes/buscar-todos` — Listar todos os clientes

### 💼 Contas
- `POST /conta/criar-conta` — Criar nova conta
- `POST /conta/transferencia-pix` — Realizar Pix
- `POST /conta/transferencia-ted` — Realizar TED
- `PUT /conta/depositar/{id}` — Depositar valor
- `PUT /conta/sacar/{id}` — Sacar valor
- `GET /conta/consultar-saldo/{id}` — Consultar saldo
- `GET /conta/minha-conta/{id}` — Buscar detalhes da conta
- `PUT /conta/manutencao/{id}` — Aplicar taxa de manutenção
- `PUT /conta/rendimento/{id}` — Aplicar rendimento da poupança
- `DELETE /conta/fechar-conta/{id}` — Fechar conta

### 💳 Cartões
- `POST /cartao/novo-cartao-debito` — Emitir cartão de débito
- `POST /cartao/novo-cartao-credito` — Emitir cartão de crédito
- `GET /cartao/buscar-cartao/{id}` — Consultar cartão
- `POST /cartao/pagamento/{id}` — Efetuar pagamento
- `PUT /cartao/aumenta-limite-credito/{id}` — Aumentar limite de crédito
- `PUT /cartai/aumentar-limite-debito/{id}` — Aumentar limite de débito *(possível erro de digitação em "cartai")*
- `PUT /cartao/status/{id}` — Ativar/Desativar cartão
- `PUT /cartao/atualizar-senha/{id}` — Alterar senha do cartão
- `GET /cartao/buscar-fatura/{id}` — Ver fatura do cartão de crédito
- `POST /cartao/pagar-fatura/{id}` — Pagar fatura do cartão de crédito

---

## 🧰 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Maven
- Spring Data JPA
- Lombok
- Banco de dados H2 (modo console)

---

## 📈 Futuras Implementações

- Interface Web (Frontend)
- Autenticação por token (JWT)
- Integração com APIs externas (validação CPF, câmbio)

---

## ▶️ Como Executar

```bash
# Clone o projeto
git clone https://github.com/seu-usuario/banco-digital.git

# Acesse o diretório do projeto
cd banco-digital

# Compile e execute
./mvnw spring-boot:run
```
Acesse o console do banco de dados H2:
📌 http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:SpringDiego
Usuário: sa
Senha: (em branco)

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)