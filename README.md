# Documentação da API de Usuários

Esta API permite manipular informações sobre usuários.

## Criar Usuário

Endpoint para criar um novo usuário.

**Request:**

```bash
curl --location --request POST 'http://localhost:8080/users' \
--header 'Content-Type: application/json' \
--data '{
    "name": "William",
    "nick": "Wil",
    "birth_date": "1980-12-10",
    "stack": ["Kotlin", "Node"]
}'
```
## Atualizar Usuário

Endpoint para atualizar informações de um usuário existente.

**Request:**

```bash
curl --location --request PUT 'http://localhost:8080/users/1' \
--header 'Content-Type: application/json' \
--data '{
    "name": "William Junior",
    "nick": "Wil",
    "birth_date": "1980-12-10",
    "stack": []
}'
```

## Listar Usuários

Endpoint para listar todos os usuários.

**Request:**

```bash
curl --location 'http://localhost:8080/users'
```

## Obter Detalhes do Usuário

Endpoint para obter os detalhes de um usuário específico.

**Request:**

```bash
curl --location 'http://localhost:8080/users/1'
```

## Deletar Usuário

Endpoint para deletar um usuário existente.

**Request:**

```bash
curl --location --request DELETE 'http://localhost:8080/users/1'
```

