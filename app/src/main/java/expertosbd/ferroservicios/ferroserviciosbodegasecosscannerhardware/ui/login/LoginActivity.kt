package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.login

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.github.javiersantos.materialstyleddialogs.enums.Style
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.component.DaggerActivityComponent
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module.ActivityModule
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.Login
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.prefs
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.base.BaseActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.main.MainActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.utils.withColor
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.R

import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginContract.View,
    BaseActivity.OnConnectionAvailableListener {

    @Inject
    lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        rootLayoutID = R.id.main_root_login
        injectDependency()
        onConnectedListener = this

        login_button.setOnClickListener {
            if (isConnected) {
                showProgress(true)
                presenter.checkLogin(user_field.text.toString().toUpperCase(),
                    password_field.text.toString())
            }
        }

        presenter.attach(this)
    }

    override fun loginResult(result: Boolean, login: Login?) {
        showProgress(false)
        if (result) {
            prefs.user = user_field.text.toString()
            prefs.firstName = login!!.nombre.toLowerCase().capitalize()
            prefs.lastName = login.apellidoPaterno.toLowerCase().capitalize()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Snackbar.make(
                main_root_login,
                "Contraseña no válida",
                Snackbar.LENGTH_SHORT
            ).withColor(resources.getColor(R.color.colorAccent)).show()
        }
    }

    override fun showErrorMessage(error: String) {
        showMessage(error)
        showProgress(false)
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progress_bar_login.visibility = View.VISIBLE
        } else {
            progress_bar_login.visibility = View.GONE
        }
    }

    override fun showVersion(localVersion: String, currentVersion: String) {
        if (localVersion == currentVersion) {
            version_number.text = "Versión: $localVersion"
        } else {
            MaterialStyledDialog.Builder(this)
                .setTitle("Versión Incompatible")
                .setDescription(
                    "Versión local: $localVersion \nVersión actual: $currentVersion " +
                            "\n\nPor favor actualiza la aplicación.\n\n")
                .setStyle(Style.HEADER_WITH_ICON)
                .setHeaderColor(R.color.colorAccent)
                .setIcon(ContextCompat.getDrawable(this, R.drawable.alert_ico)!!)
                .setCancelable(false)
                .withDialogAnimation(false) //maybe
                .show()
        }
    }

    override fun onConnectionAvailable() {
        if (version_number.text.isEmpty()) {
            presenter.checkCurrentVersion()
        }
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        activityComponent.inject(this)
    }
}
