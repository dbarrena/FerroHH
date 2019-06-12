package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model

import com.google.gson.annotations.SerializedName

data class DetalleEvento(
        @SerializedName("id_det_evento_bod")
        val detalleEventoID: Int,
        @SerializedName("codigo_barras")
        val codigoBarras: String,
        @SerializedName("estatus_det_evento")
        val estatusDetalleEvento: String,
        @SerializedName("datos_adicionales")
        val datosAdicionales: String,
        @SerializedName("peso_neto")
        val pesoNeto: Int
)