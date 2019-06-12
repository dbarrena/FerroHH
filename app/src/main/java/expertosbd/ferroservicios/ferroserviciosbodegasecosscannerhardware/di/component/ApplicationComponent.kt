package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.component

import dagger.Component
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.BaseApp
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module.ApplicationModule

@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun inject(application: BaseApp)
}