package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida.scanner_salida

import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.api.ApiServiceInterface
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.ApexApiResponse
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.DetalleEventoSalida
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.PostSuccesfulResponse
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.UpdateEventoSalida
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ScannerSalidaPresenter : ScannerSalidaContract.Presenter {

    private val api: ApiServiceInterface = ApiServiceInterface.create()
    private val subscriptions = CompositeDisposable()
    private val observables = arrayListOf<Observable<Any>>()
    private lateinit var view: ScannerSalidaContract.View

    override fun fetchEventosSalida(eventoBodegaID: Int) {
        val subscribe = api
            .detalleEventosSalida(eventoBodegaID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item: ApexApiResponse<DetalleEventoSalida> ->
                    view.onFetchEventosSalidaSuccess(item.items as MutableList<DetalleEventoSalida>)
                },
                { error ->
                    view.showErrorMessage(error.localizedMessage)
                }
            )

        subscriptions.add(subscribe)
    }

    override fun updateEvento(evento: UpdateEventoSalida) {
        val subscribe = api
            .updateEventoSalida(evento)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item: PostSuccesfulResponse ->
                    view.onPostSuccessful(item.message, evento.id_det_evento_bod)
                },
                { error ->
                    view.showErrorMessage(error.localizedMessage)
                }
            )

        subscriptions.add(subscribe)
    }

    override fun subscribe() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ScannerSalidaContract.View) {
        this.view = view
    }

}