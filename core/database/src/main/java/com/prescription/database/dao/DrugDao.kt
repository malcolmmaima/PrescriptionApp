package com.prescription.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prescription.database.entities.DrugEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrugDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrug(drug: DrugEntity): Long

    @Query("SELECT * FROM drugs WHERE medicationId = :medicationId")
    fun getDrugsForMedication(medicationId: Int): Flow<List<DrugEntity>>

    @Query("DELETE FROM drugs")
    suspend fun deleteAllDrugs()

    @Query(
        """
        SELECT drugs.* FROM drugs
        INNER JOIN medications ON drugs.medicationId = medications.id
        WHERE medications.problemId = :problemId
    """
    )
    fun getDrugsByProblemId(problemId: Int): Flow<List<DrugEntity>>
}
