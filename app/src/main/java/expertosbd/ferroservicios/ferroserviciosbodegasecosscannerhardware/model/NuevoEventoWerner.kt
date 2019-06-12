package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model

data class NuevoEventoWerner(
    val barcode: String,
    val ubicacion:String,
    val datos_adicionales: String,
    val id_evento_bodega: Int,
    val id_estatus_det_evento: Int
)