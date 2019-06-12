package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada_werner

import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.api.ApiServiceInterface
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ScannerEntradaWernerPresenter : ScannerEntradaWernerContract.Presenter {

    private val api: ApiServiceInterface = ApiServiceInterface.create()
    private val subscriptions = CompositeDisposable()
    private val observables = arrayListOf<Observable<Any>>()
    private lateinit var view: ScannerEntradaWernerContract.View

    override fun postEvento(evento: NuevoEventoWerner) {
        val subscribe = api
            .nuevoEventoWerner(evento)
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
            .detalleEventosWerner(eventoBodegaID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item: ApexApiResponse<DetalleEventoWerner> ->
                    view.onFetchDetalleEventosDataSuccess(item.items as MutableList<DetalleEventoWerner>)
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

    override fun fetchUbicacionesData() {
        val subscribe = api
            .ubicaciones()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item: ApexApiResponse<Ubicacion> ->
                    view.onFetchUbicacionesDataSuccess(item.items as MutableList<Ubicacion>)
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

    override fun attach(view: ScannerEntradaWernerContract.View) {
        this.view = view
    }

}