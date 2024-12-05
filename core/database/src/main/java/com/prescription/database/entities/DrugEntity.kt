package com.prescription.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "drugs",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DrugEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medicationId: Long,
    val name: String,
    val dose: String,
    val strength: String
)
