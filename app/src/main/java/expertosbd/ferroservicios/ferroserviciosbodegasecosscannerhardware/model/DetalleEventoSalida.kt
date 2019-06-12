package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model

import com.google.gson.annotations.SerializedName

data class DetalleEventoSalida(
        @SerializedName("id_det_evento_bod")
        val detalleEventoID: Int,
        @SerializedName("codigo_barras")
        val codigoBarras: String,
        @SerializedName("id_estatus_det_evento")
        val estatusDetalleEventoID: Int
)