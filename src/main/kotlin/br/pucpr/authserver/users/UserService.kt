package br.pucpr.authserver.users

import org.springframework.stereotype.Service

@Service
class UserService(val repository: UserRepository) {
    fun insert(user: User) = repository.save(user)
    fun findAll(dir: SortDir) = repository.findAll(dir)
    fun findByIdOrNull(id: Long) = repository.findByIdOrNull(id)
    fun delete(id: Long) = repository.delete(id)

}