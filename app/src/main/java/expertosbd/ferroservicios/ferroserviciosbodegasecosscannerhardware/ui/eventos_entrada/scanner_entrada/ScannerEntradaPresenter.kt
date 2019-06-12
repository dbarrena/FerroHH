package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada.scanner_entrada

import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.api.ApiServiceInterface
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ScannerEntradaPresenter : ScannerEntradaContract.Presenter {

    private val api: ApiServiceInterface = ApiServiceInterface.create()
    private val subscriptions = CompositeDisposable()
    private val observables = arrayListOf<Observable<Any>>()
    private lateinit var view: ScannerEntradaContract.View

    override fun postEvento(evento: NuevoEvento) {
        val subscribe = api
            .nuevoEvento(evento)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item: PostSuccesfulResponse ->
                    view.onPostSuccessful(item.message)
                },
                { error ->
                    view.showErrorMessage(error.localizedMessage)
                }
            )

        subscriptions.add(subscribe)
    }

    override fun updateEvento(evento: UpdateEvento) {
        val subscribe = api
            .updateEvento(evento)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item: PostSuccesfulResponse ->
                    view.onPostSuccessful(item.message)
                },
                { error ->
                    view.showErrorMessage(error.localizedMessage)
                }
            )

        subscriptions.add(subscribe)
    }

    override fun fetchDetalleEventosData(eventoBodegaID: Int) {
        val subscribe = api
            .detalleEventos(eventoBodegaID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item: ApexApiResponse<DetalleEvento> ->
                    view.onFetchDetalleEventosDataSuccess(item.items as MutableList<DetalleEvento>)
                },
                { error ->
                    view.showErrorMessage(error.localizedMessage)
                }
            )

        subscriptions.add(subscribe)
    }

    override fun fetchDetalleEventosEstatusData() {
        val subscribe = api
            .detalleEventosEstatus()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item: ApexApiResponse<EstatusDetalleEvento> ->
                    view.onFetchDetalleEventosEstatusDataSuccess(item.items as MutableList<EstatusDetalleEvento>)
                },
                { error ->
                    view.showErrorMessage(error.localizedMessage)
                }
            )

        subscriptions.add(subscribe)
    }


    override fun subscribe() {
    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ScannerEntradaContract.View) {
        this.view = view
    }

}