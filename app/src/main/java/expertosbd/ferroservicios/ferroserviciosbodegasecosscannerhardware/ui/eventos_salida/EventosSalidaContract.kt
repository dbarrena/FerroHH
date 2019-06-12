package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida

import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.Evento
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.FinManiobra
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.InicioManiobra
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.PostSuccesfulResponse
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.base.BaseContract
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class EventosSalidaContract {

    interface View : BaseContract.View {
        fun showProgress(show: Boolean)
        fun onFetchDataSuccess(items: MutableList<Evento>)
        fun onPostSuccesful(message: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun fetchData()
        fun inicioManiobra(inicioManiobra: InicioManiobra): Single<PostSuccesfulResponse>
        fun finManiobra(finManiobra: FinManiobra): Single<PostSuccesfulResponse>
        fun addSubscription(disposable: Disposable)
    }

}