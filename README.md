# RenovaFacens

Projeto desenvolvido para a disciplina UPX 2 do curso de Análise e Desenvolvimento de Sistemas (ADS) da FACENS.

## Sobre o projeto

O RenovaFacens tem como objetivo incentivar a realização de ações sociais e ambientais dentro da comunidade acadêmica, permitindo que estudantes participem de missões e recebam pontos de acordo com suas participações.

O sistema contará com diferentes funcionalidades para estudantes e embaixadores, incluindo cadastro de missões, participação em missões, validação de participações e gerenciamento de pontos.

## Tecnologias

- Front-end: HTML, CSS e JavaScript
- Back-end: Java + Spring Boot
- Banco de dados: SQL
- Acesso ao banco: JDBC
- Comunicação: API REST utilizando HTTP e JSON
- Controle de versão: Git + GitHub
- Testes da API: Postman

---

# Padrões de Desenvolvimento

Esta seção define as regras utilizadas pela equipe para manter o código organizado, consistente e fácil de integrar entre as diferentes partes do projeto.

## 1. Estrutura do projeto

O projeto será dividido em três partes principais:

```text
renovafacens/
├── backend/
│   └── Contém todo o código relacionado ao servidor e à lógica do sistema.
├── frontend/
│   └── Contém a interface utilizada pelo usuário e a comunicação com a API.
├── database/
│   └── Contém os arquivos relacionados à criação e configuração do banco de dados, como scripts SQL.
└── README.md
```

## 2. Organização do Back-end

O código do back-end deverá ser organizado de acordo com a responsabilidade de cada classe.

A estrutura seguirá inicialmente o seguinte padrão:

```text
backend/
└── src/
    └── ...
        └── com.renovafacens/
            ├── controller/
            ├── model/
            ├── repository/
            └── service/
```

### Model

Responsável por representar os dados utilizados pelo sistema.

Exemplos:

- `Usuario`
- `Missao`
- `Participacao`

### Repository

Responsável pela comunicação com o banco de dados.

Exemplos:

- `UsuarioRepository`
- `MissaoRepository`
- `ParticipacaoRepository`

### Service

Responsável pelas regras de negócio da aplicação.

Exemplos:

- `UsuarioService`
- `MissaoService`
- `ParticipacaoService`

### Controller

Responsável por receber as requisições do front-end e disponibilizar os endpoints da API.

Exemplos:

- `UsuarioController`
- `MissaoController`
- `ParticipacaoController`

O fluxo entre essas camadas deverá seguir, sempre que possível:

```text
Controller → Service → Repository → Banco de dados
```

O Controller não deverá acessar o banco de dados diretamente.

## 3. Convenções de nomes

Os nomes utilizados no projeto deverão seguir um padrão consistente.

### Java

Classes deverão utilizar PascalCase:

- `Usuario`
- `Missao`
- `Participacao`
- `UsuarioController`
- `MissaoService`

Variáveis e métodos deverão utilizar camelCase:

- `nomeUsuario`
- `dataParticipacao`
- `buscarMissoes()`
- `criarMissao()`

Os nomes deverão ser claros e representar sua finalidade. Devem ser evitados nomes genéricos como:

- `x`
- `teste`
- `coisa`
- `dados`
- `temp`

### Banco de dados

Tabelas e colunas deverão seguir um padrão único.

Exemplo de tabelas:

- `USUARIO`
- `MISSAO`
- `PARTICIPACAO`

Exemplo de colunas:

- `id`
- `nome`
- `email`
- `data_participacao`

Não deverão ser utilizados padrões diferentes para representar o mesmo tipo de informação.

## 4. API

A comunicação entre o front-end e o back-end será realizada através de uma API REST.

As requisições utilizarão HTTP e os dados serão enviados e recebidos em formato JSON.

Os endpoints deverão utilizar nomes claros e relacionados aos recursos que representam.

Exemplos:

```text
GET  /api/missoes
POST /api/missoes
GET  /api/missoes/{id}
POST /api/missoes/{id}/participar
POST /api/participacoes/{id}/validar
GET  /api/usuarios/{id}/pontos
```

Deve-se evitar a utilização de nomes de endpoints que misturem a ação com o recurso, como:

```text
/api/getMissoes
```

Em vez disso, deve-se utilizar o método HTTP para indicar a operação:

```text
GET /api/missoes
```

## 5. Git e branches

A branch `main` representa a versão principal do projeto.

O desenvolvimento de novas funcionalidades deverá ser realizado em branches específicas, evitando alterações diretamente na `main`.

Branches utilizadas no projeto:

- `main`
- `feature/usuarios`
- `feature/missoes`
- `feature/participacoes`

Cada branch `feature` deverá ser utilizada para o desenvolvimento da funcionalidade correspondente.

Após a conclusão e os testes da funcionalidade, suas alterações poderão ser integradas à `main`.

## 6. Commits

As mensagens dos commits deverão ser claras e indicar o tipo de alteração realizada.

Serão utilizados os seguintes prefixos:

- `feat:` — criação de uma nova funcionalidade.
- `fix:` — correção de um problema.
- `test:` — criação ou alteração de testes.
- `docs:` — alteração na documentação.
- `chore:` — configuração ou manutenção do projeto.

Exemplos:

```text
feat: cria cadastro de usuario

feat: adiciona endpoint de missoes

fix: corrige validacao de participacao

test: adiciona testes de missoes

docs: atualiza documentacao da API

chore: configura estrutura inicial do backend
```

Devem ser evitadas mensagens pouco informativas, como:

- `aaa`
- `teste`
- `final`
- `final2`
- `mudancas`

A mensagem do commit deve permitir que outro integrante entenda, de forma rápida, o que foi alterado.

## 7. Testes

As funcionalidades do back-end deverão ser testadas antes de serem consideradas concluídas.

O Postman será utilizado para testar os endpoints da API durante o desenvolvimento.

Sempre que possível, os testes deverão verificar:

- Se a requisição é aceita corretamente.
- Se os dados recebidos são tratados corretamente.
- Se o resultado retornado pela API está correto.
- Se situações inválidas são tratadas adequadamente.

## 8. Integração entre Front-end e Back-end

O front-end deverá se comunicar com o back-end exclusivamente através da API definida para o sistema.

O front-end não deverá acessar diretamente o banco de dados.

O fluxo esperado é:

```text
Front-end
    ↓
API REST
    ↓
Back-end
    ↓
Banco de dados
```

Alterações nos endpoints, nos formatos dos dados ou nas regras de comunicação deverão ser comunicadas aos integrantes responsáveis pelo front-end e back-end.

## 9. Alterações no código

Antes de enviar alterações para o repositório, o integrante deverá verificar se:

- O código está funcionando.
- A funcionalidade foi testada.
- Não foram introduzidos erros em outras funcionalidades.
- Não foram adicionados arquivos desnecessários ao repositório.
- A mensagem do commit está de acordo com o padrão definido.

Alterações que afetem diretamente outra parte do projeto deverão ser comunicadas aos integrantes responsáveis por ela.

## 10. Manutenção dos padrões

Este documento deverá ser atualizado sempre que a equipe definir uma alteração nos padrões de desenvolvimento.

Caso seja necessário modificar uma convenção, estrutura ou tecnologia utilizada no projeto, a alteração deverá ser discutida entre os integrantes antes de ser adotada.
