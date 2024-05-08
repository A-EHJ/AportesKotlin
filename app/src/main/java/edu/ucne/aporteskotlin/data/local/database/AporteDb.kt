package edu.ucne.aporteskotlin.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.aporteskotlin.data.local.dao.AporteDao
import edu.ucne.aporteskotlin.data.local.entitites.AporteEntity

//data/local/database
@Database(
    entities = [
        AporteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AporteDb : RoomDatabase() {
    abstract fun aporteDao(): AporteDao
}