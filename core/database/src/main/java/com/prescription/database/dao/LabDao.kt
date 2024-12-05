package com.prescription.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prescription.database.entities.LabEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLab(lab: LabEntity): Long

    @Query("SELECT * FROM labs WHERE problemId = :problemId")
    fun getLabsForProblem(problemId: Int): Flow<List<LabEntity>>

    @Query("DELETE FROM labs")
    suspend fun deleteAllLabs()
}
