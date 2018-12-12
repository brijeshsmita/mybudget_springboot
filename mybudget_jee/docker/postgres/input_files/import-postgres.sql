--
-- PostgreSQL database dump
--

-- Dumped from database version 10.6 (Debian 10.6-1.pgdg90+1)
-- Dumped by pg_dump version 10.4

-- Started on 2018-11-28 16:05:45 -03

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 196 (class 1259 OID 16385)
-- Name: categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoria (
    id integer NOT NULL,
    nome character varying(120) NOT NULL,
    in_out character(1) NOT NULL,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.categoria OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 16388)
-- Name: conta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.conta (
    id integer NOT NULL,
    nome character varying(120) NOT NULL,
    saldo_inicial numeric(9,2),
    data_saldo_inicial timestamp without time zone,
    tipo character(1) NOT NULL,
    cartao_dia_fechamento integer,
    cartao_dia_pagamento integer,
    padrao boolean,
    conta_pagamento_fatura_id integer,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.conta OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16391)
-- Name: conta_saldo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.conta_saldo (
    id integer NOT NULL,
    valor numeric(9,2) DEFAULT 0 NOT NULL,
    ano integer NOT NULL,
    mes integer NOT NULL,
    conta_id integer,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.conta_saldo OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 16395)
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE public.databasechangelog OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16401)
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 16404)
-- Name: lancamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lancamento (
    id integer NOT NULL,
    status character(1) NOT NULL,
    tipo character(1) NOT NULL,
    data_lancamento timestamp without time zone NOT NULL,
    ano integer NOT NULL,
    mes integer NOT NULL,
    valor numeric(9,2) DEFAULT 0 NOT NULL,
    in_out character(1) NOT NULL,
    comentario character varying(120),
    saldo_inicial boolean DEFAULT false,
    fatura_cartao boolean DEFAULT false,
    conta_id integer,
    categoria_id integer,
    cartao_credito_fatura_id integer,
    qtd_parcelas integer,
    conta_origem_id integer,
    conta_destino_id integer,
    lancamento_serie_id integer,
    lancamento_cartao_id integer,
    indice_parcela integer,
    usuario_id bigint NOT NULL,
    ajuste boolean DEFAULT false NOT NULL
);


ALTER TABLE public.lancamento OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16411)
-- Name: lancamento_serie; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lancamento_serie (
    id integer NOT NULL,
    frequencia character(1) NOT NULL,
    data_inicio timestamp without time zone NOT NULL,
    data_limite timestamp without time zone NOT NULL,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.lancamento_serie OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 16414)
-- Name: log_acesso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.log_acesso (
    id bigint NOT NULL,
    usuario_id bigint NOT NULL,
    data timestamp without time zone NOT NULL
);


ALTER TABLE public.log_acesso OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 16417)
-- Name: sq_log_erros; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sq_log_erros
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_log_erros OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 16419)
-- Name: log_erros; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.log_erros (
    id bigint DEFAULT nextval('public.sq_log_erros'::regclass) NOT NULL,
    uuid character(36) NOT NULL,
    data timestamp without time zone NOT NULL,
    severidade character varying(100) NOT NULL,
    categoria character varying(100) NOT NULL,
    ip character varying(45) NOT NULL,
    usuario character varying(70),
    cabecalhos_http text NOT NULL,
    mensagem text NOT NULL,
    pilha_excecao text NOT NULL
);


ALTER TABLE public.log_erros OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 16426)
-- Name: orcamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orcamento (
    id integer NOT NULL,
    categoria_id integer NOT NULL,
    ano integer NOT NULL,
    mes integer NOT NULL,
    valor numeric(9,2) DEFAULT 0 NOT NULL,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.orcamento OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 16430)
-- Name: recuperacao_senha; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.recuperacao_senha (
    id bigint NOT NULL,
    usuario_id bigint NOT NULL,
    dt_solicitacao timestamp without time zone NOT NULL,
    cod_solicitacao character varying(32) NOT NULL,
    ativo boolean NOT NULL
);


ALTER TABLE public.recuperacao_senha OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 16433)
-- Name: sq_categoria; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sq_categoria
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_categoria OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 16435)
-- Name: sq_conta; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sq_conta
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_conta OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 16437)
-- Name: sq_conta_saldo; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sq_conta_saldo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_conta_saldo OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 16439)
-- Name: sq_lancamento; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sq_lancamento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_lancamento OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 16441)
-- Name: sq_lancamento_serie; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sq_lancamento_serie
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_lancamento_serie OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 16443)
-- Name: sq_log_acesso; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sq_log_acesso
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_log_acesso OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 16445)
-- Name: sq_orcamento; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sq_orcamento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_orcamento OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16447)
-- Name: sq_recuperacaosenha; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sq_recuperacaosenha
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_recuperacaosenha OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16449)
-- Name: sq_usuario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_usuario OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16451)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    id bigint NOT NULL,
    password character varying(70) NOT NULL,
    first_name character varying(20),
    email character varying(70) NOT NULL,
    ativo boolean DEFAULT true NOT NULL,
    data_cadastro timestamp without time zone DEFAULT now() NOT NULL,
    last_name character varying(20),
    data_ultimo_acesso timestamp without time zone,
    quantidade_acessos integer DEFAULT 0 NOT NULL,
    pre_cadastro boolean DEFAULT false
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16458)
-- Name: vw_orcado_real_categoria_mes; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vw_orcado_real_categoria_mes AS
 SELECT saldorealizado.catid AS categoria_id,
    orc.ano,
    orc.mes,
    saldorealizado.catinout AS in_out,
    saldorealizado.usuario_id,
    sum(orc.valor) AS orcado,
    sum(saldorealizado.realizado) AS realizado
   FROM public.orcamento orc,
    public.categoria cat,
    ( SELECT orc_1.ano,
            orc_1.mes,
            cat_1.id AS catid,
            cat_1.usuario_id,
            cat_1.in_out AS catinout,
            sum(lanc.valor) AS realizado
           FROM public.categoria cat_1,
            (public.orcamento orc_1
             LEFT JOIN public.lancamento lanc ON (((orc_1.categoria_id = lanc.categoria_id) AND (orc_1.ano = lanc.ano) AND (orc_1.mes = lanc.mes) AND (lanc.tipo <> '2'::bpchar) AND (lanc.ajuste = false))))
          WHERE ((orc_1.categoria_id = cat_1.id) AND (orc_1.valor > (0)::numeric))
          GROUP BY cat_1.usuario_id, cat_1.id, cat_1.in_out, orc_1.ano, orc_1.mes
          ORDER BY cat_1.in_out, orc_1.ano, orc_1.mes, (sum(orc_1.valor)) DESC) saldorealizado
  WHERE ((orc.ano = saldorealizado.ano) AND (orc.mes = saldorealizado.mes) AND (cat.id = orc.categoria_id) AND (saldorealizado.catinout = cat.in_out) AND (saldorealizado.catid = cat.id))
  GROUP BY saldorealizado.usuario_id, saldorealizado.catid, orc.ano, orc.mes, saldorealizado.catinout;


ALTER TABLE public.vw_orcado_real_categoria_mes OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16463)
-- Name: vw_orcado_real_mes; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vw_orcado_real_mes AS
 SELECT a.usuario_id,
    a.ano,
    a.mes,
    a.in_out,
    sum(a.saldo) AS saldo
   FROM ( SELECT vw_orcado_real_categoria_mes.usuario_id,
            vw_orcado_real_categoria_mes.in_out,
            vw_orcado_real_categoria_mes.ano,
            vw_orcado_real_categoria_mes.mes,
                CASE
                    WHEN ((vw_orcado_real_categoria_mes.realizado IS NOT NULL) AND (vw_orcado_real_categoria_mes.orcado > vw_orcado_real_categoria_mes.realizado)) THEN (vw_orcado_real_categoria_mes.orcado - vw_orcado_real_categoria_mes.realizado)
                    WHEN ((vw_orcado_real_categoria_mes.realizado IS NULL) AND (vw_orcado_real_categoria_mes.orcado > (0)::numeric)) THEN vw_orcado_real_categoria_mes.orcado
                    ELSE NULL::numeric
                END AS saldo
           FROM public.vw_orcado_real_categoria_mes
          WHERE ((vw_orcado_real_categoria_mes.orcado > vw_orcado_real_categoria_mes.realizado) OR (vw_orcado_real_categoria_mes.realizado IS NULL))) a
  GROUP BY a.usuario_id, a.ano, a.mes, a.in_out;


