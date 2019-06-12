package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.component

import dagger.Component
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module.ActivityModule
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada.scanner_entrada.ScannerEntradaActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada_werner.ScannerEntradaWernerActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida.scanner_salida.ScannerSalidaActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.login.LoginActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.main.MainActivity

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(loginActivity: LoginActivity)

    fun inject(mainActivity: MainActivity)

    fun inject(scannerEntradaActivity: ScannerEntradaActivity)

    fun inject(scannerSalidaActivity: ScannerSalidaActivity)

    fun inject (scannerEntradaWernerActivity: ScannerEntradaWernerActivity)

}