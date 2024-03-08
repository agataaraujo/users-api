package com.example.users.service

import com.example.users.model.User
import java.util.*

interface UserService {

    fun create(user: User): User

    fun getAll(): List<User>

    fun getById(id: Long) : Optional<User>

    fun update(id: Long, user: User) : Optional<User>

    fun delete(id: Long)

}