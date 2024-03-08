package com.example.users.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDate

@Entity(name = "users")
data class User(
    @Id @GeneratedValue
    var id: Long? = null,
    var apelido: String?,
    var nome: String,
    var data_nascimento: LocalDate,
    var lista_tecnologias: String?
)