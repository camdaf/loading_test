const BASE = "/api/solicitacoes";
const BASE_AUDITORIA = "/api/auditoria";

async function requisitar(url, opcoes = {}) {
  let resposta;
  try {
    resposta = await fetch(url, {
      headers: { "Content-Type": "application/json" },
      ...opcoes,
    });
  } catch {
    throw new ErroApi("Não foi possível conectar ao servidor. Verifique se a API está em execução.");
  }
  const corpo = await resposta.json().catch(() => null);
  if (!resposta.ok) {
    throw new ErroApi(
      corpo?.mensagem || corpo?.erro || "Erro inesperado. Tente novamente.",
      corpo?.campos || corpo?.detalhes
);
  }
  return corpo;
}

export class ErroApi extends Error {
  constructor(mensagem, campos = {}) {
    super(mensagem);
    this.campos = campos || {};
  }
}

export const listar = ({ status, prioridade } = {}) => {
  const params = new URLSearchParams();
  if (status) params.set("status", status);
  if (prioridade) params.set("prioridade", prioridade);
  const qs = params.toString();
  return requisitar(qs ? `${BASE}?${qs}` : BASE);
};

export const buscar = (id) => requisitar(`${BASE}/${id}`);

export const criar = (dados) =>
  requisitar(BASE, { method: "POST", body: JSON.stringify(dados) });

export const alterarStatus = (id, status) =>
  requisitar(`${BASE}/${id}/status?novoStatus=${status}`, { method: "PUT" });

export const editar = (id, dados) =>
  requisitar(`${BASE}/${id}`, { method: "PUT", body: JSON.stringify(dados) });

export const excluir = (id) =>
  requisitar(`${BASE}/${id}`, { method: "DELETE" });

export const listarAuditoria = () => requisitar(BASE_AUDITORIA);