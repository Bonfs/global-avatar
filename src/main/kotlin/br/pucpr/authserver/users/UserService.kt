package br.pucpr.authserver.users

import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.exceptions.BadRequestException
import br.pucpr.authserver.roles.RoleRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    val repository: UserRepository,
    val roleRepository: RoleRepository
) {
    fun insert(user: User): User {
        if (repository.findByEmail(user.email) != null) {
            throw BadRequestException("User already exists")
        }
        val user = repository.save(user)
        log.info("Inserted new user {}", user.id)
        return user
    }

    fun findAll(dir: SortDir = SortDir.ASC) = when (dir) {
        SortDir.ASC -> repository.findAll(Sort.by("name").ascending())
        SortDir.DESC -> repository.findAll(Sort.by("name").descending())
    }

    fun findByIdOrNull(id: Long) = repository.findByIdOrNull(id)
    fun findById(id: Long) = repository.findByIdOrNull(id) ?: throw NotFoundException(id)

    fun delete(id: Long) {
        val user = findById(id)
        if (user.isAdmin() && repository.findByRole("ADMIN").size == 1) {
            throw BadRequestException("Cannot delete the last admin")
        }
        repository.delete(user)
        log.info("User {} deleted", user.id)
    }

    fun findByRole(role: String) = repository.findByRole(role.uppercase())

    fun addRole(id: Long, roleName: String): Boolean {
        val upperRole = roleName.uppercase()
        val user = findById(id)
        val role = roleRepository.findByName(upperRole) ?:
            throw BadRequestException("Role $upperRole not found")

        user.roles.add(role)
        repository.save(user)
        log.info("Added role {} to user {}", upperRole, user.id)
        return true
    }

    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }
}