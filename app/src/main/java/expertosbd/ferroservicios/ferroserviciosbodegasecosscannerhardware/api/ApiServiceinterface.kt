package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.api

import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.*
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.utils.BASE_URL
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiServiceInterface {

    @GET("version/hardware")
    fun version(): Single<ApexApiResponse<Version>>

    @GET("login/{user}")
    fun login(@Path("user") user: String): Observable<ApexApiResponse<Login>>

    @GET("eventos")
    fun eventos(): Single<ApexApiResponse<Evento>>

    @GET("eventos/salida")
    fun eventosSalida(): Single<ApexApiResponse<Evento>>

    @GET("eventos/detalles/{id_evento_bodega}")
    fun detalleEventos(@Path("id_evento_bodega")
                       eventoBodegaID: Int): Single<ApexApiResponse<DetalleEvento>>

    @GET("eventos/werner/detalles/{id_evento_bodega}")
    fun detalleEventosWerner(@Path("id_evento_bodega")
                       eventoBodegaID: Int): Single<ApexApiResponse<DetalleEventoWerner>>

    @GET("eventos/detalles/estatus/lov")
    fun detalleEventosEstatus(): Single<ApexApiResponse<EstatusDetalleEvento>>

    @GET("eventos/salida/detalles/{id_evento_bodega}")
    fun detalleEventosSalida(@Path("id_evento_bodega")
                             eventoBodegaID: Int): Single<ApexApiResponse<DetalleEventoSalida>>

    @GET("ubicaciones")
    fun ubicaciones(): Single<ApexApiResponse<Ubicacion>>

    @POST("iniciomaniobra")
    fun inicioManiobra(@Body inicioManiobra: InicioManiobra): Single<PostSuccesfulResponse>

    @POST("finmaniobra")
    fun finManiobra(@Body finManiobra: FinManiobra): Single<PostSuccesfulResponse>

    @POST("eventos/nuevo")
    fun nuevoEvento(@Body nuevoEvento: NuevoEvento): Single<PostSuccesfulResponse>

    @POST("eventos/werner/nuevo")
    fun nuevoEventoWerner(@Body nuevoEventoWerner: NuevoEventoWerner): Single<PostSuccesfulResponse>

    @PUT("eventos/update")
    fun updateEvento(@Body updateEvento: UpdateEvento): Single<PostSuccesfulResponse>

    @PUT("eventos/salida/detalles/update")
    fun updateEventoSalida(@Body updateEventoSalida: UpdateEventoSalida): Single<PostSuccesfulResponse>


    companion object Factory {
        fun create(): ApiServiceInterface {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .build()

            return retrofit.create(ApiServiceInterface::class.java)
        }
    }
}