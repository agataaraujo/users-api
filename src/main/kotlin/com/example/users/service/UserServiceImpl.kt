package com.example.users.service

import com.example.users.model.User
import com.example.users.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.*

@Service
class UserServiceImpl(private val repository: UserRepository) : UserService {

    override fun create(user: User): User {
        // Verifica se o nome não está em branco
        Assert.hasLength(user.nome, "[nome] não pode estar em branco!")

        // Verifica se o nome é único
        if (repository.existsByNome(user.nome)) {
            throw IllegalArgumentException("[nome] deve ser único!")
        }

        // Verifica se o nome tem no máximo 255 caracteres
        Assert.isTrue(user.nome.length <= 255, "[nome] deve ter no máximo 255 caracteres!")

        // Verifica se o apelido tem no máximo 32 caracteres
        user.apelido?.let {
            Assert.isTrue(it.length <= 32, "[apelido] deve ter no máximo 32 caracteres!")
        }

        // Verifica se a data de nascimento é válida
        try {
            Assert.notNull(user.data_nascimento, "[data_nascimento] não pode ser nula!")
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("[data_nascimento] não é uma data válida!")
        }

        return repository.save(user)
    }

    override fun getAll(): List<User> {
        return repository.findAll()
    }

    override fun getById(id: Long): Optional<User> {
        return repository.findById(id)
    }

    override fun update(id: Long, user: User): Optional<User> {
        val optional = getById(id)
        if(optional.isEmpty) return Optional.empty<User>()
        return optional.map {
            val userToUpdate = it.copy(
                nome = user.nome,
                apelido = user.apelido,
                data_nascimento = user.data_nascimento,
                lista_tecnologias = user.lista_tecnologias
            )
            repository.save(userToUpdate)
        }
    }

    override fun delete(id: Long) {
        repository.findById(id).map {
            repository.delete(it)
        }.orElseThrow { throw RuntimeException("Id $id não encontrado") }
    }
}