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

    fun findAll(dir: SortDir) =
        if (dir == SortDir.ASC) users.values.sortedBy { it.name }
        else users.values.sortedByDescending { it.name }

    fun findByIdOrNull(id: Long) = users[id]

    fun delete(id: Long) = users.remove(id) != null
}