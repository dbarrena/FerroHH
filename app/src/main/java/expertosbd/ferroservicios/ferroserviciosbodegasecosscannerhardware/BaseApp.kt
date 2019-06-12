package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware

import android.app.Application
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.component.ApplicationComponent
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.component.DaggerApplicationComponent
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module.ApplicationModule
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.utils.Prefs
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.utils.Utils

val prefs: Prefs by lazy {
    BaseApp.prefs!!
}

val utils: Utils by lazy {
    BaseApp.utils!!
}

class BaseApp: Application() {

    lateinit var component: ApplicationComponent

    companion object {
        lateinit var instance: BaseApp private set
        var prefs: Prefs? = null
        var utils: Utils? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        prefs = Prefs(applicationContext)
        utils = Utils()
        setup()
        if(BuildConfig.DEBUG) {

        }
    }

    fun setup() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
        component.inject(this)
    }

    fun getApplicationComponent(): ApplicationComponent {
        return component
    }
}