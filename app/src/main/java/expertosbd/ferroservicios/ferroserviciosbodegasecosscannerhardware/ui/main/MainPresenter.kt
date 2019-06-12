package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.main

import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.*
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.api.ApiServiceInterface
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainPresenter : MainContract.Presenter {

    private val api: ApiServiceInterface = ApiServiceInterface.create()
    private val subscriptions = CompositeDisposable()
    private lateinit var view: MainContract.View

    override fun fetchData() {
        val subscribe = api
            .eventos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item: ApexApiResponse<Evento> ->
                    if (item.items.isEmpty()) {
                        view.showErrorMessage("No hay registros abiertos")
                    } else {
                        view.onFetchDataSuccess(item.items as MutableList<Evento>)
                    }
                },
                { error ->
                    view.showErrorMessage(error.localizedMessage)
                }
            )

        subscriptions.add(subscribe)
    }

    override fun inicioManiobra(inicioManiobra: InicioManiobra): Single<PostSuccesfulResponse> {
        return api
            .inicioManiobra(inicioManiobra)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun finManiobra(finManiobra: FinManiobra): Single<PostSuccesfulResponse> {
        return api
            .finManiobra(finManiobra)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun subscribe() {
        fetchData()
    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun addSubscription(disposable: Disposable){
        subscriptions.add(disposable)
    }

    override fun attach(view: MainContract.View) {
        this.view = view
    }

}