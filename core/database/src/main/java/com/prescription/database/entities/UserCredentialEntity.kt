package com.prescription.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_credentials")
data class UserCredentialEntity(
    @PrimaryKey val username: String,
    val hashedPassword: String
)
