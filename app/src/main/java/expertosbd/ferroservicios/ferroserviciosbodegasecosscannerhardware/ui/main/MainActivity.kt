package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.main

import android.os.Bundle
import android.app.Activity
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.component.DaggerActivityComponent
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module.ActivityModule
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.prefs
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.base.BaseActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada.EventosEntradaFragment
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida.EventosSalidaFragment
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootLayoutID = R.id.main_root
        setSupportActionBar(toolbar)
        injectDependency()

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        setToolbarName("Bodega Secos")
        setNavEmployeeName("${prefs.firstName.capitalize()} ${prefs.lastName}")
    }

    private fun setNavEmployeeName(name: String) {
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.employee_name).text = name
    }

    fun setToolbarName(name: String) {
        supportActionBar?.title = name
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (main_logo.visibility == View.VISIBLE) {
            main_logo.visibility = View.GONE
        }

        when (item.itemId) {
            R.id.eventos_entrada -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, EventosEntradaFragment())
                    .commit()
            }
            R.id.eventos_salida -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, EventosSalidaFragment())
                    .commit()
            }
            R.id.eventos_werner -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, EventosEntradaFragment.newInstance(true))
                    .commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        activityComponent.inject(this)
    }
}

