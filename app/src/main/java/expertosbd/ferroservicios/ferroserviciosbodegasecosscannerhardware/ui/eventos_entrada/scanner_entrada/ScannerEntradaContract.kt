package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada.scanner_entrada

import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.DetalleEvento
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.EstatusDetalleEvento
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.NuevoEvento
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.UpdateEvento
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.base.BaseContract

class ScannerEntradaContract {

    interface View : BaseContract.View {
        fun showProgress(show: Boolean)
        fun onPostSuccessful(message: String)
        fun onFetchDetalleEventosDataSuccess(items: MutableList<DetalleEvento>)
        fun onFetchDetalleEventosEstatusDataSuccess(items: MutableList<EstatusDetalleEvento>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun postEvento(evento: NuevoEvento)
        fun updateEvento(evento: UpdateEvento)
        fun fetchDetalleEventosData(eventoBodegaID: Int)
        fun fetchDetalleEventosEstatusData()
    }

}