package com.prescription.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal object PrescriptionDatabaseMigrations {
    val prescription_migrations_1_2: Migration =
        object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
}