ALTER TABLE public.vw_orcado_real_mes OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16468)
-- Name: vw_saldo_orcado_acumulado_mes; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vw_saldo_orcado_acumulado_mes AS
 SELECT agrup.ano,
    agrup.mes,
    agrup.in_out,
    agrup.usuario_id,
    sum(agrup.saldoagrup) OVER (PARTITION BY agrup.usuario_id, agrup.in_out ORDER BY agrup.ano, agrup.mes ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS saldo
   FROM ( SELECT a.usuario_id,
            a.ano,
            a.mes,
            a.in_out,
            sum(a.saldo) AS saldoagrup
           FROM ( SELECT vw_orcado_real_categoria_mes.usuario_id,
                    vw_orcado_real_categoria_mes.in_out,
                    vw_orcado_real_categoria_mes.ano,
                    vw_orcado_real_categoria_mes.mes,
                        CASE
                            WHEN ((vw_orcado_real_categoria_mes.realizado IS NOT NULL) AND (vw_orcado_real_categoria_mes.orcado > vw_orcado_real_categoria_mes.realizado)) THEN (vw_orcado_real_categoria_mes.orcado - vw_orcado_real_categoria_mes.realizado)
                            WHEN ((vw_orcado_real_categoria_mes.realizado IS NULL) AND (vw_orcado_real_categoria_mes.orcado > (0)::numeric)) THEN vw_orcado_real_categoria_mes.orcado
                            ELSE NULL::numeric
                        END AS saldo
                   FROM public.vw_orcado_real_categoria_mes
                  WHERE ((vw_orcado_real_categoria_mes.orcado > vw_orcado_real_categoria_mes.realizado) OR (vw_orcado_real_categoria_mes.realizado IS NULL))) a
          GROUP BY a.usuario_id, a.ano, a.mes, a.in_out
          ORDER BY a.ano, a.mes) agrup;


ALTER TABLE public.vw_saldo_orcado_acumulado_mes OWNER TO postgres;

--
-- TOC entry 2978 (class 0 OID 16385)
-- Dependencies: 196
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.categoria VALUES (626, 'Alimentação', '1', 138);
INSERT INTO public.categoria VALUES (627, 'Doação', '1', 138);
INSERT INTO public.categoria VALUES (628, 'Educação', '1', 138);
INSERT INTO public.categoria VALUES (629, 'Empréstimos', '1', 138);
INSERT INTO public.categoria VALUES (630, 'Internet', '1', 138);
INSERT INTO public.categoria VALUES (631, 'Impostos', '1', 138);
INSERT INTO public.categoria VALUES (632, 'Lazer', '1', 138);
INSERT INTO public.categoria VALUES (633, 'Moradia', '1', 138);
INSERT INTO public.categoria VALUES (634, 'Outros', '1', 138);
INSERT INTO public.categoria VALUES (635, 'Presentes', '1', 138);
INSERT INTO public.categoria VALUES (636, 'Saúde', '1', 138);
INSERT INTO public.categoria VALUES (637, 'Tarifas Bancárias', '1', 138);
INSERT INTO public.categoria VALUES (638, 'Telefonia', '1', 138);
INSERT INTO public.categoria VALUES (639, 'Transporte', '1', 138);
INSERT INTO public.categoria VALUES (640, 'Salário', '0', 138);
INSERT INTO public.categoria VALUES (641, 'Freelancer', '0', 138);


--
-- TOC entry 2979 (class 0 OID 16388)
-- Dependencies: 197
-- Data for Name: conta; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.conta VALUES (138, 'Banco Exemplo', 0.00, '2018-11-28 00:00:00', '0', NULL, NULL, false, NULL, 138);
INSERT INTO public.conta VALUES (139, 'Carteira', 0.00, '2018-11-28 00:00:00', '2', NULL, NULL, false, NULL, 138);
INSERT INTO public.conta VALUES (140, 'Cartão de Crédito Exemplo', NULL, NULL, '1', 7, 15, false, 138, 138);


--
-- TOC entry 2980 (class 0 OID 16391)
-- Dependencies: 198
-- Data for Name: conta_saldo; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.conta_saldo VALUES (2334, 0.00, 2018, 11, 138, 138);
INSERT INTO public.conta_saldo VALUES (2335, 0.00, 2018, 11, 139, 138);


--
-- TOC entry 2981 (class 0 OID 16395)
-- Dependencies: 199
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.databasechangelog VALUES ('1528768837951', 'liquibase', 'liquibase-internal', '2018-06-12 02:00:37.947966', 1, 'EXECUTED', '7:d41d8cd98f00b204e9800998ecf8427e', 'empty', '', 'pre-1.0.20180614011233', '3.5.3', NULL, NULL, '8768837918');
INSERT INTO public.databasechangelog VALUES ('4', 'erikliberal', 'meussaldos/1.0/SAL-22.xml', '2018-06-27 02:16:00.363455', 49, 'EXECUTED', '7:93a766886c1a683791818e08d814a107', 'update tableName=usuario', '', NULL, '3.5.3', NULL, NULL, '0065760199');
INSERT INTO public.databasechangelog VALUES ('5', 'erikliberal', 'meussaldos/1.0/SAL-22.xml', '2018-06-27 02:16:00.422178', 50, 'EXECUTED', '7:7c5bdbde23c1a12a72f0341e5473f125', 'update tableName=usuario', '', NULL, '3.5.3', NULL, NULL, '0065760199');
INSERT INTO public.databasechangelog VALUES ('6', 'erikliberal', 'meussaldos/1.0/SAL-22.xml', '2018-06-27 02:16:00.565172', 51, 'EXECUTED', '7:3d87d43193e8a66a1ae993a7afff0274', 'addUniqueConstraint tableName=usuario', '', NULL, '3.5.3', NULL, NULL, '0065760199');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-26.xml', '2018-06-27 02:16:00.609244', 52, 'EXECUTED', '7:3e1f94acea33876bc6ef38adbd6ae6e5', 'createSequence sequenceName=sq_recuperacaosenha', '', NULL, '3.5.3', NULL, NULL, '0065760199');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-26.xml', '2018-06-27 02:16:00.668514', 53, 'EXECUTED', '7:d8afd81aca36c7aa4d2310b680c0862a', 'createTable tableName=recuperacao_senha', '', NULL, '3.5.3', NULL, NULL, '0065760199');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/SAL-225.xml', '2018-07-30 18:35:51.442325', 74, 'EXECUTED', '7:9d7b99b8b4e05a1accaa4e68ca489047', 'update tableName=lancamento', '', 'pre-1.0.20180801032447', '3.5.3', NULL, NULL, '2975751299');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/SAL-178.xml', '2018-08-01 22:42:03.753587', 77, 'EXECUTED', '7:8bc88febbbe171e547ef140133d7d903', 'addColumn tableName=usuario', '', NULL, '3.5.3', NULL, NULL, '3163323579');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/SAL-178.xml', '2018-08-01 22:42:03.806962', 78, 'EXECUTED', '7:cb4141f8a9c43191f36e12ca8ab04e12', 'createSequence sequenceName=sq_log_acesso', '', NULL, '3.5.3', NULL, NULL, '3163323579');
INSERT INTO public.databasechangelog VALUES ('3', 'erikliberal', 'meussaldos/1.0/SAL-26.xml', '2018-06-27 02:16:00.687254', 54, 'EXECUTED', '7:c4b6a67e8a764299ac00094dd6d0cf03', 'createIndex indexName=uk_cod_usuario, tableName=recuperacao_senha', '', 'pre-1.0.20180704095202', '3.5.3', NULL, NULL, '0065760199');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-63.xml', '2018-07-15 04:42:15.324312', 64, 'EXECUTED', '7:c1899b79670dd363753539ff33456dd6', 'createSequence sequenceName=sq_log_erros', '', NULL, '3.5.3', NULL, NULL, '1629735147');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/SAL-287.xml', '2018-09-25 13:37:40.34846', 84, 'EXECUTED', '7:59164a28ccb868dc5ab2e4481326c01f', 'addColumn tableName=usuario', '', '1.0.20181016132017', '3.5.3', NULL, NULL, '7882659755');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-63.xml', '2018-07-15 04:42:15.451891', 65, 'EXECUTED', '7:1814537e90398e816803e9b07159dc0c', 'createTable tableName=log_erros', '', 'pre-1.0.20180718193356', '3.5.3', NULL, NULL, '1629735147');
INSERT INTO public.databasechangelog VALUES ('3', 'victorfranca', 'meussaldos/1.0/SAL-178.xml', '2018-08-01 22:42:03.848382', 79, 'EXECUTED', '7:427f047f42dff2c8150be87d90727525', 'createTable tableName=log_acesso', '', 'pre-1.0.20180821122752', '3.5.3', NULL, NULL, '3163323579');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/SAL-155.xml', '2018-07-04 10:01:09.286269', 55, 'EXECUTED', '7:1f260e493157085997fefa12eda2cbe5', 'dropView viewName=vw_saldo_orcado_acumulado_mes', '', NULL, '3.5.3', NULL, NULL, '0698469162');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/SAL-131.xml', '2018-07-18 19:41:15.463503', 66, 'EXECUTED', '7:67142f1f7dd8e29b6846f5f480887b69', 'addColumn tableName=lancamento', '', NULL, '3.5.3', NULL, NULL, '1942875195');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/SAL-155.xml', '2018-07-04 10:01:09.371198', 56, 'EXECUTED', '7:56b7dc5b2ce4d962bb415508138fa3be', 'createView viewName=vw_saldo_orcado_acumulado_mes', '', 'pre-1.0.20180712155816', '3.5.3', NULL, NULL, '0698469162');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/SAL-131.xml', '2018-07-18 19:41:15.512181', 67, 'EXECUTED', '7:1f260e493157085997fefa12eda2cbe5', 'dropView viewName=vw_saldo_orcado_acumulado_mes', '', NULL, '3.5.3', NULL, NULL, '1942875195');
INSERT INTO public.databasechangelog VALUES ('3', 'victorfranca', 'meussaldos/1.0/SAL-131.xml', '2018-07-18 19:41:15.549459', 68, 'EXECUTED', '7:14dc42c56fc1441ee265c7f94dc21b2d', 'dropView viewName=vw_orcado_real_mes', '', NULL, '3.5.3', NULL, NULL, '1942875195');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/001-usuario.xml', '2018-06-14 01:15:29.310681', 2, 'EXECUTED', '7:aa143b44e30e3db5b7d21d55c3c71958', 'createSequence sequenceName=sq_usuario', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/001-usuario.xml', '2018-06-14 01:15:29.40643', 3, 'EXECUTED', '7:1b1bbca729b2b98ed0c5b41824638d5e', 'createTable tableName=usuario', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('3', 'victorfranca', 'meussaldos/1.0/001-usuario.xml', '2018-06-14 01:15:29.419738', 4, 'EXECUTED', '7:2c8e5878ec725bc244b9e00bc068b96a', 'addUniqueConstraint constraintName=unq_username, tableName=usuario', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/002-usuario-data.xml', '2018-06-14 01:15:29.46059', 5, 'EXECUTED', '7:c812e273b286bdb5b9c0a0c55424b84c', 'insert tableName=usuario', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/003-categoria.xml', '2018-06-14 01:15:29.498406', 6, 'EXECUTED', '7:8e461cc9597f4a64c880d1e9b816ea23', 'createSequence sequenceName=sq_categoria', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/003-categoria.xml', '2018-06-14 01:15:29.571487', 7, 'EXECUTED', '7:e71ea796af6c0e3a5670213bd618367b', 'createTable tableName=categoria', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/004-conta.xml', '2018-06-14 01:15:29.593552', 8, 'EXECUTED', '7:086bd55a2615d05079d513c78c949d6f', 'createSequence sequenceName=sq_conta', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/004-conta.xml', '2018-06-14 01:15:29.638057', 9, 'EXECUTED', '7:b097bf34638aa52ea34f641c36d898ef', 'createTable tableName=conta', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/005-conta_saldo.xml', '2018-06-14 01:15:29.655784', 10, 'EXECUTED', '7:184fddcba13f9acd416b267f356c479e', 'createSequence sequenceName=sq_conta_saldo', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/005-conta_saldo.xml', '2018-06-14 01:15:29.717786', 11, 'EXECUTED', '7:4b91ef7b5a51ab9c808ddc2d29e2cfa6', 'createTable tableName=conta_saldo', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/006-lancamento_serie.xml', '2018-06-14 01:15:29.724202', 12, 'EXECUTED', '7:7615847df945dab6b1107e14199ddf06', 'createSequence sequenceName=sq_lancamento_serie', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/006-lancamento_serie.xml', '2018-06-14 01:15:29.737811', 13, 'EXECUTED', '7:ac07fa1eb6550f39238bb3b7bef05770', 'createTable tableName=lancamento_serie', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/007-lancamento.xml', '2018-06-14 01:15:29.74342', 14, 'EXECUTED', '7:c70cce6cc4350add4af49202468b4945', 'createSequence sequenceName=sq_lancamento', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/007-lancamento.xml', '2018-06-14 01:15:29.795743', 15, 'EXECUTED', '7:6c2faf8538fcd219c36d25a2a818a360', 'createTable tableName=lancamento', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('3', 'victorfranca', 'meussaldos/1.0/007-lancamento.xml', '2018-06-14 01:15:29.806936', 16, 'EXECUTED', '7:8f67aba21c7ae81816b4c70c28f0160e', 'addColumn tableName=lancamento', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('4', 'victorfranca', 'meussaldos/1.0/007-lancamento.xml', '2018-06-14 01:15:29.812081', 17, 'EXECUTED', '7:490bfb77c5f864f350ac72fe7b733c4d', 'addColumn tableName=lancamento', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/008-orcamento.xml', '2018-06-14 01:15:29.816993', 18, 'EXECUTED', '7:d45881c11b043a07c5c4928559c20e87', 'createSequence sequenceName=sq_orcamento', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/008-orcamento.xml', '2018-06-14 01:15:29.830067', 19, 'EXECUTED', '7:39df45c0c293e846bdd6c6fc469e0140', 'createTable tableName=orcamento', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/009-orcado_real.xml', '2018-06-14 01:15:29.873055', 20, 'EXECUTED', '7:36c83ee62df12b14c6bb5fbbb27eee86', 'createView viewName=vw_orcado_real_mes', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/009-orcado_real.xml', '2018-06-14 01:15:29.909908', 21, 'EXECUTED', '7:081c198d8d0edee320c45ef2841637e5', 'createView viewName=vw_orcado_real_categoria_mes', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/010-saldo_orcado_acumulado.xml', '2018-06-14 01:15:29.96549', 22, 'EXECUTED', '7:8bfa6fa473a6026e0ee51144c289dee6', 'createView viewName=vw_saldo_orcado_acumulado_mes', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/SAL-110.xml', '2018-06-14 01:15:30.054233', 23, 'EXECUTED', '7:ce30673184f01429171b098cef73b669', 'addColumn tableName=lancamento; addColumn tableName=lancamento; addColumn tableName=lancamento', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-110.xml', '2018-06-14 01:15:30.0919', 24, 'EXECUTED', '7:1f260e493157085997fefa12eda2cbe5', 'dropView viewName=vw_saldo_orcado_acumulado_mes', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/SAL-110.xml', '2018-06-14 01:15:30.1475', 25, 'EXECUTED', '7:42b1db9e70052f45600d7ef9cdb865d7', 'createView viewName=vw_orcado_real_mes', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('3', 'victorfranca', 'meussaldos/1.0/SAL-110.xml', '2018-06-14 01:15:30.247815', 26, 'EXECUTED', '7:4454acde1b357d58ab278b2c67f57f83', 'createView viewName=vw_orcado_real_categoria_mes', '', NULL, '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-110.xml', '2018-06-14 01:15:30.280324', 27, 'EXECUTED', '7:2f35d9cffb7d75e691e86cf70da89b27', 'createView viewName=vw_saldo_orcado_acumulado_mes', '', 'pre-1.0.20180616222650', '3.5.3', NULL, NULL, '8938929208');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-105/001-categoria.xml', '2018-06-16 22:31:07.533976', 28, 'EXECUTED', '7:460fb60d0986b5b36aab2abb3040e145', 'addColumn tableName=categoria', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-105/001-categoria.xml', '2018-06-16 22:31:07.61447', 29, 'EXECUTED', '7:71c04cff57d17079ca659915cbced89c', 'addForeignKeyConstraint baseTableName=categoria, constraintName=fk_categoria_001, referencedTableName=usuario', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-105/002-orcamento.xml', '2018-06-16 22:31:07.664795', 30, 'EXECUTED', '7:908f5ab002b75cce1b0bc32439886176', 'addColumn tableName=orcamento', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-105/002-orcamento.xml', '2018-06-16 22:31:07.713474', 31, 'EXECUTED', '7:436b500bbabb9271db323e0589e6ae62', 'addForeignKeyConstraint baseTableName=orcamento, constraintName=fk_orcamento_001, referencedTableName=usuario', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-105/003-conta.xml', '2018-06-16 22:31:07.766482', 32, 'EXECUTED', '7:f35141897a7f076150f62f012a14483b', 'addColumn tableName=conta', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-105/003-conta.xml', '2018-06-16 22:31:07.815832', 33, 'EXECUTED', '7:fbac736ad42ad01fca42b23fb3f53ee2', 'addForeignKeyConstraint baseTableName=conta, constraintName=fk_conta_001, referencedTableName=usuario', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-105/004-conta_saldo.xml', '2018-06-16 22:31:07.863216', 34, 'EXECUTED', '7:93804287c04987a8ec355b7b8b65e503', 'addColumn tableName=conta_saldo', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-105/004-conta_saldo.xml', '2018-06-16 22:31:07.904933', 35, 'EXECUTED', '7:7f97a283dedfa7420ac4a22d970dc265', 'addForeignKeyConstraint baseTableName=conta_saldo, constraintName=fk_conta_saldo_001, referencedTableName=usuario', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-105/005-lancamento_serie.xml', '2018-06-16 22:31:07.929195', 36, 'EXECUTED', '7:4516d5e19cd2d84f9a79885fad801e2e', 'addColumn tableName=lancamento_serie', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-105/005-lancamento_serie.xml', '2018-06-16 22:31:07.975368', 37, 'EXECUTED', '7:6f8309dd70a700b6e4c0b78607920fb5', 'addForeignKeyConstraint baseTableName=lancamento_serie, constraintName=fk_lancamento_serie_001, referencedTableName=usuario', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-105/006-lancamento.xml', '2018-06-16 22:31:08.001816', 38, 'EXECUTED', '7:c605c056d784e01bfdf69d395a2e9bd0', 'addColumn tableName=lancamento', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-105/006-lancamento.xml', '2018-06-16 22:31:08.012786', 39, 'EXECUTED', '7:6bb13360a7f0c16aec298201a6fb93e6', 'addForeignKeyConstraint baseTableName=lancamento, constraintName=fk_lancamento_001, referencedTableName=usuario', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-105/007-migracaoViews.xml', '2018-06-16 22:31:08.020232', 40, 'EXECUTED', '7:1f260e493157085997fefa12eda2cbe5', 'dropView viewName=vw_saldo_orcado_acumulado_mes', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-105/007-migracaoViews.xml', '2018-06-16 22:31:08.037303', 41, 'EXECUTED', '7:14dc42c56fc1441ee265c7f94dc21b2d', 'dropView viewName=vw_orcado_real_mes', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('3', 'erikliberal', 'meussaldos/1.0/SAL-105/007-migracaoViews.xml', '2018-06-16 22:31:08.085306', 42, 'EXECUTED', '7:f4523bcc20b39b84c5a4f8d8b794aee7', 'dropView viewName=vw_orcado_real_categoria_mes', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('4', 'erikliberal', 'meussaldos/1.0/SAL-105/007-migracaoViews.xml', '2018-06-16 22:31:08.156', 43, 'EXECUTED', '7:23779c43c0dba8001b9c4d41e09c64d5', 'createView viewName=vw_orcado_real_mes', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('5', 'erikliberal', 'meussaldos/1.0/SAL-105/007-migracaoViews.xml', '2018-06-16 22:31:08.187069', 44, 'EXECUTED', '7:74c7cb6d2625c15f9df281d4842a42b0', 'createView viewName=vw_saldo_orcado_acumulado_mes', '', NULL, '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('3', 'erikliberal', 'meussaldos/1.0/SAL-22.xml', '2018-06-20 00:07:11.807475', 48, 'EXECUTED', '7:cfffaaa70bc2e59d2302c50c7b81a613', 'addNotNullConstraint columnName=email, tableName=usuario', '', 'pre-1.0.20180627020813', '3.5.3', NULL, NULL, '9453231490');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/SAL-127.xml', '2018-07-12 16:08:15.924244', 57, 'EXECUTED', '7:1f260e493157085997fefa12eda2cbe5', 'dropView viewName=vw_saldo_orcado_acumulado_mes', '', NULL, '3.5.3', NULL, NULL, '1411695791');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/SAL-127.xml', '2018-07-12 16:08:15.987951', 58, 'EXECUTED', '7:14dc42c56fc1441ee265c7f94dc21b2d', 'dropView viewName=vw_orcado_real_mes', '', NULL, '3.5.3', NULL, NULL, '1411695791');
INSERT INTO public.databasechangelog VALUES ('3', 'victorfranca', 'meussaldos/1.0/SAL-127.xml', '2018-07-12 16:08:16.0368', 59, 'EXECUTED', '7:f4523bcc20b39b84c5a4f8d8b794aee7', 'dropView viewName=vw_orcado_real_categoria_mes', '', NULL, '3.5.3', NULL, NULL, '1411695791');
INSERT INTO public.databasechangelog VALUES ('4', 'victorfranca', 'meussaldos/1.0/SAL-127.xml', '2018-07-12 16:08:16.17683', 60, 'EXECUTED', '7:9fd306895aae012d1928b9f3bff712d2', 'createView viewName=vw_orcado_real_mes', '', NULL, '3.5.3', NULL, NULL, '1411695791');
INSERT INTO public.databasechangelog VALUES ('5', 'victorfranca', 'meussaldos/1.0/SAL-127.xml', '2018-07-12 16:08:16.219787', 61, 'EXECUTED', '7:4e3420310c2828d7346b791a6de172e4', 'createView viewName=vw_orcado_real_categoria_mes', '', NULL, '3.5.3', NULL, NULL, '1411695791');
INSERT INTO public.databasechangelog VALUES ('6', 'victorfranca', 'meussaldos/1.0/SAL-127.xml', '2018-07-12 16:08:16.289226', 62, 'EXECUTED', '7:311662c6ada9d483c02429ad232c2038', 'createView viewName=vw_saldo_orcado_acumulado_mes', '', NULL, '3.5.3', NULL, NULL, '1411695791');
INSERT INTO public.databasechangelog VALUES ('6', 'erikliberal', 'meussaldos/1.0/SAL-105/007-migracaoViews.xml', '2018-06-16 22:31:08.20982', 45, 'EXECUTED', '7:882a6f816760614c8d04d570b8cc1e40', 'createView viewName=vw_orcado_real_categoria_mes', '', 'pre-1.0.20180619235641', '3.5.3', NULL, NULL, '9188267365');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-22.xml', '2018-06-20 00:07:11.706788', 46, 'EXECUTED', '7:51691aa4c8b4202b53a6e9a8690c57ee', 'addColumn tableName=usuario', '', NULL, '3.5.3', NULL, NULL, '9453231490');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-22.xml', '2018-06-20 00:07:11.761028', 47, 'EXECUTED', '7:07c7baaaf145725936847c097846943a', 'update tableName=usuario', '', NULL, '3.5.3', NULL, NULL, '9453231490');
INSERT INTO public.databasechangelog VALUES ('1', 'victorfranca', 'meussaldos/1.0/SAL-273.xml', '2018-08-21 13:27:30.043627', 80, 'EXECUTED', '7:1f260e493157085997fefa12eda2cbe5', 'dropView viewName=vw_saldo_orcado_acumulado_mes', '', NULL, '3.5.3', NULL, NULL, '4858049850');
INSERT INTO public.databasechangelog VALUES ('2', 'victorfranca', 'meussaldos/1.0/SAL-273.xml', '2018-08-21 13:27:30.146073', 81, 'EXECUTED', '7:d163b0f4d000c8063c65382c718f7162', 'createView viewName=vw_saldo_orcado_acumulado_mes', '', NULL, '3.5.3', NULL, NULL, '4858049850');
INSERT INTO public.databasechangelog VALUES ('8', 'victorfranca', 'meussaldos/1.0/SAL-131.xml', '2018-07-18 19:41:15.727072', 73, 'EXECUTED', '7:87c6178548eb0694b27359c392d98ed6', 'dropColumn columnName=valor_total_compra_cartao, tableName=lancamento', '', 'pre-1.0.20180730180144', '3.5.3', NULL, NULL, '1942875195');
INSERT INTO public.databasechangelog VALUES ('1', 'erikliberal', 'meussaldos/1.0/SAL-227.xml', '2018-08-01 09:10:42.519508', 75, 'EXECUTED', '7:8cf6183ceec0d8658360c402c31ef3bd', 'dropColumn tableName=usuario', '', NULL, '3.5.3', NULL, NULL, '3114642289');
INSERT INTO public.databasechangelog VALUES ('3', 'victorfranca', 'meussaldos/1.0/SAL-273.xml', '2018-08-21 13:27:30.194903', 82, 'EXECUTED', '7:14dc42c56fc1441ee265c7f94dc21b2d', 'dropView viewName=vw_orcado_real_mes', '', NULL, '3.5.3', NULL, NULL, '4858049850');
INSERT INTO public.databasechangelog VALUES ('7', 'victorfranca', 'meussaldos/1.0/SAL-127.xml', '2018-07-12 16:08:16.329565', 63, 'EXECUTED', '7:8bc3f3c6e8d17440a4ab8ba0fd0434a7', 'addColumn tableName=usuario', '', 'pre-1.0.20180715043706', '3.5.3', NULL, NULL, '1411695791');
INSERT INTO public.databasechangelog VALUES ('4', 'victorfranca', 'meussaldos/1.0/SAL-131.xml', '2018-07-18 19:41:15.587117', 69, 'EXECUTED', '7:f4523bcc20b39b84c5a4f8d8b794aee7', 'dropView viewName=vw_orcado_real_categoria_mes', '', NULL, '3.5.3', NULL, NULL, '1942875195');
INSERT INTO public.databasechangelog VALUES ('5', 'victorfranca', 'meussaldos/1.0/SAL-131.xml', '2018-07-18 19:41:15.651819', 70, 'EXECUTED', '7:21a1e70205a8c43dca27deeb03587809', 'createView viewName=vw_orcado_real_mes', '', NULL, '3.5.3', NULL, NULL, '1942875195');
INSERT INTO public.databasechangelog VALUES ('6', 'victorfranca', 'meussaldos/1.0/SAL-131.xml', '2018-07-18 19:41:15.691724', 71, 'EXECUTED', '7:129165f85a4ff03d096bf835ca83367a', 'createView viewName=vw_orcado_real_categoria_mes', '', NULL, '3.5.3', NULL, NULL, '1942875195');
INSERT INTO public.databasechangelog VALUES ('7', 'victorfranca', 'meussaldos/1.0/SAL-131.xml', '2018-07-18 19:41:15.722144', 72, 'EXECUTED', '7:311662c6ada9d483c02429ad232c2038', 'createView viewName=vw_saldo_orcado_acumulado_mes', '', NULL, '3.5.3', NULL, NULL, '1942875195');
INSERT INTO public.databasechangelog VALUES ('2', 'erikliberal', 'meussaldos/1.0/SAL-227.xml', '2018-08-01 09:10:42.583115', 76, 'EXECUTED', '7:3207fad9df4f5cd12822594d313bfe5c', 'addColumn tableName=usuario', '', 'pre-1.0.20180801223149', '3.5.3', NULL, NULL, '3114642289');
INSERT INTO public.databasechangelog VALUES ('4', 'victorfranca', 'meussaldos/1.0/SAL-273.xml', '2018-08-21 13:27:30.229959', 83, 'EXECUTED', '7:eb8c1bfaaffb974f6de05e838e4c82bb', 'createView viewName=vw_orcado_real_mes', '', 'pre-1.0.20180925133227', '3.5.3', NULL, NULL, '4858049850');


