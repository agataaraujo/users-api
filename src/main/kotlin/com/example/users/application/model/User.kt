package com.example.users.application.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDate
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity(name = "users")
data class User(
    @Id @GeneratedValue
    var id: Long? = null,
    var nick: String?,
    var name: String,
    var birth_date: LocalDate,
    @field:Size(max = 32, message = "Cada item da lista deve ter no máximo 32 caracteres")
    val stack: List<@NotBlank @Size(max = 32) String>?
)