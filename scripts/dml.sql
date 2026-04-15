-- ============================================================
-- 1. INSERT - Inserção de dados nas tabelas
-- ============================================================

-- ------------------------------------------------------------
-- Inserções na tabela T_EMPRESA
-- ------------------------------------------------------------
INSERT INTO T_EMPRESA (cnpj, razao_social)
VALUES ('00.000.000/0001-00', 'Voltz Investimentos LTDA');

INSERT INTO T_EMPRESA (cnpj, razao_social)
VALUES ('11.111.111/0001-11', 'Tech Investimentos LTDA');

INSERT INTO T_EMPRESA (cnpj, razao_social)
VALUES ('22.222.222/0001-22', 'Crypto Corp SA');

INSERT INTO T_EMPRESA (cnpj, razao_social)
VALUES ('33.333.333/0001-33', 'Blockchain Solutions LTDA');

INSERT INTO T_EMPRESA (cnpj, razao_social)
VALUES ('44.444.444/0001-44', 'Digital Assets Inc');

-- ------------------------------------------------------------
-- Inserções na tabela T_USUARIO
-- ------------------------------------------------------------
INSERT INTO T_USUARIO (nome, email, senha_hash, papel, id_empresa)
VALUES ('Tio Patinhas', 'tiopatinhas@voltz.com', 'hash_senha_segura_123', 'diretor', 1);

INSERT INTO T_USUARIO (nome, email, senha_hash, papel, id_empresa)
VALUES ('Donald Duck', 'donald@voltz.com', 'hash_senha_456', 'gestor', 1);

INSERT INTO T_USUARIO (nome, email, senha_hash, papel, id_empresa)
VALUES ('Huguinho Silva', 'huguinho@tech.com', 'hash_senha_789', 'operador', 2);

INSERT INTO T_USUARIO (nome, email, senha_hash, papel, id_empresa)
VALUES ('Zezinho Costa', 'zezinho@crypto.com', 'hash_senha_abc', 'admin', 3);

INSERT INTO T_USUARIO (nome, email, senha_hash, papel, id_empresa)
VALUES ('Luisinho Souza', 'luisinho@blockchain.com', 'hash_senha_def', 'gestor', 4);

-- ------------------------------------------------------------
-- Inserções na tabela T_CARTEIRA
-- ------------------------------------------------------------
INSERT INTO T_CARTEIRA (endereco, custodiante, saldo, id_empresa)
VALUES ('0x1234567890abcdef', 'Coinbase Custody', 50000.00, 1);

INSERT INTO T_CARTEIRA (endereco, custodiante, saldo, id_empresa)
VALUES ('0xabcdef1234567890', 'Fireblocks', 120000.00, 1);

INSERT INTO T_CARTEIRA (endereco, custodiante, saldo, id_empresa)
VALUES ('0x9876543210fedcba', 'BitGo', 75000.00, 2);

INSERT INTO T_CARTEIRA (endereco, custodiante, saldo, id_empresa)
VALUES ('0xfedcba0987654321', 'Anchorage Digital', 200000.00, 3);

INSERT INTO T_CARTEIRA (endereco, custodiante, saldo, id_empresa)
VALUES ('0x1111aaaa2222bbbb', 'Copper.co', 30000.00, 4);

-- ------------------------------------------------------------
-- Inserções na tabela T_INVESTIMENTO
-- ------------------------------------------------------------
INSERT INTO T_INVESTIMENTO (ativo, quantidade, preco_medio, id_carteira)
VALUES ('BTC', 0.5, 150000.00, 1);

INSERT INTO T_INVESTIMENTO (ativo, quantidade, preco_medio, id_carteira)
VALUES ('ETH', 2.0, 8000.00, 1);

INSERT INTO T_INVESTIMENTO (ativo, quantidade, preco_medio, id_carteira)
VALUES ('SOL', 50.0, 200.00, 1);

INSERT INTO T_INVESTIMENTO (ativo, quantidade, preco_medio, id_carteira)
VALUES ('BTC', 1.2, 145000.00, 2);

INSERT INTO T_INVESTIMENTO (ativo, quantidade, preco_medio, id_carteira)
VALUES ('ADA', 5000.0, 2.50, 3);

INSERT INTO T_INVESTIMENTO (ativo, quantidade, preco_medio, id_carteira)
VALUES ('ETH', 10.0, 7500.00, 4);