--
-- TOC entry 2982 (class 0 OID 16401)
-- Dependencies: 200
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.databasechangeloglock VALUES (1, false, NULL, NULL);


--
-- TOC entry 2983 (class 0 OID 16404)
-- Dependencies: 201
-- Data for Name: lancamento; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.lancamento VALUES (5120, '2', '0', '2018-11-28 00:00:00', 2018, 11, 0.00, '0', NULL, true, false, 138, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 138, false);
INSERT INTO public.lancamento VALUES (5121, '2', '0', '2018-11-28 00:00:00', 2018, 11, 0.00, '0', NULL, true, false, 139, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 138, false);


--
-- TOC entry 2984 (class 0 OID 16411)
-- Dependencies: 202
-- Data for Name: lancamento_serie; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2985 (class 0 OID 16414)
-- Dependencies: 203
-- Data for Name: log_acesso; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2987 (class 0 OID 16419)
-- Dependencies: 205
-- Data for Name: log_erros; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2988 (class 0 OID 16426)
-- Dependencies: 206
-- Data for Name: orcamento; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.orcamento VALUES (12545, 626, 2018, 11, 400.00, 138);
INSERT INTO public.orcamento VALUES (12546, 626, 2018, 12, 400.00, 138);
INSERT INTO public.orcamento VALUES (12547, 626, 2019, 1, 400.00, 138);
INSERT INTO public.orcamento VALUES (12548, 626, 2019, 2, 400.00, 138);
INSERT INTO public.orcamento VALUES (12549, 626, 2019, 3, 400.00, 138);
INSERT INTO public.orcamento VALUES (12550, 626, 2019, 4, 400.00, 138);
INSERT INTO public.orcamento VALUES (12551, 626, 2019, 5, 400.00, 138);
INSERT INTO public.orcamento VALUES (12552, 626, 2019, 6, 400.00, 138);
INSERT INTO public.orcamento VALUES (12553, 626, 2019, 7, 400.00, 138);
INSERT INTO public.orcamento VALUES (12554, 626, 2019, 8, 400.00, 138);
INSERT INTO public.orcamento VALUES (12555, 626, 2019, 9, 400.00, 138);
INSERT INTO public.orcamento VALUES (12556, 626, 2019, 10, 400.00, 138);
INSERT INTO public.orcamento VALUES (12557, 626, 2019, 11, 400.00, 138);
INSERT INTO public.orcamento VALUES (12558, 626, 2019, 12, 400.00, 138);
INSERT INTO public.orcamento VALUES (12559, 626, 2020, 1, 400.00, 138);
INSERT INTO public.orcamento VALUES (12560, 626, 2020, 2, 400.00, 138);
INSERT INTO public.orcamento VALUES (12561, 626, 2020, 3, 400.00, 138);
INSERT INTO public.orcamento VALUES (12562, 626, 2020, 4, 400.00, 138);
INSERT INTO public.orcamento VALUES (12563, 626, 2020, 5, 400.00, 138);
INSERT INTO public.orcamento VALUES (12564, 626, 2020, 6, 400.00, 138);
INSERT INTO public.orcamento VALUES (12565, 626, 2020, 7, 400.00, 138);
INSERT INTO public.orcamento VALUES (12566, 626, 2020, 8, 400.00, 138);
INSERT INTO public.orcamento VALUES (12567, 626, 2020, 9, 400.00, 138);
INSERT INTO public.orcamento VALUES (12568, 626, 2020, 10, 400.00, 138);
INSERT INTO public.orcamento VALUES (12569, 626, 2020, 11, 400.00, 138);
INSERT INTO public.orcamento VALUES (12570, 626, 2020, 12, 400.00, 138);
INSERT INTO public.orcamento VALUES (12571, 626, 2021, 1, 400.00, 138);
INSERT INTO public.orcamento VALUES (12572, 626, 2021, 2, 400.00, 138);
INSERT INTO public.orcamento VALUES (12573, 626, 2021, 3, 400.00, 138);
INSERT INTO public.orcamento VALUES (12574, 626, 2021, 4, 400.00, 138);
INSERT INTO public.orcamento VALUES (12575, 626, 2021, 5, 400.00, 138);
INSERT INTO public.orcamento VALUES (12576, 626, 2021, 6, 400.00, 138);
INSERT INTO public.orcamento VALUES (12577, 626, 2021, 7, 400.00, 138);
INSERT INTO public.orcamento VALUES (12578, 626, 2021, 8, 400.00, 138);
INSERT INTO public.orcamento VALUES (12579, 626, 2021, 9, 400.00, 138);
INSERT INTO public.orcamento VALUES (12580, 626, 2021, 10, 400.00, 138);
INSERT INTO public.orcamento VALUES (12581, 628, 2018, 11, 500.00, 138);
INSERT INTO public.orcamento VALUES (12582, 628, 2018, 12, 500.00, 138);
INSERT INTO public.orcamento VALUES (12583, 628, 2019, 1, 500.00, 138);
INSERT INTO public.orcamento VALUES (12584, 628, 2019, 2, 500.00, 138);
INSERT INTO public.orcamento VALUES (12585, 628, 2019, 3, 500.00, 138);
INSERT INTO public.orcamento VALUES (12586, 628, 2019, 4, 500.00, 138);
INSERT INTO public.orcamento VALUES (12587, 628, 2019, 5, 500.00, 138);
INSERT INTO public.orcamento VALUES (12588, 628, 2019, 6, 500.00, 138);
INSERT INTO public.orcamento VALUES (12589, 628, 2019, 7, 500.00, 138);
INSERT INTO public.orcamento VALUES (12590, 628, 2019, 8, 500.00, 138);
INSERT INTO public.orcamento VALUES (12591, 628, 2019, 9, 500.00, 138);
INSERT INTO public.orcamento VALUES (12592, 628, 2019, 10, 500.00, 138);
INSERT INTO public.orcamento VALUES (12593, 628, 2019, 11, 500.00, 138);
INSERT INTO public.orcamento VALUES (12594, 628, 2019, 12, 500.00, 138);
INSERT INTO public.orcamento VALUES (12595, 628, 2020, 1, 500.00, 138);
INSERT INTO public.orcamento VALUES (12596, 628, 2020, 2, 500.00, 138);
INSERT INTO public.orcamento VALUES (12597, 628, 2020, 3, 500.00, 138);
INSERT INTO public.orcamento VALUES (12598, 628, 2020, 4, 500.00, 138);
INSERT INTO public.orcamento VALUES (12599, 628, 2020, 5, 500.00, 138);
INSERT INTO public.orcamento VALUES (12600, 628, 2020, 6, 500.00, 138);
INSERT INTO public.orcamento VALUES (12601, 628, 2020, 7, 500.00, 138);
INSERT INTO public.orcamento VALUES (12602, 628, 2020, 8, 500.00, 138);
INSERT INTO public.orcamento VALUES (12603, 628, 2020, 9, 500.00, 138);
INSERT INTO public.orcamento VALUES (12604, 628, 2020, 10, 500.00, 138);
INSERT INTO public.orcamento VALUES (12605, 628, 2020, 11, 500.00, 138);
INSERT INTO public.orcamento VALUES (12606, 628, 2020, 12, 500.00, 138);
INSERT INTO public.orcamento VALUES (12607, 628, 2021, 1, 500.00, 138);
INSERT INTO public.orcamento VALUES (12608, 628, 2021, 2, 500.00, 138);
INSERT INTO public.orcamento VALUES (12609, 628, 2021, 3, 500.00, 138);
INSERT INTO public.orcamento VALUES (12610, 628, 2021, 4, 500.00, 138);
INSERT INTO public.orcamento VALUES (12611, 628, 2021, 5, 500.00, 138);
INSERT INTO public.orcamento VALUES (12612, 628, 2021, 6, 500.00, 138);
INSERT INTO public.orcamento VALUES (12613, 628, 2021, 7, 500.00, 138);
INSERT INTO public.orcamento VALUES (12614, 628, 2021, 8, 500.00, 138);
INSERT INTO public.orcamento VALUES (12615, 628, 2021, 9, 500.00, 138);
INSERT INTO public.orcamento VALUES (12616, 628, 2021, 10, 500.00, 138);
INSERT INTO public.orcamento VALUES (12617, 629, 2018, 11, 50.00, 138);
INSERT INTO public.orcamento VALUES (12618, 629, 2018, 12, 50.00, 138);
INSERT INTO public.orcamento VALUES (12619, 629, 2019, 1, 50.00, 138);
INSERT INTO public.orcamento VALUES (12620, 629, 2019, 2, 50.00, 138);
INSERT INTO public.orcamento VALUES (12621, 629, 2019, 3, 50.00, 138);
INSERT INTO public.orcamento VALUES (12622, 629, 2019, 4, 50.00, 138);
INSERT INTO public.orcamento VALUES (12623, 629, 2019, 5, 50.00, 138);
INSERT INTO public.orcamento VALUES (12624, 629, 2019, 6, 50.00, 138);
INSERT INTO public.orcamento VALUES (12625, 629, 2019, 7, 50.00, 138);
INSERT INTO public.orcamento VALUES (12626, 629, 2019, 8, 50.00, 138);
INSERT INTO public.orcamento VALUES (12627, 629, 2019, 9, 50.00, 138);
INSERT INTO public.orcamento VALUES (12628, 629, 2019, 10, 50.00, 138);
INSERT INTO public.orcamento VALUES (12629, 629, 2019, 11, 50.00, 138);
INSERT INTO public.orcamento VALUES (12630, 629, 2019, 12, 50.00, 138);
INSERT INTO public.orcamento VALUES (12631, 629, 2020, 1, 50.00, 138);
INSERT INTO public.orcamento VALUES (12632, 629, 2020, 2, 50.00, 138);
INSERT INTO public.orcamento VALUES (12633, 629, 2020, 3, 50.00, 138);
INSERT INTO public.orcamento VALUES (12634, 629, 2020, 4, 50.00, 138);
INSERT INTO public.orcamento VALUES (12635, 629, 2020, 5, 50.00, 138);
INSERT INTO public.orcamento VALUES (12636, 629, 2020, 6, 50.00, 138);
INSERT INTO public.orcamento VALUES (12637, 629, 2020, 7, 50.00, 138);
INSERT INTO public.orcamento VALUES (12638, 629, 2020, 8, 50.00, 138);
INSERT INTO public.orcamento VALUES (12639, 629, 2020, 9, 50.00, 138);
INSERT INTO public.orcamento VALUES (12640, 629, 2020, 10, 50.00, 138);
INSERT INTO public.orcamento VALUES (12641, 629, 2020, 11, 50.00, 138);
INSERT INTO public.orcamento VALUES (12642, 629, 2020, 12, 50.00, 138);
INSERT INTO public.orcamento VALUES (12643, 629, 2021, 1, 50.00, 138);
INSERT INTO public.orcamento VALUES (12644, 629, 2021, 2, 50.00, 138);
INSERT INTO public.orcamento VALUES (12645, 629, 2021, 3, 50.00, 138);
INSERT INTO public.orcamento VALUES (12646, 629, 2021, 4, 50.00, 138);
INSERT INTO public.orcamento VALUES (12647, 629, 2021, 5, 50.00, 138);
INSERT INTO public.orcamento VALUES (12648, 629, 2021, 6, 50.00, 138);
INSERT INTO public.orcamento VALUES (12649, 629, 2021, 7, 50.00, 138);
INSERT INTO public.orcamento VALUES (12650, 629, 2021, 8, 50.00, 138);
INSERT INTO public.orcamento VALUES (12651, 629, 2021, 9, 50.00, 138);
INSERT INTO public.orcamento VALUES (12652, 629, 2021, 10, 50.00, 138);
INSERT INTO public.orcamento VALUES (12653, 630, 2018, 11, 60.00, 138);
INSERT INTO public.orcamento VALUES (12654, 630, 2018, 12, 60.00, 138);
INSERT INTO public.orcamento VALUES (12655, 630, 2019, 1, 60.00, 138);
INSERT INTO public.orcamento VALUES (12656, 630, 2019, 2, 60.00, 138);
INSERT INTO public.orcamento VALUES (12657, 630, 2019, 3, 60.00, 138);
INSERT INTO public.orcamento VALUES (12658, 630, 2019, 4, 60.00, 138);
INSERT INTO public.orcamento VALUES (12659, 630, 2019, 5, 60.00, 138);
INSERT INTO public.orcamento VALUES (12660, 630, 2019, 6, 60.00, 138);
INSERT INTO public.orcamento VALUES (12661, 630, 2019, 7, 60.00, 138);
INSERT INTO public.orcamento VALUES (12662, 630, 2019, 8, 60.00, 138);
INSERT INTO public.orcamento VALUES (12663, 630, 2019, 9, 60.00, 138);
INSERT INTO public.orcamento VALUES (12664, 630, 2019, 10, 60.00, 138);
INSERT INTO public.orcamento VALUES (12665, 630, 2019, 11, 60.00, 138);
INSERT INTO public.orcamento VALUES (12666, 630, 2019, 12, 60.00, 138);
INSERT INTO public.orcamento VALUES (12667, 630, 2020, 1, 60.00, 138);
INSERT INTO public.orcamento VALUES (12668, 630, 2020, 2, 60.00, 138);
INSERT INTO public.orcamento VALUES (12669, 630, 2020, 3, 60.00, 138);
INSERT INTO public.orcamento VALUES (12670, 630, 2020, 4, 60.00, 138);
INSERT INTO public.orcamento VALUES (12671, 630, 2020, 5, 60.00, 138);
INSERT INTO public.orcamento VALUES (12672, 630, 2020, 6, 60.00, 138);
INSERT INTO public.orcamento VALUES (12673, 630, 2020, 7, 60.00, 138);
INSERT INTO public.orcamento VALUES (12674, 630, 2020, 8, 60.00, 138);
INSERT INTO public.orcamento VALUES (12675, 630, 2020, 9, 60.00, 138);
INSERT INTO public.orcamento VALUES (12676, 630, 2020, 10, 60.00, 138);
INSERT INTO public.orcamento VALUES (12677, 630, 2020, 11, 60.00, 138);
INSERT INTO public.orcamento VALUES (12678, 630, 2020, 12, 60.00, 138);
INSERT INTO public.orcamento VALUES (12679, 630, 2021, 1, 60.00, 138);
INSERT INTO public.orcamento VALUES (12680, 630, 2021, 2, 60.00, 138);
INSERT INTO public.orcamento VALUES (12681, 630, 2021, 3, 60.00, 138);
INSERT INTO public.orcamento VALUES (12682, 630, 2021, 4, 60.00, 138);
INSERT INTO public.orcamento VALUES (12683, 630, 2021, 5, 60.00, 138);
INSERT INTO public.orcamento VALUES (12684, 630, 2021, 6, 60.00, 138);
INSERT INTO public.orcamento VALUES (12685, 630, 2021, 7, 60.00, 138);
INSERT INTO public.orcamento VALUES (12686, 630, 2021, 8, 60.00, 138);
INSERT INTO public.orcamento VALUES (12687, 630, 2021, 9, 60.00, 138);
INSERT INTO public.orcamento VALUES (12688, 630, 2021, 10, 60.00, 138);
INSERT INTO public.orcamento VALUES (12689, 631, 2018, 11, 45.00, 138);
INSERT INTO public.orcamento VALUES (12690, 631, 2018, 12, 45.00, 138);
INSERT INTO public.orcamento VALUES (12691, 631, 2019, 1, 45.00, 138);
INSERT INTO public.orcamento VALUES (12692, 631, 2019, 2, 45.00, 138);
INSERT INTO public.orcamento VALUES (12693, 631, 2019, 3, 45.00, 138);
INSERT INTO public.orcamento VALUES (12694, 631, 2019, 4, 45.00, 138);
INSERT INTO public.orcamento VALUES (12695, 631, 2019, 5, 45.00, 138);
INSERT INTO public.orcamento VALUES (12696, 631, 2019, 6, 45.00, 138);
INSERT INTO public.orcamento VALUES (12697, 631, 2019, 7, 45.00, 138);
INSERT INTO public.orcamento VALUES (12698, 631, 2019, 8, 45.00, 138);
INSERT INTO public.orcamento VALUES (12699, 631, 2019, 9, 45.00, 138);
INSERT INTO public.orcamento VALUES (12700, 631, 2019, 10, 45.00, 138);
INSERT INTO public.orcamento VALUES (12701, 631, 2019, 11, 45.00, 138);
INSERT INTO public.orcamento VALUES (12702, 631, 2019, 12, 45.00, 138);
INSERT INTO public.orcamento VALUES (12703, 631, 2020, 1, 45.00, 138);
INSERT INTO public.orcamento VALUES (12704, 631, 2020, 2, 45.00, 138);
INSERT INTO public.orcamento VALUES (12705, 631, 2020, 3, 45.00, 138);
INSERT INTO public.orcamento VALUES (12706, 631, 2020, 4, 45.00, 138);
INSERT INTO public.orcamento VALUES (12707, 631, 2020, 5, 45.00, 138);
INSERT INTO public.orcamento VALUES (12708, 631, 2020, 6, 45.00, 138);
INSERT INTO public.orcamento VALUES (12709, 631, 2020, 7, 45.00, 138);
INSERT INTO public.orcamento VALUES (12710, 631, 2020, 8, 45.00, 138);
INSERT INTO public.orcamento VALUES (12711, 631, 2020, 9, 45.00, 138);
INSERT INTO public.orcamento VALUES (12712, 631, 2020, 10, 45.00, 138);
INSERT INTO public.orcamento VALUES (12713, 631, 2020, 11, 45.00, 138);
INSERT INTO public.orcamento VALUES (12714, 631, 2020, 12, 45.00, 138);
INSERT INTO public.orcamento VALUES (12715, 631, 2021, 1, 45.00, 138);
INSERT INTO public.orcamento VALUES (12716, 631, 2021, 2, 45.00, 138);
INSERT INTO public.orcamento VALUES (12717, 631, 2021, 3, 45.00, 138);
INSERT INTO public.orcamento VALUES (12718, 631, 2021, 4, 45.00, 138);
INSERT INTO public.orcamento VALUES (12719, 631, 2021, 5, 45.00, 138);
INSERT INTO public.orcamento VALUES (12720, 631, 2021, 6, 45.00, 138);
INSERT INTO public.orcamento VALUES (12721, 631, 2021, 7, 45.00, 138);
INSERT INTO public.orcamento VALUES (12722, 631, 2021, 8, 45.00, 138);
INSERT INTO public.orcamento VALUES (12723, 631, 2021, 9, 45.00, 138);
INSERT INTO public.orcamento VALUES (12724, 631, 2021, 10, 45.00, 138);
INSERT INTO public.orcamento VALUES (12725, 632, 2018, 11, 450.00, 138);
INSERT INTO public.orcamento VALUES (12726, 632, 2018, 12, 450.00, 138);
INSERT INTO public.orcamento VALUES (12727, 632, 2019, 1, 450.00, 138);
INSERT INTO public.orcamento VALUES (12728, 632, 2019, 2, 450.00, 138);
INSERT INTO public.orcamento VALUES (12729, 632, 2019, 3, 450.00, 138);
INSERT INTO public.orcamento VALUES (12730, 632, 2019, 4, 450.00, 138);
INSERT INTO public.orcamento VALUES (12731, 632, 2019, 5, 450.00, 138);
INSERT INTO public.orcamento VALUES (12732, 632, 2019, 6, 450.00, 138);
INSERT INTO public.orcamento VALUES (12733, 632, 2019, 7, 450.00, 138);
INSERT INTO public.orcamento VALUES (12734, 632, 2019, 8, 450.00, 138);
INSERT INTO public.orcamento VALUES (12735, 632, 2019, 9, 450.00, 138);
INSERT INTO public.orcamento VALUES (12736, 632, 2019, 10, 450.00, 138);
INSERT INTO public.orcamento VALUES (12737, 632, 2019, 11, 450.00, 138);
INSERT INTO public.orcamento VALUES (12738, 632, 2019, 12, 450.00, 138);
INSERT INTO public.orcamento VALUES (12739, 632, 2020, 1, 450.00, 138);
INSERT INTO public.orcamento VALUES (12740, 632, 2020, 2, 450.00, 138);
INSERT INTO public.orcamento VALUES (12741, 632, 2020, 3, 450.00, 138);
INSERT INTO public.orcamento VALUES (12742, 632, 2020, 4, 450.00, 138);
INSERT INTO public.orcamento VALUES (12743, 632, 2020, 5, 450.00, 138);
INSERT INTO public.orcamento VALUES (12744, 632, 2020, 6, 450.00, 138);
INSERT INTO public.orcamento VALUES (12745, 632, 2020, 7, 450.00, 138);
INSERT INTO public.orcamento VALUES (12746, 632, 2020, 8, 450.00, 138);
INSERT INTO public.orcamento VALUES (12747, 632, 2020, 9, 450.00, 138);
INSERT INTO public.orcamento VALUES (12748, 632, 2020, 10, 450.00, 138);
INSERT INTO public.orcamento VALUES (12749, 632, 2020, 11, 450.00, 138);
INSERT INTO public.orcamento VALUES (12750, 632, 2020, 12, 450.00, 138);
INSERT INTO public.orcamento VALUES (12751, 632, 2021, 1, 450.00, 138);
INSERT INTO public.orcamento VALUES (12752, 632, 2021, 2, 450.00, 138);
INSERT INTO public.orcamento VALUES (12753, 632, 2021, 3, 450.00, 138);
INSERT INTO public.orcamento VALUES (12754, 632, 2021, 4, 450.00, 138);
INSERT INTO public.orcamento VALUES (12755, 632, 2021, 5, 450.00, 138);
INSERT INTO public.orcamento VALUES (12756, 632, 2021, 6, 450.00, 138);
INSERT INTO public.orcamento VALUES (12757, 632, 2021, 7, 450.00, 138);
INSERT INTO public.orcamento VALUES (12758, 632, 2021, 8, 450.00, 138);
INSERT INTO public.orcamento VALUES (12759, 632, 2021, 9, 450.00, 138);
INSERT INTO public.orcamento VALUES (12760, 632, 2021, 10, 450.00, 138);
INSERT INTO public.orcamento VALUES (12761, 633, 2018, 11, 500.00, 138);
INSERT INTO public.orcamento VALUES (12762, 633, 2018, 12, 500.00, 138);
INSERT INTO public.orcamento VALUES (12763, 633, 2019, 1, 500.00, 138);
INSERT INTO public.orcamento VALUES (12764, 633, 2019, 2, 500.00, 138);
INSERT INTO public.orcamento VALUES (12765, 633, 2019, 3, 500.00, 138);
INSERT INTO public.orcamento VALUES (12766, 633, 2019, 4, 500.00, 138);
INSERT INTO public.orcamento VALUES (12767, 633, 2019, 5, 500.00, 138);
INSERT INTO public.orcamento VALUES (12768, 633, 2019, 6, 500.00, 138);
INSERT INTO public.orcamento VALUES (12769, 633, 2019, 7, 500.00, 138);
INSERT INTO public.orcamento VALUES (12770, 633, 2019, 8, 500.00, 138);
INSERT INTO public.orcamento VALUES (12771, 633, 2019, 9, 500.00, 138);
INSERT INTO public.orcamento VALUES (12772, 633, 2019, 10, 500.00, 138);
INSERT INTO public.orcamento VALUES (12773, 633, 2019, 11, 500.00, 138);
INSERT INTO public.orcamento VALUES (12774, 633, 2019, 12, 500.00, 138);
INSERT INTO public.orcamento VALUES (12775, 633, 2020, 1, 500.00, 138);
INSERT INTO public.orcamento VALUES (12776, 633, 2020, 2, 500.00, 138);
INSERT INTO public.orcamento VALUES (12777, 633, 2020, 3, 500.00, 138);
INSERT INTO public.orcamento VALUES (12778, 633, 2020, 4, 500.00, 138);
INSERT INTO public.orcamento VALUES (12779, 633, 2020, 5, 500.00, 138);
INSERT INTO public.orcamento VALUES (12780, 633, 2020, 6, 500.00, 138);
INSERT INTO public.orcamento VALUES (12781, 633, 2020, 7, 500.00, 138);
INSERT INTO public.orcamento VALUES (12782, 633, 2020, 8, 500.00, 138);
INSERT INTO public.orcamento VALUES (12783, 633, 2020, 9, 500.00, 138);
INSERT INTO public.orcamento VALUES (12784, 633, 2020, 10, 500.00, 138);
INSERT INTO public.orcamento VALUES (12785, 633, 2020, 11, 500.00, 138);
INSERT INTO public.orcamento VALUES (12786, 633, 2020, 12, 500.00, 138);
INSERT INTO public.orcamento VALUES (12787, 633, 2021, 1, 500.00, 138);
INSERT INTO public.orcamento VALUES (12788, 633, 2021, 2, 500.00, 138);
INSERT INTO public.orcamento VALUES (12789, 633, 2021, 3, 500.00, 138);
INSERT INTO public.orcamento VALUES (12790, 633, 2021, 4, 500.00, 138);
INSERT INTO public.orcamento VALUES (12791, 633, 2021, 5, 500.00, 138);
INSERT INTO public.orcamento VALUES (12792, 633, 2021, 6, 500.00, 138);
INSERT INTO public.orcamento VALUES (12793, 633, 2021, 7, 500.00, 138);
INSERT INTO public.orcamento VALUES (12794, 633, 2021, 8, 500.00, 138);
INSERT INTO public.orcamento VALUES (12795, 633, 2021, 9, 500.00, 138);
INSERT INTO public.orcamento VALUES (12796, 633, 2021, 10, 500.00, 138);
INSERT INTO public.orcamento VALUES (12797, 634, 2018, 11, 200.00, 138);
INSERT INTO public.orcamento VALUES (12798, 634, 2018, 12, 200.00, 138);
INSERT INTO public.orcamento VALUES (12799, 634, 2019, 1, 200.00, 138);
INSERT INTO public.orcamento VALUES (12800, 634, 2019, 2, 200.00, 138);
INSERT INTO public.orcamento VALUES (12801, 634, 2019, 3, 200.00, 138);
INSERT INTO public.orcamento VALUES (12802, 634, 2019, 4, 200.00, 138);
INSERT INTO public.orcamento VALUES (12803, 634, 2019, 5, 200.00, 138);
INSERT INTO public.orcamento VALUES (12804, 634, 2019, 6, 200.00, 138);
INSERT INTO public.orcamento VALUES (12805, 634, 2019, 7, 200.00, 138);
INSERT INTO public.orcamento VALUES (12806, 634, 2019, 8, 200.00, 138);
INSERT INTO public.orcamento VALUES (12807, 634, 2019, 9, 200.00, 138);
INSERT INTO public.orcamento VALUES (12808, 634, 2019, 10, 200.00, 138);
INSERT INTO public.orcamento VALUES (12809, 634, 2019, 11, 200.00, 138);
INSERT INTO public.orcamento VALUES (12810, 634, 2019, 12, 200.00, 138);
INSERT INTO public.orcamento VALUES (12811, 634, 2020, 1, 200.00, 138);
INSERT INTO public.orcamento VALUES (12812, 634, 2020, 2, 200.00, 138);
INSERT INTO public.orcamento VALUES (12813, 634, 2020, 3, 200.00, 138);
INSERT INTO public.orcamento VALUES (12814, 634, 2020, 4, 200.00, 138);
INSERT INTO public.orcamento VALUES (12815, 634, 2020, 5, 200.00, 138);
INSERT INTO public.orcamento VALUES (12816, 634, 2020, 6, 200.00, 138);
INSERT INTO public.orcamento VALUES (12817, 634, 2020, 7, 200.00, 138);
INSERT INTO public.orcamento VALUES (12818, 634, 2020, 8, 200.00, 138);
INSERT INTO public.orcamento VALUES (12819, 634, 2020, 9, 200.00, 138);
INSERT INTO public.orcamento VALUES (12820, 634, 2020, 10, 200.00, 138);
INSERT INTO public.orcamento VALUES (12821, 634, 2020, 11, 200.00, 138);
INSERT INTO public.orcamento VALUES (12822, 634, 2020, 12, 200.00, 138);
INSERT INTO public.orcamento VALUES (12823, 634, 2021, 1, 200.00, 138);
INSERT INTO public.orcamento VALUES (12824, 634, 2021, 2, 200.00, 138);
INSERT INTO public.orcamento VALUES (12825, 634, 2021, 3, 200.00, 138);
INSERT INTO public.orcamento VALUES (12826, 634, 2021, 4, 200.00, 138);
INSERT INTO public.orcamento VALUES (12827, 634, 2021, 5, 200.00, 138);
INSERT INTO public.orcamento VALUES (12828, 634, 2021, 6, 200.00, 138);
INSERT INTO public.orcamento VALUES (12829, 634, 2021, 7, 200.00, 138);
INSERT INTO public.orcamento VALUES (12830, 634, 2021, 8, 200.00, 138);
INSERT INTO public.orcamento VALUES (12831, 634, 2021, 9, 200.00, 138);
INSERT INTO public.orcamento VALUES (12832, 634, 2021, 10, 200.00, 138);
INSERT INTO public.orcamento VALUES (12833, 635, 2018, 11, 100.00, 138);
INSERT INTO public.orcamento VALUES (12834, 635, 2018, 12, 100.00, 138);
INSERT INTO public.orcamento VALUES (12835, 635, 2019, 1, 100.00, 138);
INSERT INTO public.orcamento VALUES (12836, 635, 2019, 2, 100.00, 138);
INSERT INTO public.orcamento VALUES (12837, 635, 2019, 3, 100.00, 138);
INSERT INTO public.orcamento VALUES (12838, 635, 2019, 4, 100.00, 138);
INSERT INTO public.orcamento VALUES (12839, 635, 2019, 5, 100.00, 138);
INSERT INTO public.orcamento VALUES (12840, 635, 2019, 6, 100.00, 138);
INSERT INTO public.orcamento VALUES (12841, 635, 2019, 7, 100.00, 138);
INSERT INTO public.orcamento VALUES (12842, 635, 2019, 8, 100.00, 138);
INSERT INTO public.orcamento VALUES (12843, 635, 2019, 9, 100.00, 138);
INSERT INTO public.orcamento VALUES (12844, 635, 2019, 10, 100.00, 138);
INSERT INTO public.orcamento VALUES (12845, 635, 2019, 11, 100.00, 138);
INSERT INTO public.orcamento VALUES (12846, 635, 2019, 12, 100.00, 138);
INSERT INTO public.orcamento VALUES (12847, 635, 2020, 1, 100.00, 138);
INSERT INTO public.orcamento VALUES (12848, 635, 2020, 2, 100.00, 138);
INSERT INTO public.orcamento VALUES (12849, 635, 2020, 3, 100.00, 138);
INSERT INTO public.orcamento VALUES (12850, 635, 2020, 4, 100.00, 138);
INSERT INTO public.orcamento VALUES (12851, 635, 2020, 5, 100.00, 138);
INSERT INTO public.orcamento VALUES (12852, 635, 2020, 6, 100.00, 138);
INSERT INTO public.orcamento VALUES (12853, 635, 2020, 7, 100.00, 138);
INSERT INTO public.orcamento VALUES (12854, 635, 2020, 8, 100.00, 138);
INSERT INTO public.orcamento VALUES (12855, 635, 2020, 9, 100.00, 138);
INSERT INTO public.orcamento VALUES (12856, 635, 2020, 10, 100.00, 138);
INSERT INTO public.orcamento VALUES (12857, 635, 2020, 11, 100.00, 138);
INSERT INTO public.orcamento VALUES (12858, 635, 2020, 12, 100.00, 138);
INSERT INTO public.orcamento VALUES (12859, 635, 2021, 1, 100.00, 138);
INSERT INTO public.orcamento VALUES (12860, 635, 2021, 2, 100.00, 138);
INSERT INTO public.orcamento VALUES (12861, 635, 2021, 3, 100.00, 138);
INSERT INTO public.orcamento VALUES (12862, 635, 2021, 4, 100.00, 138);
INSERT INTO public.orcamento VALUES (12863, 635, 2021, 5, 100.00, 138);
INSERT INTO public.orcamento VALUES (12864, 635, 2021, 6, 100.00, 138);
INSERT INTO public.orcamento VALUES (12865, 635, 2021, 7, 100.00, 138);
INSERT INTO public.orcamento VALUES (12866, 635, 2021, 8, 100.00, 138);
INSERT INTO public.orcamento VALUES (12867, 635, 2021, 9, 100.00, 138);
INSERT INTO public.orcamento VALUES (12868, 635, 2021, 10, 100.00, 138);
INSERT INTO public.orcamento VALUES (12869, 636, 2018, 11, 200.00, 138);
INSERT INTO public.orcamento VALUES (12870, 636, 2018, 12, 200.00, 138);
INSERT INTO public.orcamento VALUES (12871, 636, 2019, 1, 200.00, 138);
INSERT INTO public.orcamento VALUES (12872, 636, 2019, 2, 200.00, 138);
INSERT INTO public.orcamento VALUES (12873, 636, 2019, 3, 200.00, 138);
INSERT INTO public.orcamento VALUES (12874, 636, 2019, 4, 200.00, 138);
INSERT INTO public.orcamento VALUES (12875, 636, 2019, 5, 200.00, 138);
INSERT INTO public.orcamento VALUES (12876, 636, 2019, 6, 200.00, 138);
INSERT INTO public.orcamento VALUES (12877, 636, 2019, 7, 200.00, 138);
INSERT INTO public.orcamento VALUES (12878, 636, 2019, 8, 200.00, 138);
INSERT INTO public.orcamento VALUES (12879, 636, 2019, 9, 200.00, 138);
INSERT INTO public.orcamento VALUES (12880, 636, 2019, 10, 200.00, 138);
INSERT INTO public.orcamento VALUES (12881, 636, 2019, 11, 200.00, 138);
INSERT INTO public.orcamento VALUES (12882, 636, 2019, 12, 200.00, 138);
INSERT INTO public.orcamento VALUES (12883, 636, 2020, 1, 200.00, 138);
INSERT INTO public.orcamento VALUES (12884, 636, 2020, 2, 200.00, 138);
INSERT INTO public.orcamento VALUES (12885, 636, 2020, 3, 200.00, 138);
INSERT INTO public.orcamento VALUES (12886, 636, 2020, 4, 200.00, 138);
INSERT INTO public.orcamento VALUES (12887, 636, 2020, 5, 200.00, 138);
INSERT INTO public.orcamento VALUES (12888, 636, 2020, 6, 200.00, 138);
INSERT INTO public.orcamento VALUES (12889, 636, 2020, 7, 200.00, 138);
INSERT INTO public.orcamento VALUES (12890, 636, 2020, 8, 200.00, 138);
INSERT INTO public.orcamento VALUES (12891, 636, 2020, 9, 200.00, 138);
INSERT INTO public.orcamento VALUES (12892, 636, 2020, 10, 200.00, 138);
INSERT INTO public.orcamento VALUES (12893, 636, 2020, 11, 200.00, 138);
INSERT INTO public.orcamento VALUES (12894, 636, 2020, 12, 200.00, 138);
INSERT INTO public.orcamento VALUES (12895, 636, 2021, 1, 200.00, 138);
INSERT INTO public.orcamento VALUES (12896, 636, 2021, 2, 200.00, 138);
INSERT INTO public.orcamento VALUES (12897, 636, 2021, 3, 200.00, 138);
INSERT INTO public.orcamento VALUES (12898, 636, 2021, 4, 200.00, 138);
INSERT INTO public.orcamento VALUES (12899, 636, 2021, 5, 200.00, 138);
INSERT INTO public.orcamento VALUES (12900, 636, 2021, 6, 200.00, 138);
INSERT INTO public.orcamento VALUES (12901, 636, 2021, 7, 200.00, 138);
INSERT INTO public.orcamento VALUES (12902, 636, 2021, 8, 200.00, 138);
INSERT INTO public.orcamento VALUES (12903, 636, 2021, 9, 200.00, 138);
INSERT INTO public.orcamento VALUES (12904, 636, 2021, 10, 200.00, 138);
INSERT INTO public.orcamento VALUES (12905, 637, 2018, 11, 45.00, 138);
INSERT INTO public.orcamento VALUES (12906, 637, 2018, 12, 45.00, 138);
INSERT INTO public.orcamento VALUES (12907, 637, 2019, 1, 45.00, 138);
INSERT INTO public.orcamento VALUES (12908, 637, 2019, 2, 45.00, 138);
INSERT INTO public.orcamento VALUES (12909, 637, 2019, 3, 45.00, 138);
INSERT INTO public.orcamento VALUES (12910, 637, 2019, 4, 45.00, 138);
INSERT INTO public.orcamento VALUES (12911, 637, 2019, 5, 45.00, 138);
INSERT INTO public.orcamento VALUES (12912, 637, 2019, 6, 45.00, 138);
INSERT INTO public.orcamento VALUES (12913, 637, 2019, 7, 45.00, 138);
INSERT INTO public.orcamento VALUES (12914, 637, 2019, 8, 45.00, 138);
INSERT INTO public.orcamento VALUES (12915, 637, 2019, 9, 45.00, 138);
INSERT INTO public.orcamento VALUES (12916, 637, 2019, 10, 45.00, 138);
INSERT INTO public.orcamento VALUES (12917, 637, 2019, 11, 45.00, 138);
INSERT INTO public.orcamento VALUES (12918, 637, 2019, 12, 45.00, 138);
INSERT INTO public.orcamento VALUES (12919, 637, 2020, 1, 45.00, 138);
INSERT INTO public.orcamento VALUES (12920, 637, 2020, 2, 45.00, 138);
INSERT INTO public.orcamento VALUES (12921, 637, 2020, 3, 45.00, 138);
INSERT INTO public.orcamento VALUES (12922, 637, 2020, 4, 45.00, 138);
INSERT INTO public.orcamento VALUES (12923, 637, 2020, 5, 45.00, 138);
INSERT INTO public.orcamento VALUES (12924, 637, 2020, 6, 45.00, 138);
INSERT INTO public.orcamento VALUES (12925, 637, 2020, 7, 45.00, 138);
INSERT INTO public.orcamento VALUES (12926, 637, 2020, 8, 45.00, 138);
INSERT INTO public.orcamento VALUES (12927, 637, 2020, 9, 45.00, 138);
INSERT INTO public.orcamento VALUES (12928, 637, 2020, 10, 45.00, 138);
INSERT INTO public.orcamento VALUES (12929, 637, 2020, 11, 45.00, 138);
INSERT INTO public.orcamento VALUES (12930, 637, 2020, 12, 45.00, 138);
INSERT INTO public.orcamento VALUES (12931, 637, 2021, 1, 45.00, 138);
INSERT INTO public.orcamento VALUES (12932, 637, 2021, 2, 45.00, 138);
INSERT INTO public.orcamento VALUES (12933, 637, 2021, 3, 45.00, 138);
INSERT INTO public.orcamento VALUES (12934, 637, 2021, 4, 45.00, 138);
INSERT INTO public.orcamento VALUES (12935, 637, 2021, 5, 45.00, 138);
INSERT INTO public.orcamento VALUES (12936, 637, 2021, 6, 45.00, 138);
INSERT INTO public.orcamento VALUES (12937, 637, 2021, 7, 45.00, 138);
INSERT INTO public.orcamento VALUES (12938, 637, 2021, 8, 45.00, 138);
INSERT INTO public.orcamento VALUES (12939, 637, 2021, 9, 45.00, 138);
INSERT INTO public.orcamento VALUES (12940, 637, 2021, 10, 45.00, 138);
INSERT INTO public.orcamento VALUES (12941, 638, 2018, 11, 70.00, 138);
INSERT INTO public.orcamento VALUES (12942, 638, 2018, 12, 70.00, 138);
INSERT INTO public.orcamento VALUES (12943, 638, 2019, 1, 70.00, 138);
INSERT INTO public.orcamento VALUES (12944, 638, 2019, 2, 70.00, 138);
INSERT INTO public.orcamento VALUES (12945, 638, 2019, 3, 70.00, 138);
INSERT INTO public.orcamento VALUES (12946, 638, 2019, 4, 70.00, 138);
INSERT INTO public.orcamento VALUES (12947, 638, 2019, 5, 70.00, 138);
INSERT INTO public.orcamento VALUES (12948, 638, 2019, 6, 70.00, 138);
INSERT INTO public.orcamento VALUES (12949, 638, 2019, 7, 70.00, 138);
INSERT INTO public.orcamento VALUES (12950, 638, 2019, 8, 70.00, 138);
INSERT INTO public.orcamento VALUES (12951, 638, 2019, 9, 70.00, 138);
INSERT INTO public.orcamento VALUES (12952, 638, 2019, 10, 70.00, 138);
INSERT INTO public.orcamento VALUES (12953, 638, 2019, 11, 70.00, 138);
INSERT INTO public.orcamento VALUES (12954, 638, 2019, 12, 70.00, 138);
INSERT INTO public.orcamento VALUES (12955, 638, 2020, 1, 70.00, 138);
INSERT INTO public.orcamento VALUES (12956, 638, 2020, 2, 70.00, 138);
INSERT INTO public.orcamento VALUES (12957, 638, 2020, 3, 70.00, 138);
INSERT INTO public.orcamento VALUES (12958, 638, 2020, 4, 70.00, 138);
INSERT INTO public.orcamento VALUES (12959, 638, 2020, 5, 70.00, 138);
INSERT INTO public.orcamento VALUES (12960, 638, 2020, 6, 70.00, 138);
INSERT INTO public.orcamento VALUES (12961, 638, 2020, 7, 70.00, 138);
INSERT INTO public.orcamento VALUES (12962, 638, 2020, 8, 70.00, 138);
INSERT INTO public.orcamento VALUES (12963, 638, 2020, 9, 70.00, 138);
INSERT INTO public.orcamento VALUES (12964, 638, 2020, 10, 70.00, 138);
INSERT INTO public.orcamento VALUES (12965, 638, 2020, 11, 70.00, 138);
INSERT INTO public.orcamento VALUES (12966, 638, 2020, 12, 70.00, 138);
INSERT INTO public.orcamento VALUES (12967, 638, 2021, 1, 70.00, 138);
INSERT INTO public.orcamento VALUES (12968, 638, 2021, 2, 70.00, 138);
INSERT INTO public.orcamento VALUES (12969, 638, 2021, 3, 70.00, 138);
INSERT INTO public.orcamento VALUES (12970, 638, 2021, 4, 70.00, 138);
INSERT INTO public.orcamento VALUES (12971, 638, 2021, 5, 70.00, 138);
INSERT INTO public.orcamento VALUES (12972, 638, 2021, 6, 70.00, 138);
INSERT INTO public.orcamento VALUES (12973, 638, 2021, 7, 70.00, 138);
INSERT INTO public.orcamento VALUES (12974, 638, 2021, 8, 70.00, 138);
INSERT INTO public.orcamento VALUES (12975, 638, 2021, 9, 70.00, 138);
INSERT INTO public.orcamento VALUES (12976, 638, 2021, 10, 70.00, 138);
INSERT INTO public.orcamento VALUES (12977, 639, 2018, 11, 300.00, 138);
INSERT INTO public.orcamento VALUES (12978, 639, 2018, 12, 300.00, 138);
INSERT INTO public.orcamento VALUES (12979, 639, 2019, 1, 300.00, 138);
INSERT INTO public.orcamento VALUES (12980, 639, 2019, 2, 300.00, 138);
INSERT INTO public.orcamento VALUES (12981, 639, 2019, 3, 300.00, 138);
INSERT INTO public.orcamento VALUES (12982, 639, 2019, 4, 300.00, 138);
INSERT INTO public.orcamento VALUES (12983, 639, 2019, 5, 300.00, 138);
INSERT INTO public.orcamento VALUES (12984, 639, 2019, 6, 300.00, 138);
INSERT INTO public.orcamento VALUES (12985, 639, 2019, 7, 300.00, 138);
INSERT INTO public.orcamento VALUES (12986, 639, 2019, 8, 300.00, 138);
INSERT INTO public.orcamento VALUES (12987, 639, 2019, 9, 300.00, 138);
INSERT INTO public.orcamento VALUES (12988, 639, 2019, 10, 300.00, 138);
INSERT INTO public.orcamento VALUES (12989, 639, 2019, 11, 300.00, 138);
INSERT INTO public.orcamento VALUES (12990, 639, 2019, 12, 300.00, 138);
INSERT INTO public.orcamento VALUES (12991, 639, 2020, 1, 300.00, 138);
INSERT INTO public.orcamento VALUES (12992, 639, 2020, 2, 300.00, 138);
INSERT INTO public.orcamento VALUES (12993, 639, 2020, 3, 300.00, 138);
INSERT INTO public.orcamento VALUES (12994, 639, 2020, 4, 300.00, 138);
INSERT INTO public.orcamento VALUES (12995, 639, 2020, 5, 300.00, 138);
INSERT INTO public.orcamento VALUES (12996, 639, 2020, 6, 300.00, 138);
INSERT INTO public.orcamento VALUES (12997, 639, 2020, 7, 300.00, 138);
INSERT INTO public.orcamento VALUES (12998, 639, 2020, 8, 300.00, 138);
INSERT INTO public.orcamento VALUES (12999, 639, 2020, 9, 300.00, 138);
INSERT INTO public.orcamento VALUES (13000, 639, 2020, 10, 300.00, 138);
INSERT INTO public.orcamento VALUES (13001, 639, 2020, 11, 300.00, 138);
INSERT INTO public.orcamento VALUES (13002, 639, 2020, 12, 300.00, 138);
INSERT INTO public.orcamento VALUES (13003, 639, 2021, 1, 300.00, 138);
INSERT INTO public.orcamento VALUES (13004, 639, 2021, 2, 300.00, 138);
INSERT INTO public.orcamento VALUES (13005, 639, 2021, 3, 300.00, 138);
INSERT INTO public.orcamento VALUES (13006, 639, 2021, 4, 300.00, 138);
INSERT INTO public.orcamento VALUES (13007, 639, 2021, 5, 300.00, 138);
INSERT INTO public.orcamento VALUES (13008, 639, 2021, 6, 300.00, 138);
INSERT INTO public.orcamento VALUES (13009, 639, 2021, 7, 300.00, 138);
INSERT INTO public.orcamento VALUES (13010, 639, 2021, 8, 300.00, 138);
INSERT INTO public.orcamento VALUES (13011, 639, 2021, 9, 300.00, 138);
INSERT INTO public.orcamento VALUES (13012, 639, 2021, 10, 300.00, 138);
INSERT INTO public.orcamento VALUES (13013, 640, 2018, 11, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13014, 640, 2018, 12, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13015, 640, 2019, 1, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13016, 640, 2019, 2, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13017, 640, 2019, 3, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13018, 640, 2019, 4, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13019, 640, 2019, 5, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13020, 640, 2019, 6, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13021, 640, 2019, 7, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13022, 640, 2019, 8, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13023, 640, 2019, 9, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13024, 640, 2019, 10, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13025, 640, 2019, 11, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13026, 640, 2019, 12, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13027, 640, 2020, 1, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13028, 640, 2020, 2, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13029, 640, 2020, 3, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13030, 640, 2020, 4, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13031, 640, 2020, 5, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13032, 640, 2020, 6, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13033, 640, 2020, 7, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13034, 640, 2020, 8, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13035, 640, 2020, 9, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13036, 640, 2020, 10, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13037, 640, 2020, 11, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13038, 640, 2020, 12, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13039, 640, 2021, 1, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13040, 640, 2021, 2, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13041, 640, 2021, 3, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13042, 640, 2021, 4, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13043, 640, 2021, 5, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13044, 640, 2021, 6, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13045, 640, 2021, 7, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13046, 640, 2021, 8, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13047, 640, 2021, 9, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13048, 640, 2021, 10, 3000.00, 138);
INSERT INTO public.orcamento VALUES (13049, 641, 2018, 11, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13050, 641, 2018, 12, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13051, 641, 2019, 1, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13052, 641, 2019, 2, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13053, 641, 2019, 3, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13054, 641, 2019, 4, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13055, 641, 2019, 5, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13056, 641, 2019, 6, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13057, 641, 2019, 7, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13058, 641, 2019, 8, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13059, 641, 2019, 9, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13060, 641, 2019, 10, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13061, 641, 2019, 11, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13062, 641, 2019, 12, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13063, 641, 2020, 1, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13064, 641, 2020, 2, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13065, 641, 2020, 3, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13066, 641, 2020, 4, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13067, 641, 2020, 5, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13068, 641, 2020, 6, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13069, 641, 2020, 7, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13070, 641, 2020, 8, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13071, 641, 2020, 9, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13072, 641, 2020, 10, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13073, 641, 2020, 11, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13074, 641, 2020, 12, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13075, 641, 2021, 1, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13076, 641, 2021, 2, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13077, 641, 2021, 3, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13078, 641, 2021, 4, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13079, 641, 2021, 5, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13080, 641, 2021, 6, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13081, 641, 2021, 7, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13082, 641, 2021, 8, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13083, 641, 2021, 9, 1000.00, 138);
INSERT INTO public.orcamento VALUES (13084, 641, 2021, 10, 1000.00, 138);


