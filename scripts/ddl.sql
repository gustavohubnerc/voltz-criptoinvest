-- ============================================================
-- 1. DROP - Remoção das tabelas (ordem inversa de dependência)
-- ============================================================

DROP TABLE T_RELATORIO_INVESTIMENTO CASCADE CONSTRAINTS;
DROP TABLE T_TRANSACAO CASCADE CONSTRAINTS;
DROP TABLE T_RELATORIO CASCADE CONSTRAINTS;
DROP TABLE T_INVESTIMENTO CASCADE CONSTRAINTS;
DROP TABLE T_CARTEIRA CASCADE CONSTRAINTS;
DROP TABLE T_USUARIO CASCADE CONSTRAINTS;
DROP TABLE T_EMPRESA CASCADE CONSTRAINTS;

-- ============================================================
-- 2. CREATE - Criação das tabelas
-- ============================================================

-- ------------------------------------------------------------
-- Tabela: T_EMPRESA
-- Descrição: Representa uma entidade jurídica que investe
--            em criptoativos.
-- PK: id_empresa
-- ------------------------------------------------------------
CREATE TABLE T_EMPRESA (
    id_empresa      NUMBER          GENERATED ALWAYS AS IDENTITY,
    cnpj            VARCHAR2(18)    NOT NULL,
    razao_social    VARCHAR2(100)   NOT NULL,

    CONSTRAINT PK_EMPRESA PRIMARY KEY (id_empresa),
    CONSTRAINT UK_EMPRESA_CNPJ UNIQUE (cnpj)
);

-- ------------------------------------------------------------
-- Tabela: T_USUARIO
-- Descrição: Representa uma pessoa autorizada para acessar
--            o sistema. Herda atributos de Pessoa (nome, email).
-- PK: id_usuario
-- FK: id_empresa -> T_EMPRESA(id_empresa)
-- ------------------------------------------------------------
CREATE TABLE T_USUARIO (
    id_usuario      NUMBER          GENERATED ALWAYS AS IDENTITY,
    nome            VARCHAR2(100)   NOT NULL,
    email           VARCHAR2(100)   NOT NULL,
    senha_hash      VARCHAR2(255)   NOT NULL,
    papel           VARCHAR2(30)    NOT NULL,
    id_empresa      NUMBER,

    CONSTRAINT PK_USUARIO PRIMARY KEY (id_usuario),
    CONSTRAINT UK_USUARIO_EMAIL UNIQUE (email)
);

-- ------------------------------------------------------------
-- Tabela: T_CARTEIRA
-- Descrição: Representa uma carteira digital vinculada
--            a uma empresa.
-- PK: id_carteira
-- FK: id_empresa -> T_EMPRESA(id_empresa)
-- ------------------------------------------------------------
CREATE TABLE T_CARTEIRA (
    id_carteira     NUMBER          GENERATED ALWAYS AS IDENTITY,
    endereco        VARCHAR2(100)   NOT NULL,
    custodiante     VARCHAR2(100)   NOT NULL,
    saldo           NUMBER(18,2)    DEFAULT 0 NOT NULL,
    id_empresa      NUMBER          NOT NULL,

    CONSTRAINT PK_CARTEIRA PRIMARY KEY (id_carteira),
    CONSTRAINT UK_CARTEIRA_ENDERECO UNIQUE (endereco)
);

-- ------------------------------------------------------------
-- Tabela: T_INVESTIMENTO
-- Descrição: Representa a alocação de um criptoativo
--            específico em uma carteira.
-- PK: id_investimento
-- FK: id_carteira -> T_CARTEIRA(id_carteira)
-- ------------------------------------------------------------
CREATE TABLE T_INVESTIMENTO (
    id_investimento NUMBER          GENERATED ALWAYS AS IDENTITY,
    ativo           VARCHAR2(10)    NOT NULL,
    quantidade      NUMBER(18,8)    NOT NULL,
    preco_medio     NUMBER(18,2)    NOT NULL,
    id_carteira     NUMBER          NOT NULL,

    CONSTRAINT PK_INVESTIMENTO PRIMARY KEY (id_investimento)
);

-- ------------------------------------------------------------
-- Tabela: T_TRANSACAO
-- Descrição: Representa uma operação financeira (compra,
--            venda, depósito ou saque) em uma carteira.
-- PK: id_transacao
-- FKs: id_carteira   -> T_CARTEIRA(id_carteira)
--       id_usuario    -> T_USUARIO(id_usuario)
--       id_investimento -> T_INVESTIMENTO(id_investimento)
-- ------------------------------------------------------------
CREATE TABLE T_TRANSACAO (
    id_transacao    NUMBER          GENERATED ALWAYS AS IDENTITY,
    tipo            VARCHAR2(20)    NOT NULL,
    ativo           VARCHAR2(10)    NOT NULL,
    quantidade      NUMBER(18,8)    NOT NULL,
    preco           NUMBER(18,2)    NOT NULL,
    data_transacao  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status          VARCHAR2(20)    DEFAULT 'CONFIRMADA' NOT NULL,
    id_carteira     NUMBER          NOT NULL,
    id_usuario      NUMBER          NOT NULL,
    id_investimento NUMBER,

    CONSTRAINT PK_TRANSACAO PRIMARY KEY (id_transacao),
    CONSTRAINT CK_TRANSACAO_TIPO CHECK (tipo IN ('COMPRA', 'VENDA', 'DEPOSITO', 'SAQUE')),
    CONSTRAINT CK_TRANSACAO_STATUS CHECK (status IN ('PENDENTE', 'CONFIRMADA', 'CANCELADA', 'FALHA'))
);

