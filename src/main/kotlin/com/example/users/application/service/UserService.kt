package com.example.users.application.service

import com.example.users.application.model.User
import java.util.*

interface UserService {

    fun create(user: User): User

    fun getAll(): List<User>

    fun getById(id: Long) : Optional<User>

    fun update(id: Long, user: User) : Optional<User>

    fun delete(id: Long)

}