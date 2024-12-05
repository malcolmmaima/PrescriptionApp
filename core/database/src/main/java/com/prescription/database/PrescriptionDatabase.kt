package com.prescription.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.prescription.database.dao.DrugDao
import com.prescription.database.dao.LabDao
import com.prescription.database.dao.MedicationDao
import com.prescription.database.dao.ProblemDao
import com.prescription.database.dao.UserCredentialDao
import com.prescription.database.entities.DrugEntity
import com.prescription.database.entities.LabEntity
import com.prescription.database.entities.MedicationEntity
import com.prescription.database.entities.ProblemEntity
import com.prescription.database.entities.UserCredentialEntity

@Database(
    entities = [ProblemEntity::class, MedicationEntity::class, DrugEntity::class, LabEntity::class, UserCredentialEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PrescriptionDatabase : RoomDatabase() {
    abstract fun problemDao(): ProblemDao
    abstract fun medicationDao(): MedicationDao
    abstract fun drugDao(): DrugDao
    abstract fun labDao(): LabDao
    abstract fun userCredentialDao(): UserCredentialDao
}
