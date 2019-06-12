package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.utils

import android.support.annotation.ColorInt
import android.support.design.widget.Snackbar

fun Snackbar.withColor(@ColorInt colorInt: Int): Snackbar {
    this.view.setBackgroundColor(colorInt)
    return this
}