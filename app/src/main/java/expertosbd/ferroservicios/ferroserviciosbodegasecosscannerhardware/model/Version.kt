package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model

import com.google.gson.annotations.SerializedName

data class Version (
        @SerializedName("app_version")
        val appVersion: String
)