package com.prescription.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prescription.database.entities.ProblemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProblem(problem: ProblemEntity): Long

    @Query("SELECT * FROM problems")
    fun getAllProblems(): Flow<List<ProblemEntity>>

    @Query("DELETE FROM problems")
    suspend fun deleteAllProblems()

    @Query("SELECT * FROM problems WHERE id = :problemId")
    fun getProblemById(problemId: String): Flow<ProblemEntity?>
}
