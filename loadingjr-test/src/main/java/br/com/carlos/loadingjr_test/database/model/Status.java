package br.com.carlos.loadingjr_test.database.model;

public enum Status {
    ABERTA("Aberta"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDA("Concluída");

    private final String descricao;

    Status(String descricao) { this.descricao = descricao; }

    public String getDescricao() { return descricao; }

    public Status proximo() {
        return switch (this) {
            case ABERTA -> EM_ANDAMENTO;
            case EM_ANDAMENTO -> CONCLUIDA;
            case CONCLUIDA -> null;
        };
    }
}