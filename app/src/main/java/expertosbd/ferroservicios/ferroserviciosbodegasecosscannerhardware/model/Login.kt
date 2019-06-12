package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model

import com.google.gson.annotations.SerializedName

data class Login(
        val pass_android: String,
        val nombre: String,
        @SerializedName("apellido_paterno")
        val apellidoPaterno: String
)