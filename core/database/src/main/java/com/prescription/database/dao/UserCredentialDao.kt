package com.prescription.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prescription.database.entities.UserCredentialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCredentialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userCredential: UserCredentialEntity)

    @Query("SELECT * FROM user_credentials WHERE username = :username LIMIT 1")
    suspend fun getCredentialByUsername(username: String): UserCredentialEntity?

    @Query("SELECT * FROM user_credentials LIMIT 1")
    fun getLoggedInUser(): Flow<UserCredentialEntity?>

    @Query("DELETE FROM user_credentials")
    fun delete()
}
