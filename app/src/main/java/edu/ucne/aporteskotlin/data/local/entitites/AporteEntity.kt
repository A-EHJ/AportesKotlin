package edu.ucne.aporteskotlin.data.local.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

//data/local/entities
@Entity(tableName = "Aportes")
data class AporteEntity(
    @PrimaryKey
    val aporteId: Int? = null,
    var fecha: String = "",
    var observacion: String = "",
    var persona: String = "",
    var monto: Double = 0.0
)