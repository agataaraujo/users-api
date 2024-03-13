package com.example.users

import com.example.users.application.model.User
import com.example.users.application.repository.UserRepository
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
        userRepository.save(User(name = "William", nick = "Wil", birth_date = parsedDate, stack = listOf("Java", "Node")))

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].name").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].nick").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].birth_date").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].stack[0]").value("Java"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].stack[1]").value("Node"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test find user by id`() {

        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        val user = userRepository.save(User(name = "William", nick = "Wil", birth_date = parsedDate, stack = listOf("Java", "Node")))

        mockMvc.perform(MockMvcRequestBuilders.get("/users/${user.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(user.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(user.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.nick").value(user.nick))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.birth_date").value(user.birth_date.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("\$stack[0]").value("Java"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$stack[1]").value("Node"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create user`() {

        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = User(name = "William", nick = "", birth_date = parsedDate, stack = listOf("Java", "Node"))
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        var json = objectMapper.writeValueAsString(user)
        userRepository.deleteAll()
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(user.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.nick").value(user.nick))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.birth_date").value(parsedDate.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("\$stack[0]").value("Java"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$stack[1]").value("Node"))
            .andDo(MockMvcResultHandlers.print())
        Assertions.assertFalse(userRepository.findAll().isEmpty())
    }

    @Test
    fun `test create user validation error empty name`() {

        userRepository.deleteAll()
        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = User(name = "", nick = "Wil", birth_date =parsedDate, stack = listOf("Java", "Node"))
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
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[name] não pode estar em branco!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create user validation error unique name`() {

        userRepository.deleteAll()
        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        userRepository.save(User(name = "William", nick = "Wil", birth_date = parsedDate, stack = listOf("Java", "Node")))
        var user = User(name = "William", nick = "Wil", birth_date = parsedDate, stack = listOf("Java", "Node"))
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
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[name] deve ser único!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create user validation error character limit for name`() {

        userRepository.deleteAll()
        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = User(
            name = "William Junior Almeida Araujo Mendonça Gonçalves Silva Barmosa Ferreira Nogueira Santos Cunha da Penha Almeida Araujo Mendonça Gonçalves Silva Barmosa Ferreira Nogueira Santos Cunha da Penha Almeida Araujo Mendonça Gonçalves Silva Barmosa Ferreira Nogueira Santos Cunha da Penha ",
            nick = "Wil",
            birth_date = parsedDate,
            stack = listOf("Java", "Node")
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
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[name] deve ter no máximo 255 caracteres!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create user validation error character limit for surname`() {

        userRepository.deleteAll()
        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = User(name = "William", nick = "William Junior Almeida", birth_date = parsedDate, stack = listOf("Java", "Node"))
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
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[nick] deve ter no máximo 32 caracteres!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test update user`() {

        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = userRepository
            .save(User(name = "William", nick = "Wil", birth_date = parsedDate, stack = listOf("Java", "Node")))
            .copy(name = "Updated")
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        var json = objectMapper.writeValueAsString(user)
        mockMvc.perform(MockMvcRequestBuilders.put("/users/${user.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(user.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.nick").value(user.nick))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.birth_date").value(parsedDate.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.stack[0]").value("Java"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.stack[1]").value("Node"))
            .andDo(MockMvcResultHandlers.print())
        var findById = userRepository.findById(user.id!!)
        Assertions.assertTrue(findById.isPresent)
        Assertions.assertEquals(user.name, findById.get().name)
    }

    @Test
    fun `test delete user`() {

        val parsedDate = LocalDate.parse("1980-12-10", DateTimeFormatter.ISO_DATE)
        var user = userRepository
            .save(User(name = "William", nick = "Wil", birth_date = parsedDate, stack = listOf("Java", "Node")))
            .copy(name = "Updated")
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        var json = objectMapper.writeValueAsString(user)
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/${user.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
        var findById = userRepository.findById(user.id!!)
        Assertions.assertFalse(findById.isPresent)
    }

}