--
-- TOC entry 2989 (class 0 OID 16430)
-- Dependencies: 207
-- Data for Name: recuperacao_senha; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2999 (class 0 OID 16451)
-- Dependencies: 217
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.usuario VALUES (138, 'jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=', 'John', 'john@mybudget.com', true, '2018-11-28 18:58:51.286821', '', NULL, 0, false);


--
-- TOC entry 3006 (class 0 OID 0)
-- Dependencies: 208
-- Name: sq_categoria; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sq_categoria', 641, true);


--
-- TOC entry 3007 (class 0 OID 0)
-- Dependencies: 209
-- Name: sq_conta; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sq_conta', 140, true);


--
-- TOC entry 3008 (class 0 OID 0)
-- Dependencies: 210
-- Name: sq_conta_saldo; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sq_conta_saldo', 2335, true);


--
-- TOC entry 3009 (class 0 OID 0)
-- Dependencies: 211
-- Name: sq_lancamento; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sq_lancamento', 5121, true);


--
-- TOC entry 3010 (class 0 OID 0)
-- Dependencies: 212
-- Name: sq_lancamento_serie; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sq_lancamento_serie', 63, true);


--
-- TOC entry 3011 (class 0 OID 0)
-- Dependencies: 213
-- Name: sq_log_acesso; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sq_log_acesso', 391, true);


