package com.example.users

import com.example.users.model.User
import com.example.users.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired lateinit var mockMvc: MockMvc

    @Autowired lateinit var userRepository: UserRepository

    @Test
    fun `test find all users`() {

        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        userRepository.save(User(nome = "William", apelido = "Wil", data_nascimento = parsedDate, lista_tecnologias = ""))

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].nome").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].apelido").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].data_nascimento").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].lista_tecnologias").isString)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test find user by id`() {

        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        val user = userRepository.save(User(nome = "William", apelido = "Wil", data_nascimento = parsedDate, lista_tecnologias = ""))

        mockMvc.perform(MockMvcRequestBuilders.get("/users/${user.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(user.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.nome").value(user.nome))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.apelido").value(user.apelido))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.data_nascimento").value(user.data_nascimento.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.lista_tecnologias").value(user.lista_tecnologias))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create user`() {

        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = User(nome = "William", apelido = "", data_nascimento = parsedDate, lista_tecnologias = "")
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        var json = objectMapper.writeValueAsString(user)
        userRepository.deleteAll()
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.nome").value(user.nome))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.apelido").value(user.apelido))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.data_nascimento").value(parsedDate.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.lista_tecnologias").value(user.lista_tecnologias))
            .andDo(MockMvcResultHandlers.print())
        Assertions.assertFalse(userRepository.findAll().isEmpty())
    }

    @Test
    fun `test create user validation error empty name`() {

        userRepository.deleteAll()
        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = User(nome = "", apelido = "Wil", data_nascimento =parsedDate, lista_tecnologias = "")
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        var json = objectMapper.writeValueAsString(user)
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[nome] não pode estar em branco!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create user validation error unique name`() {

        userRepository.deleteAll()
        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        userRepository.save(User(nome = "William", apelido = "Wil", data_nascimento = parsedDate, lista_tecnologias = ""))
        var user = User(nome = "William", apelido = "Wil", data_nascimento = parsedDate, lista_tecnologias = "")
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        var json = objectMapper.writeValueAsString(user)
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[nome] deve ser único!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create user validation error character limit for name`() {

        userRepository.deleteAll()
        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = User(
            nome = "William Junior Almeida Araujo Mendonça Gonçalves Silva Barmosa Ferreira Nogueira Santos Cunha da Penha Almeida Araujo Mendonça Gonçalves Silva Barmosa Ferreira Nogueira Santos Cunha da Penha Almeida Araujo Mendonça Gonçalves Silva Barmosa Ferreira Nogueira Santos Cunha da Penha ",
            apelido = "Wil",
            data_nascimento = parsedDate,
            lista_tecnologias = ""
        )
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        var json = objectMapper.writeValueAsString(user)
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[nome] deve ter no máximo 255 caracteres!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create user validation error character limit for surname`() {

        userRepository.deleteAll()
        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = User(nome = "William", apelido = "William Junior Almeida Araujo Mendonça", data_nascimento = parsedDate, lista_tecnologias = "")
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        var json = objectMapper.writeValueAsString(user)
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[apelido] deve ter no máximo 32 caracteres!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test update user`() {

        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = userRepository
            .save(User(nome = "William", apelido = "Wil", data_nascimento = parsedDate, lista_tecnologias = ""))
            .copy(nome = "Updated")
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        var json = objectMapper.writeValueAsString(user)
        mockMvc.perform(MockMvcRequestBuilders.put("/users/${user.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.nome").value(user.nome))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.apelido").value(user.apelido))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.data_nascimento").value(parsedDate.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.lista_tecnologias").value(user.lista_tecnologias))
            .andDo(MockMvcResultHandlers.print())
        var findById = userRepository.findById(user.id!!)
        Assertions.assertTrue(findById.isPresent)
        Assertions.assertEquals(user.nome, findById.get().nome)
    }

    @Test
    fun `test delete user`() {

        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = userRepository
            .save(User(nome = "William", apelido = "Wil", data_nascimento = parsedDate, lista_tecnologias = ""))
            .copy(nome = "Updated")
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        var json = objectMapper.writeValueAsString(user)
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/${user.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
        var findById = userRepository.findById(user.id!!)
        Assertions.assertFalse(findById.isPresent)
    }

}