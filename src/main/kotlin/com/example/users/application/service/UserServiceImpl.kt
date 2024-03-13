package com.example.users.application.service

import com.example.users.application.model.User
import com.example.users.application.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.time.format.DateTimeParseException
import java.util.*

@Service
class UserServiceImpl(private val repository: UserRepository) : UserService {

    override fun create(user: User): User {
        Assert.hasLength(user.name, "[name] não pode estar em branco!")

        if (repository.existsByName(user.name)) {
            throw IllegalArgumentException("[name] deve ser único!")
        }

        Assert.isTrue(user.name.length <= 255, "[name] deve ter no máximo 255 caracteres!")

        user.nick?.let {
            Assert.isTrue(it.length <= 32, "[nick] deve ter no máximo 32 caracteres!")
        }

        try {
            Assert.notNull(user.birth_date, "[birth_date] não pode ser nula!")
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("[birth_date] não é uma data válida!")
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
                name = user.name,
                nick = user.nick,
                birth_date = user.birth_date,
                stack = user.stack
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