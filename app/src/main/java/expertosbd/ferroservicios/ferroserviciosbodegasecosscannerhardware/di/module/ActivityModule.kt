package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module

import android.app.Activity
import dagger.Module
import dagger.Provides
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.login.LoginContract
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.login.LoginPresenter
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.main.MainContract
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.main.MainPresenter
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.api.ApiServiceInterface
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada.scanner_entrada.ScannerEntradaContract
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada.scanner_entrada.ScannerEntradaPresenter
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada_werner.ScannerEntradaWernerContract
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada_werner.ScannerEntradaWernerPresenter
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida.scanner_salida.ScannerSalidaContract
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida.scanner_salida.ScannerSalidaPresenter

@Module
class ActivityModule(private var activity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun provideLoginPresenter(): LoginContract.Presenter {
        return LoginPresenter()
    }

    @Provides
    fun provideMainPresenter(): MainContract.Presenter {
        return MainPresenter()
    }

    @Provides
    fun provideScannerEntradaPresenter(): ScannerEntradaContract.Presenter {
        return ScannerEntradaPresenter()
    }

    @Provides
    fun provideScannerEntradaWernerPresenter(): ScannerEntradaWernerContract.Presenter {
        return ScannerEntradaWernerPresenter()
    }
    @Provides
    fun provideScannerSalidaPresenter(): ScannerSalidaContract.Presenter {
        return ScannerSalidaPresenter()
    }

    @Provides
    fun provideApiService(): ApiServiceInterface {
        return ApiServiceInterface.create()
    }
}