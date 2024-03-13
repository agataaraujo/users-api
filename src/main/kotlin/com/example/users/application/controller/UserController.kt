package com.example.users.application.controller

import com.example.users.application.model.User
import com.example.users.application.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping( "/users" )
class UserController(private val service: UserService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody user: User) = service.create(user)

    @GetMapping
    fun getAll(): List<User> = service.getAll()

    @GetMapping( "/{id}" )
    fun getById(@PathVariable id: Long) : ResponseEntity<User> =
        service.getById(id).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @PutMapping( "/{id}" )
    fun update(@PathVariable id: Long, @RequestBody user: User) : ResponseEntity<User> =
        service.update(id, user).map{
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @DeleteMapping( "/{id}" )
    fun delete(@PathVariable id: Long) : ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity<Void>(HttpStatus.OK)
    }

}