package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.login

import android.util.Log
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.BuildConfig
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.ApexApiResponse
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.Login
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.Version
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.api.ApiServiceInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginPresenter : LoginContract.Presenter {

    private val api: ApiServiceInterface = ApiServiceInterface.create()
    private val subscriptions = CompositeDisposable()
    private lateinit var view: LoginContract.View

    override fun checkLogin(user: String, password: String) {
        val subscribe = api
            .login(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item: ApexApiResponse<Login> ->
                    checkPassword(item.items[0], password)
                },
                { error ->
                    view.showErrorMessage("Usuario no válido")
                    Log.e("LOGIN ERROR", error.localizedMessage)
                }
            )

        subscriptions.add(subscribe)
    }

    override fun checkCurrentVersion() {
        val subscribe = api
            .version()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item: ApexApiResponse<Version> ->
                    checkVersion(item.items[0])
                },
                { error ->
                    view.showErrorMessage(error.localizedMessage)
                    Log.e("VERSION ERROR", error.localizedMessage)
                }
            )
        subscriptions.add(subscribe)
    }

    private fun checkPassword(loginData: Login, password: String) {
        if (loginData.pass_android == password) {
            view.loginResult(true, loginData)
        } else {
            view.loginResult(false, null)
            Log.d("LOGIN USER", loginData.toString())
        }
    }

    private fun checkVersion(version: Version) {
        val localVersion = BuildConfig.VERSION_NAME
        val currentVersion = version.appVersion
        view.showVersion(localVersion, currentVersion)
    }

    override fun subscribe() {
    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: LoginContract.View) {
        this.view = view
    }
}