--
-- TOC entry 3012 (class 0 OID 0)
-- Dependencies: 204
-- Name: sq_log_erros; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sq_log_erros', 127, true);


--
-- TOC entry 3013 (class 0 OID 0)
-- Dependencies: 214
-- Name: sq_orcamento; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sq_orcamento', 13084, true);


--
-- TOC entry 3014 (class 0 OID 0)
-- Dependencies: 215
-- Name: sq_recuperacaosenha; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sq_recuperacaosenha', 12, true);


--
-- TOC entry 3015 (class 0 OID 0)
-- Dependencies: 216
-- Name: sq_usuario; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sq_usuario', 138, true);


--
-- TOC entry 2813 (class 2606 OID 16474)
-- Name: categoria pk_categoria; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT pk_categoria PRIMARY KEY (id);


--
-- TOC entry 2815 (class 2606 OID 16476)
-- Name: conta pk_conta; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conta
    ADD CONSTRAINT pk_conta PRIMARY KEY (id);


--
-- TOC entry 2817 (class 2606 OID 16478)
-- Name: conta_saldo pk_conta_saldo; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conta_saldo
    ADD CONSTRAINT pk_conta_saldo PRIMARY KEY (id);


--
-- TOC entry 2819 (class 2606 OID 16480)
-- Name: databasechangeloglock pk_databasechangeloglock; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT pk_databasechangeloglock PRIMARY KEY (id);


