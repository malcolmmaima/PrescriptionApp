package com.prescription.database.repository

import com.prescription.database.dao.UserCredentialDao
import com.prescription.database.entities.UserCredentialEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserCredentialRepository @Inject constructor(
    private val userCredentialDao: UserCredentialDao
) {
    suspend fun saveCredential(username: String, hashedPassword: String) {
        deleteUsersTable()

        val userCredential = UserCredentialEntity(username, hashedPassword)
        userCredentialDao.insert(userCredential)
    }

    suspend fun getCredentialByUsername(username: String): UserCredentialEntity? {
        return userCredentialDao.getCredentialByUsername(username)
    }

    fun getLoggedInUser(): Flow<UserCredentialEntity?> {
        return userCredentialDao.getLoggedInUser()
    }

    fun deleteUsersTable() {
        userCredentialDao.delete()
    }
}