INSERT INTO T_INVESTIMENTO (ativo, quantidade, preco_medio, id_carteira)
VALUES ('DOT', 300.0, 35.00, 4);

INSERT INTO T_INVESTIMENTO (ativo, quantidade, preco_medio, id_carteira)
VALUES ('BTC', 0.3, 155000.00, 5);

-- ------------------------------------------------------------
-- Inserções na tabela T_TRANSACAO
-- ------------------------------------------------------------
INSERT INTO T_TRANSACAO (tipo, ativo, quantidade, preco, data_transacao, status, id_carteira, id_usuario, id_investimento)
VALUES ('COMPRA', 'BTC', 0.5, 150000.00, TIMESTAMP '2025-10-15 10:30:00', 'CONFIRMADA', 1, 1, 1);

INSERT INTO T_TRANSACAO (tipo, ativo, quantidade, preco, data_transacao, status, id_carteira, id_usuario, id_investimento)
VALUES ('COMPRA', 'ETH', 2.0, 8000.00, TIMESTAMP '2025-10-16 14:20:00', 'CONFIRMADA', 1, 1, 2);

INSERT INTO T_TRANSACAO (tipo, ativo, quantidade, preco, data_transacao, status, id_carteira, id_usuario, id_investimento)
VALUES ('DEPOSITO', 'BRL', 50000.0, 1.00, TIMESTAMP '2025-10-10 09:00:00', 'CONFIRMADA', 1, 1, NULL);

INSERT INTO T_TRANSACAO (tipo, ativo, quantidade, preco, data_transacao, status, id_carteira, id_usuario, id_investimento)
VALUES ('COMPRA', 'SOL', 50.0, 200.00, TIMESTAMP '2025-10-18 11:45:00', 'CONFIRMADA', 1, 2, 3);

INSERT INTO T_TRANSACAO (tipo, ativo, quantidade, preco, data_transacao, status, id_carteira, id_usuario, id_investimento)
VALUES ('COMPRA', 'BTC', 1.2, 145000.00, TIMESTAMP '2025-10-20 16:00:00', 'CONFIRMADA', 2, 2, 4);

INSERT INTO T_TRANSACAO (tipo, ativo, quantidade, preco, data_transacao, status, id_carteira, id_usuario, id_investimento)
VALUES ('VENDA', 'ETH', 1.0, 8500.00, TIMESTAMP '2025-11-01 13:30:00', 'CONFIRMADA', 4, 4, 6);

INSERT INTO T_TRANSACAO (tipo, ativo, quantidade, preco, data_transacao, status, id_carteira, id_usuario, id_investimento)
VALUES ('SAQUE', 'BRL', 10000.0, 1.00, TIMESTAMP '2025-11-05 08:15:00', 'PENDENTE', 3, 3, NULL);

INSERT INTO T_TRANSACAO (tipo, ativo, quantidade, preco, data_transacao, status, id_carteira, id_usuario, id_investimento)
VALUES ('COMPRA', 'DOT', 300.0, 35.00, TIMESTAMP '2025-11-10 10:00:00', 'CONFIRMADA', 4, 4, 7);

-- ------------------------------------------------------------
-- Inserções na tabela T_RELATORIO
-- ------------------------------------------------------------
INSERT INTO T_RELATORIO (titulo, data_geracao, tipo, id_empresa, id_usuario)
VALUES ('Relatório Mensal - Outubro 2025', TIMESTAMP '2025-11-01 08:00:00', 'CONSOLIDADO', 1, 1);

INSERT INTO T_RELATORIO (titulo, data_geracao, tipo, id_empresa, id_usuario)
VALUES ('Relatório de Carteira - Fireblocks', TIMESTAMP '2025-11-05 10:30:00', 'CARTEIRA', 1, 2);

INSERT INTO T_RELATORIO (titulo, data_geracao, tipo, id_empresa, id_usuario)
VALUES ('Relatório de Performance Q4', TIMESTAMP '2025-12-01 09:00:00', 'PERFORMANCE', 3, 4);

INSERT INTO T_RELATORIO (titulo, data_geracao, tipo, id_empresa, id_usuario)
VALUES ('Relatório Consolidado - Tech Invest', TIMESTAMP '2025-11-15 14:00:00', 'CONSOLIDADO', 2, 3);

