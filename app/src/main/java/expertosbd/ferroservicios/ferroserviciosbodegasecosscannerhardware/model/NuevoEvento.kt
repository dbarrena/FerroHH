package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model

data class NuevoEvento(
        val barcode: String,
        val peso_neto:String,
        val datos_adicionales: String,
        val id_evento_bodega: Int,
        val id_estatus_det_evento: Int
)