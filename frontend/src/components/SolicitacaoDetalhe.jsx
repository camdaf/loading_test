const formatar = (iso) =>
  new Date(iso).toLocaleString("pt-BR", { dateStyle: "short", timeStyle: "short" });

export default function SolicitacaoDetalhe({ item, onAvancar, onEditar, onExcluir, onFechar }) {
  return (
    <article className="detalhe">
      <header>
        <h3>{item.titulo}</h3>
        <button className="fechar" onClick={onFechar} aria-label="Fechar detalhes">×</button>
      </header>

      <p className="descricao">{item.descricao}</p>

      <dl>
        <div><dt>Categoria</dt><dd>{item.categoria}</dd></div>
        <div><dt>Prioridade</dt><dd>{item.prioridadeDescricao}</dd></div>
        <div>
          <dt>Status</dt>
          <dd><span className={`selo status-${item.status}`}>{item.statusDescricao}</span></dd>
        </div>
        <div><dt>Registrada em</dt><dd>{formatar(item.criadaEm)}</dd></div>
        <div><dt>Última atualização</dt><dd>{formatar(item.atualizadaEm)}</dd></div>
      </dl>

      {!item.proximoStatus && (
        <p className="vazio">Fluxo concluído. O status não pode mais ser alterado.</p>
      )}

      <div className="acoes">
        {item.proximoStatus && (
          <button className="botao" onClick={() => onAvancar(item)}>
            Atualizar para "{item.proximoStatusDescricao}"
          </button>
        )}
        {item.proximoStatus && (
          <button className="botao-secundario" onClick={onEditar}>Editar</button>
        )}
        <button className="botao-perigo" onClick={onExcluir}>Excluir</button>
      </div>
    </article>
  );
}