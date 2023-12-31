package com.example.db

import java.io.Closeable
import java.sql.SQLException
import javax.persistence.EntityManager
import javax.persistence.EntityTransaction
import javax.persistence.Persistence

object DBManager : Closeable {
    // Creamos las EntityManagerFactory para manejar las entidades y transacciones
    private var entityManagerFactory = Persistence.createEntityManagerFactory("default")
    lateinit var manager: EntityManager
    private lateinit var transaction: EntityTransaction

    init {
        // Creamos la EntityManagerFactory
        // Creamos la EntityManager
        manager = entityManagerFactory.createEntityManager()
        // Creamos la transacción
        transaction = manager.transaction
    }

    fun open() {
        manager = entityManagerFactory.createEntityManager()
        transaction = manager.transaction
    }

    override fun close() {
        manager.close()
    }

    fun query(operations: () -> Unit) {
        open()
        try {
            operations()
        } catch (e: SQLException) {
        } finally {
            close()
        }
    }

    /**
     * Realiza unas operaciones en una transacción
     * @param operations operaciones a realizar
     * @throws SQLException si no se ha podido realizar la transacción
     */
    fun transaction(operations: () -> Unit) {
        open()
        try {
            transaction.begin()
            // Aquí las operaciones
            operations()
            transaction.commit()
        } catch (e: SQLException) {
            transaction.rollback()
            throw SQLException(e)
        } finally {
            close()
        }
    }
}