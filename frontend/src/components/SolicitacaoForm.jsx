  import { useState } from "react";

  const CATEGORIAS = ["TI", "Infraestrutura", "Recursos Humanos", "Financeiro", "Compras", "Outros"];
  const VAZIO = { titulo: "", descricao: "", categoria: "", prioridade: "" };

  export default function SolicitacaoForm({ onSalvar, onCancelar, inicial }) {
    const [dados, setDados] = useState(
      inicial
        ? { titulo: inicial.titulo, descricao: inicial.descricao,
            categoria: inicial.categoria, prioridade: inicial.prioridade }
        : VAZIO
    );
    const [erros, setErros] = useState({});
    const [mensagem, setMensagem] = useState("");
    const [enviando, setEnviando] = useState(false);

    const alterar = (campo) => (e) => setDados({ ...dados, [campo]: e.target.value });

    // Validação rápida no navegador; a API valida novamente (fonte de verdade)
    function validar() {
      const e = {};
      if (!dados.titulo.trim()) e.titulo = "O título é obrigatório.";
      if (!dados.descricao.trim()) e.descricao = "A descrição é obrigatória.";
      if (!dados.categoria) e.categoria = "Escolha uma categoria.";
      if (!dados.prioridade) e.prioridade = "Escolha uma prioridade.";
      return e;
    }

    async function enviar(ev) {
      ev.preventDefault();
      const e = validar();
      setErros(e);
      setMensagem("");
      if (Object.keys(e).length) return;

      setEnviando(true);
      try {
        await onSalvar(dados);
        if (!inicial) setDados(VAZIO);
      } catch (err) {
        setErros(err.campos || {});
        setMensagem(err.message);
      } finally {
        setEnviando(false);
      }
    }

    const campo = (nome, rotulo, controle) => (
      <label className="campo">
        <span>{rotulo}</span>
        {controle}
        {erros[nome] && <small className="erro">{erros[nome]}</small>}
      </label>
    );

    return (
      <form onSubmit={enviar} noValidate>
        {mensagem && <p className="erro erro-geral">{mensagem}</p>}

        {campo("titulo", "Título",
          <input value={dados.titulo} onChange={alterar("titulo")} maxLength={150} />)}

        {campo("descricao", "Descrição",
          <textarea rows={4} value={dados.descricao} onChange={alterar("descricao")} maxLength={2000} />)}

        <div className="linha">
          {campo("categoria", "Categoria",
            <select value={dados.categoria} onChange={alterar("categoria")}>
              <option value="">Selecione…</option>
              {CATEGORIAS.map((c) => <option key={c}>{c}</option>)}
            </select>)}

          {campo("prioridade", "Prioridade",
            <select value={dados.prioridade} onChange={alterar("prioridade")}>
              <option value="">Selecione…</option>
              <option value="BAIXA">Baixa</option>
              <option value="MEDIA">Média</option>
              <option value="ALTA">Alta</option>
            </select>)}
        </div>
        <div className="acoes">
        <button className="botao" disabled={enviando}>
            {enviando ? "Salvando…" : inicial ? "Salvar alterações" : "Registrar solicitação"}
          </button>
          {inicial && (
            <button type="button" className="botao-secundario" onClick={onCancelar}>
              Cancelar
            </button>
          )}
        </div>
      </form>
    );
  }
