import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer,
  BarChart, Bar, XAxis, YAxis, CartesianGrid,
} from "recharts";
import * as api from "../services/api.js";
import AuditoriaPainel from "../components/AuditoriaPainel.jsx";

const ROTULOS_STATUS = { ABERTA: "Aberta", EM_ANDAMENTO: "Em andamento", CONCLUIDA: "Concluída" };
const ROTULOS_PRIORIDADE = { BAIXA: "Baixa", MEDIA: "Média", ALTA: "Alta" };
const CORES_STATUS = { ABERTA: "#4f9bd0", EM_ANDAMENTO: "#d9a21b", CONCLUIDA: "#1d9b5f" };
const CORES_PRIORIDADE = { BAIXA: "#7fa6c0", MEDIA: "#d9a21b", ALTA: "#c8452f" };

function contar(lista, campo, rotulos = {}) {
  const mapa = {};
  lista.forEach((i) => { mapa[i[campo]] = (mapa[i[campo]] || 0) + 1; });
  return Object.entries(mapa).map(([chave, value]) => ({
    chave, name: rotulos[chave] || chave, value,
  }));
}

function Rosca({ titulo, dados, cores }) {
  const total = dados.reduce((soma, d) => soma + d.value, 0);
  return (
    <div className="grafico">
      <h3>{titulo}</h3>
      {total === 0 ? (
        <p className="vazio">Sem dados.</p>
      ) : (
        <ResponsiveContainer width="100%" height={250}>
          <PieChart>
            <Pie data={dados} dataKey="value" nameKey="name"
                 innerRadius={55} outerRadius={85} paddingAngle={2}>
              {dados.map((d) => <Cell key={d.chave} fill={cores[d.chave] || "#8894a0"} />)}
            </Pie>
            <Tooltip formatter={(v) => `${v} (${Math.round((v / total) * 100)}%)`} />
            <Legend />
          </PieChart>
        </ResponsiveContainer>
      )}
    </div>
  );
}

export default function Dashboard() {
  const [solicitacoes, setSolicitacoes] = useState([]);
  const [logs, setLogs] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");

  useEffect(() => {
    Promise.all([api.listar(), api.listarAuditoria()])
      .then(([s, l]) => { setSolicitacoes(s); setLogs(l); })
      .catch((e) => setErro(e.message))
      .finally(() => setCarregando(false));
  }, []);

  const total = solicitacoes.length;
  const abertas = solicitacoes.filter((s) => s.status === "ABERTA").length;
  const andamento = solicitacoes.filter((s) => s.status === "EM_ANDAMENTO").length;
  const concluidas = solicitacoes.filter((s) => s.status === "CONCLUIDA").length;
  const taxa = total ? Math.round((concluidas / total) * 100) : 0;

  const porCategoria = contar(solicitacoes, "categoria");

  return (
    <div className="pagina">
      <header className="topo topo-linha">
        <div>
          <h1>Dashboard</h1>
          <p>Visão geral das solicitações e do histórico de eventos.</p>
        </div>
        <Link to="/" className="botao-secundario">← Voltar</Link>
      </header>

      {erro && (
        <div className="aviso aviso-erro" role="alert"><span>{erro}</span></div>
      )}

      {carregando ? (
        <p className="vazio">Carregando…</p>
      ) : (
        <>
          <div className="resumo">
            <div className="cartao"><strong>{total}</strong><span>Solicitações ativas</span></div>
            <div className="cartao evento-EDICAO"><strong>{abertas}</strong><span>Abertas</span></div>
            <div className="cartao evento-ATUALIZACAO_STATUS"><strong>{andamento}</strong><span>Em andamento</span></div>
            <div className="cartao evento-CRIACAO"><strong>{concluidas}</strong><span>Concluídas</span></div>
            <div className="cartao"><strong>{taxa}%</strong><span>Taxa de conclusão</span></div>
          </div>

          <div className="graficos">
            <section className="painel">
              <Rosca titulo="Proporção por status"
                     dados={contar(solicitacoes, "status", ROTULOS_STATUS)}
                     cores={CORES_STATUS} />
            </section>
            <section className="painel">
              <Rosca titulo="Proporção por prioridade"
                     dados={contar(solicitacoes, "prioridade", ROTULOS_PRIORIDADE)}
                     cores={CORES_PRIORIDADE} />
            </section>
            <section className="painel">
              <div className="grafico">
                <h3>Solicitações por categoria</h3>
                {porCategoria.length === 0 ? (
                  <p className="vazio">Sem dados.</p>
                ) : (
                  <ResponsiveContainer width="100%" height={250}>
                    <BarChart data={porCategoria}>
                      <CartesianGrid strokeDasharray="3 3" stroke="#8894a055" />
                      <XAxis dataKey="name" tick={{ fill: "#8894a0", fontSize: 12 }} />
                      <YAxis allowDecimals={false} tick={{ fill: "#8894a0", fontSize: 12 }} />
                      <Tooltip />
                      <Bar dataKey="value" name="Solicitações" fill="#4f9bd0" radius={[4, 4, 0, 0]} />
                    </BarChart>
                  </ResponsiveContainer>
                )}
              </div>
            </section>
          </div>

          <section className="painel">
            <h2>Histórico de eventos</h2>
            <AuditoriaPainel logs={logs} carregando={false} />
          </section>
        </>
      )}
    </div>
  );
}