--
-- TOC entry 2821 (class 2606 OID 16482)
-- Name: lancamento pk_lancamento; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lancamento
    ADD CONSTRAINT pk_lancamento PRIMARY KEY (id);


--
-- TOC entry 2823 (class 2606 OID 16484)
-- Name: lancamento_serie pk_lancamento_serie; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lancamento_serie
    ADD CONSTRAINT pk_lancamento_serie PRIMARY KEY (id);


--
-- TOC entry 2825 (class 2606 OID 16486)
-- Name: log_acesso pk_log_acesso; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log_acesso
    ADD CONSTRAINT pk_log_acesso PRIMARY KEY (id);


--
-- TOC entry 2827 (class 2606 OID 16488)
-- Name: log_erros pk_log_erros; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log_erros
    ADD CONSTRAINT pk_log_erros PRIMARY KEY (id);


--
-- TOC entry 2829 (class 2606 OID 16490)
-- Name: orcamento pk_orcamento; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT pk_orcamento PRIMARY KEY (id);


--
-- TOC entry 2831 (class 2606 OID 16492)
-- Name: recuperacao_senha pk_recuperacao_senha; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recuperacao_senha
    ADD CONSTRAINT pk_recuperacao_senha PRIMARY KEY (id);


--
-- TOC entry 2834 (class 2606 OID 16494)
-- Name: usuario pk_usuario; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT pk_usuario PRIMARY KEY (id);


