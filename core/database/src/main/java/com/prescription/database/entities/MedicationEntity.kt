package com.prescription.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "medications",
    foreignKeys = [
        ForeignKey(
            entity = ProblemEntity::class,
            parentColumns = ["id"],
            childColumns = ["problemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val problemId: Long,
    val medicationClass: String
)
