package edu.ucne.aporteskotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import com.ucne.myapplication.presentation.AporteListScreen
import edu.ucne.aporteskotlin.data.local.database.AporteDb
import edu.ucne.aporteskotlin.data.local.entitites.AporteEntity
import edu.ucne.aporteskotlin.ui.theme.AportesKotlinTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : ComponentActivity() {
    private lateinit var aporteDb: AporteDb
    val regex = Regex("[0-9]*\\.?[0-9]*")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        aporteDb = Room.databaseBuilder(
            this,
            AporteDb::class.java,
            "Aportes.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        enableEdgeToEdge()
        setContent {
            AportesKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(8.dp)
                    ){
                        Text(text = "Aportes")
                        val aportes: List<AporteEntity> by getAportes().collectAsStateWithLifecycle(
                            initialValue = emptyList()
                        )
                        var aporteId by remember { mutableStateOf("") }
                        var persona by remember { mutableStateOf("") }
                        var observacion by remember { mutableStateOf("") }
                        var monto by remember { mutableStateOf(0.0 ) }


                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {

                                OutlinedTextField(
                                    label = { Text(text = "Persona") },
                                    value = persona,
                                    onValueChange = { persona = it },
                                    modifier = Modifier.fillMaxWidth().size(80.dp).padding(8.dp),
                                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                                )

                                OutlinedTextField(
                                    label = { Text(text = "Monto") },
                                    value = monto.toString(),
                                    onValueChange = {if (it.matches(regex)) {
                                        monto = it.toDoubleOrNull() ?: 0.0
                                    }},
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),

                                    modifier = Modifier.fillMaxWidth().size(80.dp).padding(8.dp),
                                )

                                OutlinedTextField(
                                    label = { Text(text = "Observacion") },
                                    value = observacion,
                                    onValueChange = { observacion = it },
                                    modifier = Modifier.fillMaxWidth().size(120.dp).padding(8.dp),
                                )

                                Spacer(modifier = Modifier.padding(2.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            aporteId = ""
                                            persona = ""
                                            monto = 0.0
                                            observacion = ""
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "new button"
                                        )
                                        Text(text = "Nuevo")
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            saveAporte(
                                                AporteEntity(
                                                    aporteId = aporteId.toIntOrNull(),
                                                    fecha = Date().toString(),
                                                    persona = persona,
                                                    monto = monto,
                                                    observacion = observacion
                                                )
                                            )
                                            aporteId = ""
                                            persona = ""
                                            monto = 0.0
                                            observacion = ""
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "save button"
                                        )
                                        if (aporteId.isNotEmpty()) {
                                            Text(text = "Actualizar")
                                        } else {
                                        Text(text = "Guardar")
                                            }
                                    }


                                    if (aporteId.isNotEmpty()) {
                                    OutlinedButton(
                                        onClick = {
                                            deleteAporte(
                                                AporteEntity(
                                                    aporteId = aporteId.toIntOrNull(),
                                                    fecha = Date().toString(),
                                                    persona = persona,
                                                    monto = monto,
                                                    observacion = observacion
                                                )
                                            )
                                            aporteId = ""
                                            persona = ""
                                            monto = 0.0
                                            observacion = ""
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "delete button"
                                        )
                                        Text(text = "Borrar")
                                    }
                                        }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(2.dp))

                        AporteListScreen(
                            aportes = aportes,
                            onVerAporte = { aporteSeleccionado ->
                                aporteId = aporteSeleccionado.aporteId.toString()
                                observacion = aporteSeleccionado.observacion
                                persona = aporteSeleccionado.persona
                                monto = aporteSeleccionado.monto
                            })
                    }
                }
            }
        }
    }
    fun saveAporte(aporte: AporteEntity) {
        GlobalScope.launch {
            aporteDb.aporteDao().save(aporte)
        }
    }

    fun getAportes(): Flow<List<AporteEntity>> {
        return aporteDb.aporteDao().getAll()
    }

    fun deleteAporte(aporte: AporteEntity) {
        GlobalScope.launch {
            aporteDb.aporteDao().delete(aporte)
        }
    }
}
