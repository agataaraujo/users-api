package com.example.users.application.repository

import com.example.users.application.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun existsByName(name: String): Boolean
}