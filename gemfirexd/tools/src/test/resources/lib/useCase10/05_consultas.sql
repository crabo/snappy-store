-- Query 1: Total de Vendas por mes
SELECT	t.NOM_MES,
	SUM(n.VLR_NF) as SUM_VLR_NF
FROM	APP.NOTA_FISCAL n
INNER JOIN APP.TEMPO T ON (n.DAT_DIA = t.DAT_DIA)
GROUP BY t.NOM_MES

-- Query 2: Total de Vendas por revenda, por mes
SELECT	r.NOM_REVENDA,
		t.NOM_MES,
		SUM(n.VLR_NF) as SUM_VLR_NF
FROM	APP.NOTA_FISCAL n
INNER JOIN APP.REVENDA R ON (n.COD_REVENDA = r.COD_REVENDA)
INNER JOIN APP.TEMPO T ON (n.DAT_DIA = t.DAT_DIA)
GROUP BY r.NOM_REVENDA,t.NOM_MES

-- Query 3: TOP 10 Revendas que mais vendem
--SELECT	TOP 10
SELECT
		r.NOM_REVENDA,
		SUM(n.VLR_NF) as SUM_VLR_NF
FROM	APP.NOTA_FISCAL n
INNER JOIN APP.REVENDA R ON (n.COD_REVENDA = r.COD_REVENDA)
GROUP BY r.NOM_REVENDA
ORDER BY SUM(n.VLR_NF) DESC
FETCH FIRST 10 ROWS ONLY;

-- Query 4: Revendas com maior ticket medio
SELECT	TOP 10
	r.NOM_REVENDA,
	SUM(i.VLR_ITEM_NF) as SUM_VLR_NF,
	AVG(i.VLR_ITEM_NF) as VLR_TICKET
FROM	APP.NOTA_FISCAL n
INNER JOIN APP.ITEM_NOTA_FISCAL i ON (n.NUM_NF = i.NUM_NF)
INNER JOIN APP.REVENDA R ON (n.COD_REVENDA = r.COD_REVENDA)
GROUP BY r.NOM_REVENDA
ORDER BY AVG(i.VLR_ITEM_NF) DESC

-- Query 5: TOP 10 Vendedores
--SELECT	TOP 10
SELECT
		v.NOM_VENDEDOR,
		r.NOM_REVENDA,
		SUM(n.VLR_NF) as SUM_VLR_NF
FROM	APP.NOTA_FISCAL n
INNER JOIN APP.REVENDA R ON (n.COD_REVENDA = r.COD_REVENDA)
INNER JOIN APP.VENDEDOR V ON (r.COD_REVENDA = v.COD_REVENDA AND n.COD_VENDEDOR = v.COD_VENDEDOR AND n.COD_REVENDA = v.COD_REVENDA)
GROUP BY v.NOM_VENDEDOR,r.NOM_REVENDA
ORDER BY SUM(n.VLR_NF) DESC
FETCH FIRST 10 ROWS ONLY;
-- Query 6: TOP produtos mais vendidos
--SELECT	TOP 10
SELECT
	t.NOM_TIPO_PRODUTO,
	p.NOM_PRODUTO,
	SUM(VLR_ITEM_NF) as SUM_VLR_NF
FROM	APP.ITEM_NOTA_FISCAL n
INNER JOIN APP.PRODUTO p ON (n.COD_PRODUTO = p.COD_PRODUTO AND n.COD_TIPO_PRODUTO = p.COD_TIPO_PRODUTO)
INNER JOIN APP.TIPO_PRODUTO t ON (t.COD_TIPO_PRODUTO = n.COD_TIPO_PRODUTO)
GROUP BY t.NOM_TIPO_PRODUTO,p.NOM_PRODUTO
ORDER BY SUM(VLR_ITEM_NF) DESC
FETCH FIRST 10 ROWS ONLY;

-- Query 7: Total de Vendas para um mes especifico
SELECT	t.NOM_MES,
	SUM(n.VLR_NF) as SUM_VLR_NF
FROM	APP.NOTA_FISCAL n
INNER JOIN APP.TEMPO T ON (n.DAT_DIA = t.DAT_DIA)
WHERE t.NOM_MES = 'Abr/2013'
GROUP BY t.NOM_MES

-- Query 8: Total de Vendas por revenda, por mes
SELECT	r.NOM_REVENDA,
		t.NOM_MES,
		SUM(n.VLR_NF) as SUM_VLR_NF
FROM	APP.NOTA_FISCAL n
INNER JOIN APP.REVENDA R ON (n.COD_REVENDA = r.COD_REVENDA)
INNER JOIN APP.TEMPO T ON (n.DAT_DIA = t.DAT_DIA)
WHERE t.NOM_MES = 'Abr/2013'
GROUP BY r.NOM_REVENDA,t.NOM_MES

-- Query 9: Total de Vendas por tipo de produto, por mes
SELECT d.NOM_MES,
	tp.NOM_TIPO_PRODUTO,
	SUM(VLR_ITEM_NF) as SUM_VLR_NF
FROM	
	APP.ITEM_NOTA_FISCAL n
INNER JOIN 
	APP.TEMPO d ON (n.DAT_DIA = d.DAT_DIA)
INNER JOIN 
	APP.TIPO_PRODUTO tp ON (tp.COD_TIPO_PRODUTO = n.COD_TIPO_PRODUTO)
INNER JOIN 
	APP.PRODUTO p ON (n.COD_PRODUTO = p.COD_PRODUTO 
	AND n.COD_TIPO_PRODUTO = p.COD_TIPO_PRODUTO 
	AND p.COD_TIPO_PRODUTO = tp.COD_TIPO_PRODUTO
	)
GROUP BY d.NOM_MES, tp.NOM_TIPO_PRODUTO

