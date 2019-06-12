package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module

import android.support.v4.app.Fragment
import dagger.Module
import dagger.Provides
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada.EventosEntradaContract
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada.EventosEntradaPresenter
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida.EventosSalidaContract
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida.EventosSalidaPresenter

@Module
class FragmentModule(private var fragment: Fragment) {

    @Provides
    fun provideFragment(): Fragment {
        return fragment
    }

    @Provides
    fun provideEventosEntradaPresenter(): EventosEntradaContract.Presenter {
        return EventosEntradaPresenter()
    }

    @Provides
    fun provideEventosSalidaPresenter(): EventosSalidaContract.Presenter {
        return EventosSalidaPresenter()
    }
}