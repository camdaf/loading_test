import { useCallback, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import * as api from "./services/api.js";
import SolicitacaoForm from "./components/SolicitacaoForm.jsx";
import SolicitacaoLista from "./components/SolicitacaoLista.jsx";
import SolicitacaoDetalhe from "./components/SolicitacaoDetalhe.jsx";

export default function App() {
  const [itens, setItens] = useState([]);
  const [filtros, setFiltros] = useState({ status: "", prioridade: "" });
  const [selecionada, setSelecionada] = useState(null);
  const [aviso, setAviso] = useState(null); // { tipo: "ok" | "erro", texto }
  const [carregando, setCarregando] = useState(false);
  const [editando, setEditando] = useState(null);

  const carregar = useCallback(async () => {
    setCarregando(true);
    try {
      setItens(await api.listar(filtros));
    } catch (e) {
      setAviso({ tipo: "erro", texto: e.message });
    } finally {
      setCarregando(false);
    }
  }, [filtros]);

  useEffect(() => {
    carregar();
  }, [carregar]);

  async function abrir(id) {
    try {
      setSelecionada(await api.buscar(id));
    } catch (e) {
      setAviso({ tipo: "erro", texto: e.message });
    }
  }

  async function avancar(item) {
    try {
      const atualizada = await api.alterarStatus(item.id, item.proximoStatus);
      setSelecionada(atualizada);
      setAviso({
        tipo: "ok",
        texto: `Solicitação #${atualizada.id} agora está ${atualizada.statusDescricao}.`,
      });
      await carregar();
    } catch (e) {
      setAviso({ tipo: "erro", texto: e.message });
    }
  }

  // Usada pelo formulário para criar e para editar (erros sobem para o formulário)
  async function salvar(dados) {
    if (editando) {
      const atualizada = await api.editar(editando.id, dados);
      setEditando(null);
      setSelecionada(atualizada);
      setAviso({ tipo: "ok", texto: "Solicitação atualizada." });
    } else {
      const nova = await api.criar(dados);
      setAviso({ tipo: "ok", texto: `Solicitação ${nova.titulo} registrada` });
    }
    await carregar();
  }

  async function excluir(item) {
    if (!window.confirm(`Excluir "${item.titulo}"? Esta ação não pode ser desfeita.`)) return;
    try {
      await api.excluir(item.id);
      setSelecionada(null);
      setAviso({ tipo: "ok", texto: "Solicitação excluída." });
      await carregar();
    } catch (e) {
      setAviso({ tipo: "erro", texto: e.message });
    }
  }

  return (
    <div className="pagina">
      <header className="topo topo-linha">
        <div>
          <h1>Solicitações internas</h1>
          <p>Registre demandas e acompanhe cada uma até a conclusão.</p>
        </div>
        <Link to="/dashboard" className="botao">Ver dashboard</Link>
      </header>

      {aviso && (
        <div className={`aviso aviso-${aviso.tipo}`} role="alert">
          <span>{aviso.texto}</span>
          <button onClick={() => setAviso(null)} aria-label="Fechar aviso">×</button>
        </div>
      )}

      <main className="grade">
        <section className="painel">
          <h2>{editando ? "Editar solicitação" : "Nova solicitação"}</h2>
          <SolicitacaoForm
            key={editando ? editando.id : "novo"}
            inicial={editando}
            onSalvar={salvar}
            onCancelar={() => setEditando(null)}
          />
        </section>

        <section className="painel">
          <h2>Solicitações</h2>
          <SolicitacaoLista
            itens={itens}
            filtros={filtros}
            onFiltrar={setFiltros}
            onAbrir={abrir}
            selecionadaId={selecionada?.id}
            carregando={carregando}
          />
          {selecionada && (
            <SolicitacaoDetalhe
              item={selecionada}
              onAvancar={avancar}
              onEditar={() => setEditando(selecionada)}
              onExcluir={() => excluir(selecionada)}
              onFechar={() => setSelecionada(null)}
            />
          )}
        </section>
      </main>
    </div>
  );
}