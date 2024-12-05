package com.prescription.database.di

import android.content.Context
import androidx.room.Room
import com.prescription.database.PrescriptionDatabase
import com.prescription.database.dao.DrugDao
import com.prescription.database.dao.LabDao
import com.prescription.database.dao.MedicationDao
import com.prescription.database.dao.ProblemDao
import com.prescription.database.dao.UserCredentialDao
import com.prescription.database.migrations.PrescriptionDatabaseMigrations
import com.prescription.database.repository.PrescriptionRepository
import com.prescription.database.repository.UserCredentialRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Singleton
    @Provides
    fun providePrescriptionDatabase(
        @ApplicationContext context: Context
    ): PrescriptionDatabase = Room.databaseBuilder(
        context = context,
        name = "prescriptiondatabase.db",
        klass = PrescriptionDatabase::class.java
    ).addMigrations(PrescriptionDatabaseMigrations.prescription_migrations_1_2)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideProblemDao(
        prescriptionDatabase: PrescriptionDatabase
    ): ProblemDao = prescriptionDatabase.problemDao()

    @Provides
    @Singleton
    fun provideMedicationDao(
        prescriptionDatabase: PrescriptionDatabase
    ): MedicationDao = prescriptionDatabase.medicationDao()

    @Provides
    @Singleton
    fun provideDrugDao(
        prescriptionDatabase: PrescriptionDatabase
    ): DrugDao = prescriptionDatabase.drugDao()

    @Provides
    @Singleton
    fun provideLabDao(
        prescriptionDatabase: PrescriptionDatabase
    ): LabDao = prescriptionDatabase.labDao()

    @Provides
    @Singleton
    fun providePrescriptionRepository(
        problemDao: ProblemDao,
        medicationDao: MedicationDao,
        drugDao: DrugDao,
        labDao: LabDao
    ): PrescriptionRepository = PrescriptionRepository(
        problemDao = problemDao,
        medicationDao = medicationDao,
        drugDao = drugDao,
        labDao = labDao
    )

    @Provides
    fun provideUserCredentialDao(prescriptionDatabase: PrescriptionDatabase): UserCredentialDao {
        return prescriptionDatabase.userCredentialDao()
    }

    @Provides
    fun provideUserCredentialRepository(dao: UserCredentialDao): UserCredentialRepository {
        return UserCredentialRepository(dao)
    }
}
