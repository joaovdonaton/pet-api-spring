# API de Sistema de Adoção de Pets (Versão 2.0)

<b>Alunos:</b> João Vitor Donaton <br>
<b>Professor:</b> Vinicius Godoy <br>
<b>[Link da Apresentação](https://docs.google.com/presentation/d/13cT0ZHaYo5hq0otKDznQIyC0bLPGd1kjzCT8pPLYeIM/edit?usp=sharing)</b> <br>

### **Configurações:**

- <i>apikeys.properties</i> (não está no git)
  - Propriedade keys.google-api (chave da api do google para consultar dados de localização)
- <i>bootstrapdata.properties</i>
    - Contêm os dados para fazer o bootstrapping da base de dados
    - Detalhes adicionais no <i>BootstrapSettings.java</i>
- <i>location.properties</i>
  - URLs de apis de terceiros para consultar dados de localização
  - O <i>LocationUtils.java</i> faz a substituição dos dados "placeholder" (ADDRESS, KEY e o CEP)
- <i>messages.properties</i>
  - Mensagens de erro padronizadas para retornar nos requests
- <i>security.properties</i>
  - Secret e Issuer para gerar e validar tokens JWT

### **A API conta com endpoints para:**

- Gerenciamento de contas e autenticação
- Registro de perfil com dados de localização (através da API de geocoding do google, e a API viacep) e preferência para adoção
- Cadastro de pets
- Sistema de "matching" para encontrar pets que se encaixam no perfil do usuário.
- Envio e gerenciamento de pedidos de adoção para os pets
- Alguns endpoints foram removidos na nova versão (Escolhi focar em outros detalhes, já que implementar esses endpoints seria basicamente mais do mesmo de criar entidades e relações)
  - ~~Criação de organizações de usuários, ONGs ou governamentais~~
  - ~~Criação de campanhas para arrecadação de dinheiro/recursos e adoção~~
  - ~~Postagem de blogposts da campanha~~
