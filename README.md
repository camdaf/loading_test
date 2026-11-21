# Solicitações internas

Aplicação web para registrar solicitações internas e acompanhar sua resolução. Tem interface web, API REST, banco PostgreSQL e um dashboard com gráficos e histórico de eventos.

## Tecnologias

- **Back-end:** Java 21, Spring Boot 4.1, Spring Data JPA, Bean Validation, Lombok, Gradle, SpringDoc (Swagger)
- **Front-end:** React 18, Vite, React Router, Recharts
- **Banco:** PostgreSQL 18

## Funcionalidades

- Cadastrar, listar, consultar, editar e excluir solicitações (título, descrição, categoria e prioridade).
- Filtrar por status e prioridade.
- Avançar o status: **Aberta → Em andamento → Concluída**.
- Dashboard em `/dashboard`: totais, taxa de conclusão, gráficos por status, prioridade e categoria, e histórico de eventos com filtro por tipo.
- Auditoria gravada a cada criação, edição, mudança de status e exclusão.

## Regras de negócio

- O status avança uma etapa por vez: sem pular, voltar ou repetir.
- Solicitação **Concluída** não pode mudar de status nem ser editada.
- Título, descrição, categoria e prioridade são obrigatórios.
- A solicitação e seu registro de auditoria são gravados na mesma transação.

## Estrutura

```
br.com.carlos.loadingjr_test
├── controller    # SolicitacoesController, AuditoriaController
├── service       # SolicitacaoService, AuditoriaService
├── database      # model (entidades e enums) e repository
├── dto           # entrada e saída da API
├── exception     # exceções de negócio
└── handler       # GlobalExceptionHandler

frontend/src
├── main.jsx      # rotas: "/" e "/dashboard"
├── App.jsx       # tela principal: estado e ações
├── pages/Dashboard.jsx
├── components/   # Form, Lista, Detalhe, AuditoriaPainel
├── services/api.js
└── styles.css
```

## Como rodar

**Pré-requisitos:** JDK 21, Node.js LTS e PostgreSQL em execução. O Gradle não precisa ser instalado (wrapper incluído).

**1. Banco.** Crie o banco vazio; as tabelas são criadas na primeira execução:

```sql
CREATE DATABASE solicitacoes;
```

**2. Configuração.** Em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/solicitacoes
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
```

**3. Back-end** (pasta com o `build.gradle`):

```powershell
.\gradlew.bat bootRun
```

Aguarde `Started LoadingjrTestApplication`. A API fica em `http://localhost:8080`.

Se aparecer `Gradle requires JVM 17 or later`, aponte o terminal para o JDK 21 (ajuste o nome da pasta):

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.13.10002-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat --stop
```

**4. Front-end** (outro terminal, pasta do front):

```powershell
npm install
npm run dev
```

Abra `http://localhost:5173`. O Vite repassa `/api` para `localhost:8080`, então não há problema de CORS.

Para parar, use `Ctrl + C` em cada terminal.

## API

Swagger: `http://localhost:8080/swagger-ui.html`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/solicitacoes` | Cria (status inicial Aberta) |
| `GET` | `/api/solicitacoes` | Lista; filtros `?status=` e `?prioridade=` |
| `GET` | `/api/solicitacoes/{id}` | Consulta uma |
| `PUT` | `/api/solicitacoes/{id}` | Edita título, descrição, categoria e prioridade |
| `PUT` | `/api/solicitacoes/{id}/status?novoStatus=` | Avança o status |
| `DELETE` | `/api/solicitacoes/{id}` | Exclui |
| `GET` | `/api/auditoria` | Histórico de eventos, do mais recente ao mais antigo |

Valores: `status` = `ABERTA`, `EM_ANDAMENTO`, `CONCLUIDA`; `prioridade` = `BAIXA`, `MEDIA`, `ALTA`.

Erros voltam como JSON (`400` para dados inválidos ou regra violada):

```json
{ "erro": "Status inválido. A solicitação aberta só pode ser atualizada para EM_ANDAMENTO." }
```

Para campos inválidos, a resposta traz também `"detalhes": { "titulo": "..." }`.

## Auditoria e dashboard

A tabela `tabela_auditoria` recebe uma linha por evento, com o tipo gravado na própria linha:

| Tipo (`TipoEvento`) | Quando |
|---|---|
| `CRIACAO` | Ao cadastrar |
| `EDICAO` | Ao editar |
| `ATUALIZACAO_STATUS` | Ao avançar o status |
| `EXCLUSAO` | Ao excluir |

Colunas: `id`, `id_solicitacao`, `nome_solicitacao`, `status_solicitacao`, `tipo_evento`, `data_evento`. Não há chave estrangeira para `solicitacao`, de propósito: o histórico sobrevive à exclusão.

O dashboard busca `/api/solicitacoes` e `/api/auditoria` e calcula os números no front. Solicitações excluídas saem dos gráficos, mas continuam no histórico.

## Problemas comuns

| Sintoma | Solução |
|---|---|
| `Connection refused` na porta 5432 ou banco inexistente | Inicie o PostgreSQL e crie o banco `solicitacoes` |
| `Port 8080 was already in use` | Pare a instância antiga do back-end |
| `Gradle requires JVM 17 or later` | Aponte `JAVA_HOME` para o JDK 21 (veja "Como rodar") |
| `mvnw.cmd` não reconhecido | O projeto usa Gradle: `.\gradlew.bat bootRun`, na pasta do `build.gradle` |
| `'vite' não é reconhecido` | Rode `npm install` na pasta do front |
| `Failed to resolve import` | Confira o nome do arquivo e rode `npm install react-router-dom recharts` |
| `http proxy error ... ECONNREFUSED` ou "Erro inesperado" na tela | O back-end está fora do ar; suba-o e veja o log |
| `coluna ... de tabela_auditoria não existe` | Tabela antiga: `DROP TABLE tabela_auditoria;` e suba de novo |

Em caso de erro 500, o motivo está no terminal do back-end: procure o último `Caused by:`.