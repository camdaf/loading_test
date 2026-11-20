export default function SolicitacaoLista({ itens, filtros, onFiltrar, onAbrir, selecionadaId, carregando }) {
  const alterar = (campo) => (e) => onFiltrar({ ...filtros, [campo]: e.target.value });

  return (
    <>
      <div className="filtros">
        <label className="campo">
          <span>Status</span>
          <select value={filtros.status} onChange={alterar("status")}>
            <option value="">Todos</option>
            <option value="ABERTA">Aberta</option>
            <option value="EM_ANDAMENTO">Em andamento</option>
            <option value="CONCLUIDA">Concluída</option>
          </select>
        </label>
        <label className="campo">
          <span>Prioridade</span>
          <select value={filtros.prioridade} onChange={alterar("prioridade")}>
            <option value="">Todas</option>
            <option value="BAIXA">Baixa</option>
            <option value="MEDIA">Média</option>
            <option value="ALTA">Alta</option>
          </select>
        </label>
      </div>

      {carregando && itens.length === 0 ? (
        <p className="vazio">Carregando…</p>
      ) : itens.length === 0 ? (
        <p className="vazio">Nenhuma solicitação encontrada. Registre a primeira ao lado ou ajuste os filtros.</p>
      ) : (
        <ul className="lista">
          {itens.map((s) => (
            <li key={s.id}>
              <button
                className={`item ${s.id === selecionadaId ? "ativo" : ""}`}
                onClick={() => onAbrir(s.id)}
              >
                <span className={`barra prioridade-${s.prioridade}`} aria-hidden="true" />
                <span className="item-texto">
                  <strong>{s.titulo}</strong>
                  <small>{s.categoria} · prioridade {s.prioridadeDescricao.toLowerCase()}</small>
                </span>
                <span className={`selo status-${s.status}`}>{s.statusDescricao}</span>
              </button>
            </li>
          ))}
        </ul>
      )}
    </>
  );
}
