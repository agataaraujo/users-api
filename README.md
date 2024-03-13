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

## Parâmetros

<table>
	<thead>
		<tr>
			<th>Campo</th>
			<th>Tipo</th>
			<th>Obrigatório?</th>
			<th>Descrição</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>nick</td>
			<td>String</td>
			<td>Não</td>
			<td>Apelido do usuário. Máximo de 32 caracteres.</td>
		</tr>
		<tr>
			<td>name</td>
			<td>String</td>
			<td>Sim</td>
			<td>Nome completo do usuário. Deve ser único. Máximo de 255 caracteres.</td>
		</tr>
		<tr>
			<td>birth_date</td>
			<td>LocalDate</td>
			<td>Sim</td>
			<td>Data de nascimento do usuário. Deve ser uma data válida.</td>
		</tr>
		<tr>
			<td>stack</td>
			<td>Lista de Strings</td>
			<td>Não</td><td>Lista de habilidades do usuário. Cada item da lista pode ter no máximo 32 caracteres.</td>
		</tr>
	</tbody>
</table>

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

## Parâmetros

<table>
	<thead>
		<tr>
			<th>Campo</th>
			<th>Tipo</th>
			<th>Obrigatório?</th>
			<th>Descrição</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>id</td>
			<td>Long</td>
			<td>Sim</td>
			<td>Identificador único do usuário.</td>
		</tr>
		<tr>
			<td>nick</td>
			<td>String</td>
			<td>Não</td>
			<td>Apelido do usuário. Máximo de 32 caracteres.</td>
		</tr>
		<tr>
			<td>name</td>
			<td>String</td>
			<td>Sim</td>
			<td>Nome completo do usuário. Deve ser único. Máximo de 255 caracteres.</td>
		</tr>
		<tr>
			<td>birth_date</td>
			<td>LocalDate</td>
			<td>Sim</td>
			<td>Data de nascimento do usuário. Deve ser uma data válida.</td>
		</tr>
		<tr>
			<td>stack</td>
			<td>Lista de Strings</td>
			<td>Não</td><td>Lista de habilidades do usuário. Cada item da lista pode ter no máximo 32 caracteres.</td>
		</tr>
	</tbody>
</table>

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

