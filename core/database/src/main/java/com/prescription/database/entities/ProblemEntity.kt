package com.prescription.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "problems")
data class ProblemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