SELECT d.NOM_MES,tp.NOM_TIPO_PRODUTO,SUM(VLR_ITEM_NF) as SUM_VLR_NF FROM APP.ITEM_NOTA_FISCAL n INNER JOIN APP.TEMPO d ON (n.DAT_DIA = d.DAT_DIA) INNER JOIN APP.TIPO_PRODUTO tp ON (tp.COD_TIPO_PRODUTO = n.COD_TIPO_PRODUTO) INNER JOIN APP.PRODUTO p ON (n.COD_PRODUTO = p.COD_PRODUTO AND n.COD_TIPO_PRODUTO = p.COD_TIPO_PRODUTO AND p.COD_TIPO_PRODUTO=tp.COD_TIPO_PRODUTO ) GROUP BY d.NOM_MES, tp.NOM_TIPO_PRODUTO

SELECT d.NOM_MES,tp.NOM_TIPO_PRODUTO,SUM(VLR_ITEM_NF) as SUM_VLR_NF FROM APP.ITEM_NOTA_FISCAL n  INNER JOIN APP.TIPO_PRODUTO tp ON (tp.COD_TIPO_PRODUTO = n.COD_TIPO_PRODUTO) INNER JOIN APP.PRODUTO p ON (n.COD_PRODUTO = p.COD_PRODUTO AND n.COD_TIPO_PRODUTO = p.COD_TIPO_PRODUTO AND p.COD_TIPO_PRODUTO=tp.COD_TIPO_PRODUTO ) INNER JOIN APP.TEMPO d ON (n.DAT_DIA = d.DAT_DIA)  GROUP BY d.NOM_MES, tp.NOM_TIPO_PRODUTO
	

-- Query 10: Total de Vendas de um produto (cod 6: Hoegaarden) por mes
SELECT d.NOM_MES,
	SUM(VLR_ITEM_NF) as SUM_VLR_NF
FROM	APP.ITEM_NOTA_FISCAL n
INNER JOIN APP.TEMPO d ON (n.DAT_DIA = d.DAT_DIA)
WHERE	n.COD_PRODUTO = 6
GROUP BY d.NOM_MES

-- Query 11: Total de Vendas de Cerveja por mes
SELECT d.NOM_MES,
	SUM(VLR_ITEM_NF) as SUM_VLR_NF
FROM	APP.ITEM_NOTA_FISCAL n
INNER JOIN APP.TEMPO d ON (n.DAT_DIA = d.DAT_DIA)
WHERE	n.COD_TIPO_PRODUTO = 1
GROUP BY d.NOM_MES

-- Query 12: Total de Vendas por produto, revenda e vendedor
--SELECT	TOP 10
SELECT
	v.NOM_VENDEDOR,
	r.NOM_REVENDA,
	tp.NOM_TIPO_PRODUTO,
	p.NOM_PRODUTO,
	SUM(n.VLR_ITEM_NF) as SUM_VLR_NF
FROM	
	APP.ITEM_NOTA_FISCAL n
INNER JOIN 
	APP.REVENDA R ON (n.COD_REVENDA = r.COD_REVENDA)
INNER JOIN 
	APP.VENDEDOR V ON (r.COD_REVENDA = v.COD_REVENDA AND n.COD_VENDEDOR = v.COD_VENDEDOR AND n.COD_REVENDA = v.COD_REVENDA)
INNER JOIN 
	APP.TIPO_PRODUTO tp ON (tp.COD_TIPO_PRODUTO = n.COD_TIPO_PRODUTO)
INNER JOIN 
	APP.PRODUTO p ON (n.COD_PRODUTO = p.COD_PRODUTO 
		AND n.COD_TIPO_PRODUTO = p.COD_TIPO_PRODUTO 
	--	AND p.COD_TIPO_PRODUTO = tp.COD_TIPO_PRODUTO
		)
INNER JOIN 
	APP.TEMPO d ON (n.DAT_DIA = d.DAT_DIA)
WHERE 
	d.NOM_MES = 'Abr/2013'
GROUP BY 
	v.NOM_VENDEDOR,r.NOM_REVENDA,tp.NOM_TIPO_PRODUTO,p.NOM_PRODUTO
ORDER BY 
	SUM(n.VLR_ITEM_NF) DESC
FETCH FIRST 10 ROWS ONLY;

SELECT v.NOM_VENDEDOR, r.NOM_REVENDA, tp.NOM_TIPO_PRODUTO,p.NOM_PRODUTO,SUM(n.VLR_ITEM_NF) as SUM_VLR_NF FROM APP.ITEM_NOTA_FISCAL n INNER JOIN APP.REVENDA R ON (n.COD_REVENDA = r.COD_REVENDA) INNER JOIN APP.VENDEDOR V ON (r.COD_REVENDA = v.COD_REVENDA AND n.COD_VENDEDOR = v.COD_VENDEDOR AND n.COD_REVENDA = v.COD_REVENDA) INNER JOIN APP.TIPO_PRODUTO tp ON (tp.COD_TIPO_PRODUTO = n.COD_TIPO_PRODUTO) INNER JOIN APP.PRODUTO p ON (n.COD_PRODUTO = p.COD_PRODUTO AND n.COD_TIPO_PRODUTO = p.COD_TIPO_PRODUTO AND p.COD_TIPO_PRODUTO = tp.COD_TIPO_PRODUTO) INNER JOIN APP.TEMPO d ON (n.DAT_DIA = d.DAT_DIA) WHERE d.NOM_MES = 'Abr/2013' GROUP BY v.NOM_VENDEDOR,r.NOM_REVENDA,tp.NOM_TIPO_PRODUTO,p.NOM_PRODUTO ORDER BY SUM(n.VLR_ITEM_NF) DESC FETCH FIRST 10 ROWS ONLY; 