-- ------------------------------------------------------------
-- Inserções na tabela T_RELATORIO_INVESTIMENTO
-- ------------------------------------------------------------
-- Relatório 1 (Consolidado Voltz) contém BTC, ETH e SOL da carteira 1
INSERT INTO T_RELATORIO_INVESTIMENTO (id_relatorio, id_investimento, data_inclusao, preco_mercado_capturado, observacoes)
VALUES (1, 1, TIMESTAMP '2025-11-01 08:05:00', 160000.00, 'BTC com valorização de 6.67%');

INSERT INTO T_RELATORIO_INVESTIMENTO (id_relatorio, id_investimento, data_inclusao, preco_mercado_capturado, observacoes)
VALUES (1, 2, TIMESTAMP '2025-11-01 08:05:00', 8500.00, 'ETH estável');

INSERT INTO T_RELATORIO_INVESTIMENTO (id_relatorio, id_investimento, data_inclusao, preco_mercado_capturado, observacoes)
VALUES (1, 3, TIMESTAMP '2025-11-01 08:05:00', 220.00, 'SOL com tendência de alta');

-- Relatório 2 (Carteira Fireblocks) contém BTC da carteira 2
INSERT INTO T_RELATORIO_INVESTIMENTO (id_relatorio, id_investimento, data_inclusao, preco_mercado_capturado, observacoes)
VALUES (2, 4, TIMESTAMP '2025-11-05 10:35:00', 158000.00, 'BTC posição principal');

-- Relatório 3 (Performance Q4) contém ETH e DOT da carteira 4
INSERT INTO T_RELATORIO_INVESTIMENTO (id_relatorio, id_investimento, data_inclusao, preco_mercado_capturado, observacoes)
VALUES (3, 6, TIMESTAMP '2025-12-01 09:10:00', 9200.00, 'ETH com boa performance no Q4');

INSERT INTO T_RELATORIO_INVESTIMENTO (id_relatorio, id_investimento, data_inclusao, preco_mercado_capturado, observacoes)
VALUES (3, 7, TIMESTAMP '2025-12-01 09:10:00', 42.00, 'DOT valorizou 20%');

-- Relatório 4 (Consolidado Tech) contém ADA da carteira 3
INSERT INTO T_RELATORIO_INVESTIMENTO (id_relatorio, id_investimento, data_inclusao, preco_mercado_capturado, observacoes)
VALUES (4, 5, TIMESTAMP '2025-11-15 14:10:00', 2.80, 'ADA com leve alta');

COMMIT;

-- ============================================================
-- 2. UPDATE - Atualização de dados
-- ============================================================

-- Atualizar a razão social de uma empresa
UPDATE T_EMPRESA
SET razao_social = 'Voltz Investimentos S.A.'
WHERE cnpj = '00.000.000/0001-00';

-- Atualizar o saldo de uma carteira após um depósito
UPDATE T_CARTEIRA
SET saldo = 60000.00
WHERE endereco = '0x1234567890abcdef';

-- Atualizar o papel de um usuário (promoção)
UPDATE T_USUARIO
SET papel = 'admin'
WHERE email = 'donald@voltz.com';

-- Atualizar o preço médio de um investimento após nova compra
UPDATE T_INVESTIMENTO
SET preco_medio = 152000.00,
    quantidade = 0.8
WHERE id_investimento = 1;

-- Atualizar o status de uma transação pendente para confirmada
UPDATE T_TRANSACAO
SET status = 'CONFIRMADA'
WHERE status = 'PENDENTE'
  AND id_transacao = 7;

-- Atualizar a observação de um item do relatório
UPDATE T_RELATORIO_INVESTIMENTO
SET observacoes = 'BTC com valorização expressiva de 6.67% no período',
    preco_mercado_capturado = 162000.00
WHERE id_relatorio = 1
  AND id_investimento = 1;

COMMIT;

-- ============================================================
-- 3. DELETE - Exclusão de dados
-- ============================================================

-- Remover uma associação específica do relatório
DELETE FROM T_RELATORIO_INVESTIMENTO
WHERE id_relatorio = 4
  AND id_investimento = 5;

-- Remover o relatório que ficou sem investimentos associados
DELETE FROM T_RELATORIO
WHERE id_relatorio = 4;

-- Remover uma transação cancelada (exemplo)
DELETE FROM T_TRANSACAO
WHERE id_transacao = 7;

COMMIT;

-- ============================================================
-- 4. SELECT - Consultas de dados
-- ============================================================

