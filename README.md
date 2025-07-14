# Diário Digital - Sistema de Gerenciamento de Entradas

## Sobre o Projeto

Este é um sistema de diário digital desenvolvido em Java usando Swing para interface gráfica. O sistema permite que os usuários criem contas, façam login e gerenciem suas entradas diárias com funcionalidades completas de CRUD (Create, Read, Update, Delete).

## Funcionalidades

### 🏠 Tela Principal
- Interface principal com menu de navegação
- Controle de autenticação de usuários
- Acesso a todas as funcionalidades do sistema

### 👤 Sistema de Usuários
- **Cadastro de Usuários**: Registro com validação de dados
- **Login**: Autenticação segura
- **Validações**: Email, senha, campos obrigatórios

### 📝 Gerenciamento de Entradas
- **Nova Entrada**: Criar entradas com título, conteúdo e data
- **Visualizar Entradas**: Lista completa com busca e filtros
- **Editar Entradas**: Modificar entradas existentes
- **Excluir Entradas**: Remover entradas com confirmação

### 📊 Relatórios
- **Resumo Geral**: Estatísticas completas do diário
- **Relatório Mensal**: Dados agrupados por mês
- **Entradas Recentes**: Últimas 10 entradas
- **Exportação**: Salvar relatórios em arquivo texto

## Tecnologias Utilizadas

- **Java 22**: Linguagem principal
- **Swing**: Interface gráfica
- **Gson**: Serialização JSON
- **Lombok**: Redução de código boilerplate
- **Maven**: Gerenciamento de dependências

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── org/example/
│   │       ├── Main.java                 # Classe principal
│   │       ├── entities/
│   │       │   ├── User.java            # Entidade usuário
│   │       │   └── Entries.java         # Entidade entrada
│   │       ├── utils/
│   │       │   ├── JSONManager.java     # Gerenciador de JSON
│   │       │   └── ValidationUtils.java # Utilitários de validação
│   │       └── gui/
│   │           ├── MainFrame.java       # Tela principal
│   │           ├── LoginFrame.java      # Tela de login
│   │           ├── RegisterFrame.java   # Tela de cadastro
│   │           ├── NewEntryFrame.java   # Nova entrada
│   │           ├── ViewEntriesFrame.java # Visualizar entradas
│   │           ├── EditEntryFrame.java  # Editar entrada
│   │           └── ReportsFrame.java    # Relatórios
│   └── resources/
│       └── users.json                   # Arquivo de dados
```

## Validações Implementadas

### Usuário
- Nome: mínimo 3 caracteres
- Login: 3-20 caracteres, único no sistema
- Email: formato válido
- Senha: mínimo 4 caracteres
- Confirmação de senha obrigatória

### Entradas
- Título: 3-100 caracteres
- Conteúdo: 10-5000 caracteres
- Data: gerada automaticamente
- Campos obrigatórios validados

## Como Executar

### Pré-requisitos
- Java 22 ou superior
- Maven (ou usar o wrapper incluído)

### Passos para Execução

1. **Clonar/Baixar o projeto**
   ```bash
   cd Desktop_Final
   ```

2. **Compilar o projeto**
   ```bash
   mvn clean compile
   ```

3. **Executar o sistema**
   ```bash
   mvn exec:java -Dexec.mainClass="org.example.Main"
   ```

   Ou alternativamente:
   ```bash
   java -cp "target/classes;target/dependency/*" org.example.Main
   ```

### Primeira Execução
1. Execute o sistema
2. Clique em "Cadastrar" para criar sua conta
3. Faça login com suas credenciais
4. Comece a usar o diário digital!

## Funcionalidades Detalhadas

### 🔐 Autenticação
- Sistema seguro de login/logout
- Validação de credenciais
- Sessão de usuário persistente

### 📖 Entradas do Diário
- **Criar**: Título, conteúdo e data automática
- **Listar**: Tabela com busca e ordenação
- **Editar**: Modificar qualquer campo (exceto data)
- **Excluir**: Confirmação antes da exclusão

### 🔍 Busca e Filtros
- Busca por título, conteúdo ou data
- Filtros em tempo real
- Ordenação por colunas

### 📈 Relatórios Avançados
- Estatísticas gerais (total de entradas, palavras, etc.)
- Relatórios por período
- Identificação de padrões de escrita
- Exportação de dados

## Persistência de Dados

Os dados são armazenados em formato JSON no arquivo `src/main/resources/users.json`:

```json
[
  {
    "login": "usuario1",
    "password": "senha123",
    "email": "usuario@email.com",
    "name": "Nome do Usuário",
    "entries": [
      {
        "title": "Minha primeira entrada",
        "content": "Conteúdo da entrada...",
        "author": "Nome do Usuário",
        "date": "2025-01-15"
      }
    ]
  }
]
```

## Características Técnicas

- **Padrão MVC**: Separação clara de responsabilidades
- **Validação robusta**: Entrada de dados segura
- **Interface responsiva**: Experiência de usuário fluida
- **Código limpo**: Uso de Lombok para redução de boilerplate
- **Arquitetura extensível**: Fácil adição de novas funcionalidades

## Capturas de Tela

### Tela Principal
- Menu central com todas as opções
- Barra superior com controles de usuário
- Design moderno e intuitivo

### Formulários
- Validação em tempo real
- Mensagens de erro claras
- Campos formatados adequadamente

### Relatórios
- Múltiplas abas com diferentes visões
- Gráficos e estatísticas detalhadas
- Opção de exportação

## Melhorias Futuras

- [ ] Backup automático dos dados
- [ ] Criptografia de senhas
- [ ] Temas personalizáveis
- [ ] Anexar imagens às entradas
- [ ] Sincronização em nuvem
- [ ] App mobile complementar

## Suporte

Para dúvidas ou problemas, verifique:
1. Se o Java 22+ está instalado
2. Se todas as dependências foram baixadas
3. Se o arquivo JSON tem permissão de escrita
4. Se há espaço suficiente em disco

---

**Desenvolvido com ❤️ usando Java e Swing**
