package com.ucne.myapplication.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.ucne.aporteskotlin.data.local.entitites.AporteEntity
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun AporteListScreen(
    aportes: List<AporteEntity>,
    onVerAporte: (AporteEntity) -> Unit

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                ) {
                    Text(
                        text = "ID",
                        modifier = Modifier
                            .weight(0.10f)
                    )

                    Text(
                        text = "Persona",
                        modifier = Modifier
                            .weight(0.40f)
                    )
                    Text(
                        text = "Observa-\nción",
                        modifier = Modifier
                            .weight(0.30f)
                    )
                    Text(
                        text = "Monto",
                        modifier = Modifier
                            .weight(0.20f)
                    )
                    Text(
                        text = "Fecha",
                        modifier = Modifier
                            .weight(0.40f)
                    )
                }
            }
            items(aportes) { aporte ->
                val fecha =  aporte.fecha.split(" ").take(4).joinToString(" ").split(":").take(2).joinToString(":")
                val observacion = cortarCantidadCaracteres(aporte.observacion,25)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onVerAporte(aporte) }
                        .padding(16.dp)
                ) {
                    Text(text = aporte.aporteId.toString(), modifier = Modifier.weight(0.10f))
                    Text(text = aporte.persona, modifier = Modifier.weight(0.40f))
                    Text(text = observacion, modifier = Modifier.weight(0.50f))
                    Text(text = aporte.monto.toString(), modifier = Modifier.weight(0.30f))
                    Text(text = fecha, modifier = Modifier.weight(0.40f))
                }
            }
        }
    }
}

fun cortarCantidadCaracteres(texto: String, cantidad: Int): String {
    if (texto.length <= cantidad) {
        return texto
    } else {
        return texto.substring(0, cantidad)
    }
}