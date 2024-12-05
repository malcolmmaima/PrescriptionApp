package com.prescription.database.repository

import com.prescription.database.dao.DrugDao
import com.prescription.database.dao.LabDao
import com.prescription.database.dao.MedicationDao
import com.prescription.database.dao.ProblemDao
import com.prescription.database.entities.DrugEntity
import com.prescription.database.entities.LabEntity
import com.prescription.database.entities.MedicationEntity
import com.prescription.database.entities.ProblemEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class PrescriptionRepository @Inject constructor(
    private val problemDao: ProblemDao,
    private val medicationDao: MedicationDao,
    private val drugDao: DrugDao,
    private val labDao: LabDao
) {
    // problem-related functions
    suspend fun insertProblem(problem: ProblemEntity) = problemDao.insertProblem(problem)

    fun getAllProblems(): Flow<List<ProblemEntity>> = problemDao.getAllProblems()

    fun getProblemById(problemId: String): Flow<ProblemEntity?> = problemDao.getProblemById(
        problemId
    )

    // medication-related functions
    suspend fun insertMedication(medication: MedicationEntity) = medicationDao.insertMedication(
        medication
    )

    fun getMedicationsForProblem(problemId: Int): Flow<List<MedicationEntity>> =
        medicationDao.getMedicationsForProblem(problemId)

    // drug-related functions
    suspend fun insertDrug(drug: DrugEntity) = drugDao.insertDrug(drug)

    fun getDrugsByProblemId(problemId: Int): Flow<List<DrugEntity>> =
        drugDao.getDrugsByProblemId(problemId)

    fun getDrugsForMedication(medicationId: Int): Flow<List<DrugEntity>> =
        drugDao.getDrugsForMedication(medicationId)

    // lab-related functions
    suspend fun insertLab(lab: LabEntity) = labDao.insertLab(lab)

    fun getLabsForProblem(problemId: Int): Flow<List<LabEntity>> = labDao.getLabsForProblem(
        problemId
    )

    // purge all data
    suspend fun purgeAllMedicalData() {
        problemDao.deleteAllProblems()
        medicationDao.deleteAllMedications()
        drugDao.deleteAllDrugs()
        labDao.deleteAllLabs()
    }
}
