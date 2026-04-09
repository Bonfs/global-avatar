package br.pucpr.authserver.users

import org.springframework.stereotype.Component

@Component
class UserRepository {
    private val users = mutableMapOf<Long, User>()

    fun save(user: User): User {
        if (user.id == null) {
            user.id = (users.keys.maxOrNull() ?: 0) + 1
        }
        users[user.id!!] = user
        return user
    }

    fun findAll() = users.values.sortedBy { it.name }

    fun findByIdOrNull(id: Long) = users[id]
}