-- 4.1 Listar todas as empresas cadastradas
SELECT id_empresa, cnpj, razao_social
FROM T_EMPRESA
ORDER BY razao_social;

-- 4.2 Listar todos os usuários com o nome da empresa
SELECT u.id_usuario,
       u.nome,
       u.email,
       u.papel,
       e.razao_social AS empresa
FROM T_USUARIO u
INNER JOIN T_EMPRESA e ON u.id_empresa = e.id_empresa
ORDER BY u.nome;

-- 4.3 Listar todas as carteiras com o saldo e empresa dona
SELECT c.id_carteira,
       c.endereco,
       c.custodiante,
       c.saldo,
       e.razao_social AS empresa
FROM T_CARTEIRA c
INNER JOIN T_EMPRESA e ON c.id_empresa = e.id_empresa
ORDER BY c.saldo DESC;

-- 4.4 Listar investimentos detalhados com carteira e empresa
SELECT i.id_investimento,
       i.ativo,
       i.quantidade,
       i.preco_medio,
       (i.quantidade * i.preco_medio) AS valor_investido,
       c.endereco AS carteira,
       c.custodiante,
       e.razao_social AS empresa
FROM T_INVESTIMENTO i
INNER JOIN T_CARTEIRA c ON i.id_carteira = c.id_carteira
INNER JOIN T_EMPRESA e ON c.id_empresa = e.id_empresa
ORDER BY valor_investido DESC;

-- 4.5 Listar transações com detalhes de carteira e usuário
SELECT t.id_transacao,
       t.tipo,
       t.ativo,
       t.quantidade,
       t.preco,
       (t.quantidade * t.preco) AS valor_total,
       t.data_transacao,
       t.status,
       u.nome AS usuario,
       c.endereco AS carteira
FROM T_TRANSACAO t
INNER JOIN T_USUARIO u ON t.id_usuario = u.id_usuario
INNER JOIN T_CARTEIRA c ON t.id_carteira = c.id_carteira
ORDER BY t.data_transacao DESC;

-- 4.6 Relatórios com seus investimentos (via tabela associativa N:N)
SELECT r.titulo AS relatorio,
       r.tipo AS tipo_relatorio,
       r.data_geracao,
       i.ativo,
       i.quantidade,
       ri.preco_mercado_capturado AS preco_mercado,
       (i.quantidade * ri.preco_mercado_capturado) AS valor_mercado,
       ri.observacoes
FROM T_RELATORIO r
INNER JOIN T_RELATORIO_INVESTIMENTO ri ON r.id_relatorio = ri.id_relatorio
INNER JOIN T_INVESTIMENTO i ON ri.id_investimento = i.id_investimento
ORDER BY r.data_geracao, i.ativo;

-- 4.7 Saldo total por empresa (soma dos saldos das carteiras)
SELECT e.razao_social AS empresa,
       COUNT(c.id_carteira) AS total_carteiras,
       SUM(c.saldo) AS saldo_total
FROM T_EMPRESA e
LEFT JOIN T_CARTEIRA c ON e.id_empresa = c.id_empresa
GROUP BY e.razao_social
ORDER BY saldo_total DESC;

-- 4.8 Valor total investido por ativo (consolidado entre todas as carteiras)
SELECT i.ativo,
       SUM(i.quantidade) AS quantidade_total,
       ROUND(AVG(i.preco_medio), 2) AS preco_medio_geral,
       SUM(i.quantidade * i.preco_medio) AS valor_total_investido
FROM T_INVESTIMENTO i
GROUP BY i.ativo
ORDER BY valor_total_investido DESC;

-- 4.9 Histórico de transações por tipo (total movimentado)
SELECT t.tipo,
       COUNT(*) AS total_transacoes,
       SUM(t.quantidade * t.preco) AS valor_total_movimentado
FROM T_TRANSACAO t
WHERE t.status = 'CONFIRMADA'
GROUP BY t.tipo
ORDER BY valor_total_movimentado DESC;

-- 4.10 Usuários que possuem papel de 'admin' ou 'diretor'
SELECT u.nome,
       u.email,
       u.papel,
       e.razao_social AS empresa
FROM T_USUARIO u
INNER JOIN T_EMPRESA e ON u.id_empresa = e.id_empresa
WHERE u.papel IN ('admin', 'diretor')
ORDER BY u.nome;

-- ============================================================
-- FIM DO SCRIPT DML
-- ============================================================
