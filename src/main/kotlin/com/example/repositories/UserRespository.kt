package com.example.repositories

import com.example.db.DBManager
import com.example.db.DBManager.manager
import com.example.models.User
import com.example.utils.PasswordEncrypter
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class UserRespository : IRepository<User, UUID>{
    override suspend fun findAll(): List<User> {
        lateinit var res : List<User>

        DBManager.query {
            val query = DBManager.manager.createQuery("SELECT u FROM User u")
            res = query.resultList as List<User>
        }
        println(res)
    return res
    }

    override suspend fun deleteById(id: UUID): Boolean {
        DBManager.query {
            val query = manager.createQuery("SELECT u FROM User u WHERE u.uudi = :uuid")
            query.setParameter("uuid", id.toString())

            val user = query.singleResult as User?

            user?.let {
                manager.transaction.begin()
                manager.remove(user)
                manager.transaction.commit()
            }
        }
        return true
    }

    override suspend fun update(t: User): User {
        val user = manager.find(User::class.java, t.username)
        user?.let {
            manager.transaction.begin()
            user.username = t.username
            user.password = t.password
            manager.transaction.commit()
        }
        return t
    }

    override suspend fun save(t: User): User {
        t.password = PasswordEncrypter.encrypt(t.password)
        DBManager.query {
            manager.transaction.begin()
            manager.persist(t)
            manager.transaction.commit()
        }
        return t
    }

    override suspend fun findById(id: UUID): User? {
        var res : User? = null

        DBManager.query {
            val query = manager.createQuery("SELECT u FROM User u WHERE u.uudi = :uuid")
            query.setParameter("uuid", id.toString())
            res = query.singleResult as User

        }
        return res
    }

    fun findByUsername(userName: String) : User? {
        println("repositorio")
         var res : User? = null
        DBManager.query {
            res = DBManager.manager.find(User::class.java, userName)
        }
        return res
    }

}