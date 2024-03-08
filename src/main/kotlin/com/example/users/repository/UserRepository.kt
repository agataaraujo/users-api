package com.example.users.repository

import com.example.users.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun existsByNome(nome: String): Boolean
}