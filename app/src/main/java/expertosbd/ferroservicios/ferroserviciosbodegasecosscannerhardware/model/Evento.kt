package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Evento (
        @SerializedName("id_evento_bodega")
        val eventoBodegaID: Int,
        @SerializedName("fecha_evento")
        val fechaEvento: String,
        @SerializedName("nombre_cliente")
        val nombreCliente: String,
        @SerializedName("num_cont")
        val contenedor: String,
        val pallets: Int,
        val producto: String,
        val placas: String
): Serializable