-- ------------------------------------------------------------
-- Tabela: T_RELATORIO
-- Descrição: Consolida investimentos e gera relatórios
--            de posição.
-- PK: id_relatorio
-- FKs: id_empresa -> T_EMPRESA(id_empresa)
--       id_usuario -> T_USUARIO(id_usuario)
-- ------------------------------------------------------------
CREATE TABLE T_RELATORIO (
    id_relatorio    NUMBER          GENERATED ALWAYS AS IDENTITY,
    titulo          VARCHAR2(150)   NOT NULL,
    data_geracao    TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    tipo            VARCHAR2(30)    NOT NULL,
    id_empresa      NUMBER          NOT NULL,
    id_usuario      NUMBER          NOT NULL,

    CONSTRAINT PK_RELATORIO PRIMARY KEY (id_relatorio),
    CONSTRAINT CK_RELATORIO_TIPO CHECK (tipo IN ('CONSOLIDADO', 'CARTEIRA', 'PERFORMANCE'))
);

-- ------------------------------------------------------------
-- Tabela Associativa: T_RELATORIO_INVESTIMENTO
-- Descrição: Relacionamento N:N entre Relatório e
--            Investimento. Permite que um relatório contenha
--            múltiplos investimentos e que um investimento
--            apareça em múltiplos relatórios.
-- PK composta: (id_relatorio, id_investimento)
-- FKs: id_relatorio    -> T_RELATORIO(id_relatorio)
--       id_investimento -> T_INVESTIMENTO(id_investimento)
-- ------------------------------------------------------------
CREATE TABLE T_RELATORIO_INVESTIMENTO (
    id_relatorio            NUMBER          NOT NULL,
    id_investimento         NUMBER          NOT NULL,
    data_inclusao           TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    preco_mercado_capturado NUMBER(18,2)    NOT NULL,
    observacoes             VARCHAR2(500),

    CONSTRAINT PK_RELATORIO_INVESTIMENTO PRIMARY KEY (id_relatorio, id_investimento)
);

-- ============================================================
-- 3. ALTER - Adição das Foreign Keys (Chaves Estrangeiras)
-- ============================================================

-- FK: T_USUARIO.id_empresa -> T_EMPRESA.id_empresa
ALTER TABLE T_USUARIO
    ADD CONSTRAINT FK_USUARIO_EMPRESA
    FOREIGN KEY (id_empresa) REFERENCES T_EMPRESA(id_empresa);

-- FK: T_CARTEIRA.id_empresa -> T_EMPRESA.id_empresa
ALTER TABLE T_CARTEIRA
    ADD CONSTRAINT FK_CARTEIRA_EMPRESA
    FOREIGN KEY (id_empresa) REFERENCES T_EMPRESA(id_empresa);

-- FK: T_INVESTIMENTO.id_carteira -> T_CARTEIRA.id_carteira
ALTER TABLE T_INVESTIMENTO
    ADD CONSTRAINT FK_INVESTIMENTO_CARTEIRA
    FOREIGN KEY (id_carteira) REFERENCES T_CARTEIRA(id_carteira);

-- FK: T_TRANSACAO.id_carteira -> T_CARTEIRA.id_carteira
ALTER TABLE T_TRANSACAO
    ADD CONSTRAINT FK_TRANSACAO_CARTEIRA
    FOREIGN KEY (id_carteira) REFERENCES T_CARTEIRA(id_carteira);

-- FK: T_TRANSACAO.id_usuario -> T_USUARIO.id_usuario
ALTER TABLE T_TRANSACAO
    ADD CONSTRAINT FK_TRANSACAO_USUARIO
    FOREIGN KEY (id_usuario) REFERENCES T_USUARIO(id_usuario);

-- FK: T_TRANSACAO.id_investimento -> T_INVESTIMENTO.id_investimento
ALTER TABLE T_TRANSACAO
    ADD CONSTRAINT FK_TRANSACAO_INVESTIMENTO
    FOREIGN KEY (id_investimento) REFERENCES T_INVESTIMENTO(id_investimento);

-- FK: T_RELATORIO.id_empresa -> T_EMPRESA.id_empresa
ALTER TABLE T_RELATORIO
    ADD CONSTRAINT FK_RELATORIO_EMPRESA
    FOREIGN KEY (id_empresa) REFERENCES T_EMPRESA(id_empresa);

-- FK: T_RELATORIO.id_usuario -> T_USUARIO.id_usuario
ALTER TABLE T_RELATORIO
    ADD CONSTRAINT FK_RELATORIO_USUARIO
    FOREIGN KEY (id_usuario) REFERENCES T_USUARIO(id_usuario);

-- FK: T_RELATORIO_INVESTIMENTO.id_relatorio -> T_RELATORIO.id_relatorio
ALTER TABLE T_RELATORIO_INVESTIMENTO
    ADD CONSTRAINT FK_REL_INV_RELATORIO
    FOREIGN KEY (id_relatorio) REFERENCES T_RELATORIO(id_relatorio);

-- FK: T_RELATORIO_INVESTIMENTO.id_investimento -> T_INVESTIMENTO.id_investimento
ALTER TABLE T_RELATORIO_INVESTIMENTO
    ADD CONSTRAINT FK_REL_INV_INVESTIMENTO
    FOREIGN KEY (id_investimento) REFERENCES T_INVESTIMENTO(id_investimento);

-- ============================================================
-- FIM DO SCRIPT DDL
-- ============================================================
