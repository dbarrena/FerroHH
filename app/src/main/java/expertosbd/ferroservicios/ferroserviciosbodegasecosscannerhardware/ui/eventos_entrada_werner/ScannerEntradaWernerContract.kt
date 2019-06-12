package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada_werner

import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.*
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.base.BaseContract

class ScannerEntradaWernerContract {

    interface View : BaseContract.View {
        fun showProgress(show: Boolean)
        fun onPostSuccessful(message: String)
        fun onFetchDetalleEventosDataSuccess(items: MutableList<DetalleEventoWerner>)
        fun onFetchDetalleEventosEstatusDataSuccess(items: MutableList<EstatusDetalleEvento>)
        fun onFetchUbicacionesDataSuccess(items: MutableList<Ubicacion>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun postEvento(evento: NuevoEventoWerner)
        fun updateEvento(evento: UpdateEvento)
        fun fetchDetalleEventosData(eventoBodegaID: Int)
        fun fetchDetalleEventosEstatusData()
        fun fetchUbicacionesData()
    }

}