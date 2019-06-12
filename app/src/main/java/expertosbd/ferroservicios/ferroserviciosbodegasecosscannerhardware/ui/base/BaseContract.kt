package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.base

class BaseContract {

    interface Presenter<in T> {
        fun subscribe()
        fun unsubscribe()
        fun attach(view: T)
    }

    interface View {
        fun showErrorMessage(error: String)
    }

}