package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.utils

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Spinner
import android.widget.TextView


class Utils {

    fun showSnackBar(view: View, text: String, backgroundColor: Int): Snackbar {
        return Snackbar.make(
            view,
            text,
            Snackbar.LENGTH_SHORT
        ).withColor(backgroundColor)
    }

    fun showSnackBar(view: View, text: String, backgroundColor: Int, duration: Int): Snackbar {
        val snackbar = Snackbar.make(
            view,
            text,
            duration
        ).withColor(backgroundColor)

        val snackview = snackbar.getView()
        val tv = snackview.findViewById(android.support.design.R.id.snackbar_text) as TextView
        //tv.setTextColor(Color.BLACK)

        return snackbar
    }

    fun getSpinnerIndexByValue(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }
        // Check for this when you set the position.
        return -1
    }
}