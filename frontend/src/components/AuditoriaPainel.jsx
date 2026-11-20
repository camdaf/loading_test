import { useState } from "react";

const TIPOS = {
  CRIACAO: "Criações",
  EDICAO: "Edições",
  ATUALIZACAO_STATUS: "Mudanças de status",
  EXCLUSAO: "Exclusões",
};

const formatar = (iso) =>
  new Date(iso).toLocaleString("pt-BR", { dateStyle: "short", timeStyle: "medium" });

export default function AuditoriaPainel({ logs, carregando }) {
  const [tipo, setTipo] = useState("");

  const contagem = logs.reduce((acc, l) => {
    acc[l.tipoEvento] = (acc[l.tipoEvento] || 0) + 1;
    return acc;
  }, {});

  const visiveis = tipo ? logs.filter((l) => l.tipoEvento === tipo) : logs;

  return (
    <>
      <div className="resumo">
        {Object.entries(TIPOS).map(([chave, rotulo]) => (
          <div key={chave} className={`cartao evento-${chave}`}>
            <strong>{contagem[chave] || 0}</strong>
            <span>{rotulo}</span>
          </div>
        ))}
      </div>

      <div className="filtros">
        <label className="campo">
          <span>Tipo de evento</span>
          <select value={tipo} onChange={(e) => setTipo(e.target.value)}>
            <option value="">Todos</option>
            {Object.entries(TIPOS).map(([chave, rotulo]) => (
              <option key={chave} value={chave}>{rotulo}</option>
            ))}
          </select>
        </label>
      </div>

      {carregando && logs.length === 0 ? (
        <p className="vazio">Carregando…</p>
      ) : visiveis.length === 0 ? (
        <p className="vazio">Nenhum evento registrado.</p>
      ) : (
        <ul className="log-lista">
          {visiveis.map((l) => (
            <li key={l.id} className={`log evento-${l.tipoEvento}`}>
              <div className="log-texto">
                <strong>{l.mensagem}</strong>
                <small># {l.nomeSolicitacao}</small>
              </div>
              <span className={`selo status-${l.status}`}>{l.statusDescricao}</span>
              <time dateTime={l.dataEvento}>{formatar(l.dataEvento)}</time>
            </li>
          ))}
        </ul>
      )}
    </>
  );
}