package com.prescription.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "labs",
    foreignKeys = [
        ForeignKey(
            entity = ProblemEntity::class,
            parentColumns = ["id"],
            childColumns = ["problemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LabEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val problemId: Int,
    val missingField: String
)