--
-- TOC entry 2836 (class 2606 OID 16496)
-- Name: usuario usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- TOC entry 2832 (class 1259 OID 16497)
-- Name: uk_cod_usuario; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX uk_cod_usuario ON public.recuperacao_senha USING btree (usuario_id, cod_solicitacao);


--
-- TOC entry 2842 (class 2606 OID 16498)
-- Name: lancamento FK_lancamento_cartao_credito_fatura; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lancamento
    ADD CONSTRAINT "FK_lancamento_cartao_credito_fatura" FOREIGN KEY (cartao_credito_fatura_id) REFERENCES public.conta(id);


--
-- TOC entry 2838 (class 2606 OID 16503)
-- Name: conta cartao_credito_fatura_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conta
    ADD CONSTRAINT cartao_credito_fatura_id FOREIGN KEY (conta_pagamento_fatura_id) REFERENCES public.conta(id);


--
-- TOC entry 2837 (class 2606 OID 16508)
-- Name: categoria fk_categoria_001; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT fk_categoria_001 FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- TOC entry 2839 (class 2606 OID 16513)
-- Name: conta fk_conta_001; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conta
    ADD CONSTRAINT fk_conta_001 FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- TOC entry 2840 (class 2606 OID 16518)
-- Name: conta_saldo fk_conta_saldo_001; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conta_saldo
    ADD CONSTRAINT fk_conta_saldo_001 FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- TOC entry 2843 (class 2606 OID 16523)
