package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida.scanner_salida

import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.DetalleEventoSalida
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.UpdateEventoSalida
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.base.BaseContract

class ScannerSalidaContract {
    interface View : BaseContract.View {
        fun showProgress(show: Boolean)
        fun onPostSuccessful(message: String, eventoID: Int)
        fun onFetchEventosSalidaSuccess(items: MutableList<DetalleEventoSalida>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun updateEvento(evento: UpdateEventoSalida)
        fun fetchEventosSalida(eventoBodegaID: Int)
    }
}