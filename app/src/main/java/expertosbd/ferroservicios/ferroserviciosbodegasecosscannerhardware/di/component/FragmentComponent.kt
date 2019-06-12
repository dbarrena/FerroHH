package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.component

import dagger.Component
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module.FragmentModule
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada.EventosEntradaFragment
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida.EventosSalidaFragment
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.login.LoginActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.main.MainActivity

@Component(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

    fun inject(eventosEntradaFragment: EventosEntradaFragment)

    fun inject(eventosSalidaFragment: EventosSalidaFragment)
}
