package com.prescription.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prescription.database.entities.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity): Long

    @Query("SELECT * FROM medications WHERE problemId = :problemId")
    fun getMedicationsForProblem(problemId: Int): Flow<List<MedicationEntity>>

    @Query("DELETE FROM medications")
    suspend fun deleteAllMedications()
}