-- Name: lancamento fk_lancamento_001; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lancamento
    ADD CONSTRAINT fk_lancamento_001 FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- TOC entry 2844 (class 2606 OID 16528)
-- Name: lancamento fk_lancamento_cartao; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lancamento
    ADD CONSTRAINT fk_lancamento_cartao FOREIGN KEY (lancamento_cartao_id) REFERENCES public.lancamento(id);


--
-- TOC entry 2845 (class 2606 OID 16533)
-- Name: lancamento fk_lancamento_categoria; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lancamento
    ADD CONSTRAINT fk_lancamento_categoria FOREIGN KEY (categoria_id) REFERENCES public.categoria(id);


--
-- TOC entry 2841 (class 2606 OID 16538)
-- Name: conta_saldo fk_lancamento_conta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conta_saldo
    ADD CONSTRAINT fk_lancamento_conta FOREIGN KEY (conta_id) REFERENCES public.conta(id);


--
-- TOC entry 2846 (class 2606 OID 16543)
-- Name: lancamento fk_lancamento_conta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lancamento
    ADD CONSTRAINT fk_lancamento_conta FOREIGN KEY (conta_id) REFERENCES public.conta(id);


--
-- TOC entry 2847 (class 2606 OID 16548)
-- Name: lancamento fk_lancamento_conta_destino; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lancamento
    ADD CONSTRAINT fk_lancamento_conta_destino FOREIGN KEY (conta_destino_id) REFERENCES public.conta(id);


--
-- TOC entry 2848 (class 2606 OID 16553)
-- Name: lancamento fk_lancamento_conta_origem; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lancamento
    ADD CONSTRAINT fk_lancamento_conta_origem FOREIGN KEY (conta_origem_id) REFERENCES public.conta(id);


--
-- TOC entry 2849 (class 2606 OID 16558)
-- Name: lancamento fk_lancamento_serie; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lancamento
    ADD CONSTRAINT fk_lancamento_serie FOREIGN KEY (lancamento_serie_id) REFERENCES public.lancamento_serie(id);


--
-- TOC entry 2850 (class 2606 OID 16563)
-- Name: lancamento_serie fk_lancamento_serie_001; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lancamento_serie
    ADD CONSTRAINT fk_lancamento_serie_001 FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- TOC entry 2851 (class 2606 OID 16568)
-- Name: orcamento fk_orcamento_001; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT fk_orcamento_001 FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- TOC entry 2852 (class 2606 OID 16573)
-- Name: orcamento fk_orcamento_item_categoria; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT fk_orcamento_item_categoria FOREIGN KEY (categoria_id) REFERENCES public.categoria(id);


--
-- TOC entry 2853 (class 2606 OID 16578)
-- Name: recuperacao_senha fk_recuperacao_senha_01; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recuperacao_senha
    ADD CONSTRAINT fk_recuperacao_senha_01 FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


-- Completed on 2018-11-28 16:05:58 -03

--
-- PostgreSQL database dump complete
--

