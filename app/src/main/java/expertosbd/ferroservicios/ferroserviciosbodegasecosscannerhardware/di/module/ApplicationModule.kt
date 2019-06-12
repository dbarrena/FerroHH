package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.BaseApp
import javax.inject.Singleton

@Module
class ApplicationModule(private val baseApp: BaseApp) {

    @Provides
    @Singleton
    fun provideApplication(): Application = baseApp

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = baseApp
}