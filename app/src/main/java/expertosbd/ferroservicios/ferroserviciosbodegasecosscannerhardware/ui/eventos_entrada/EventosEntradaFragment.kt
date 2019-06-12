package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.R
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.component.DaggerFragmentComponent
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module.FragmentModule
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.Evento
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.FinManiobra
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.InicioManiobra
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.prefs
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada.scanner_entrada.ScannerEntradaActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada_werner.ScannerEntradaWernerActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_eventos_entrada.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val WERNER = "WERNER"

class EventosEntradaFragment : Fragment(), EventosEntradaContract.View,
    SearchView.OnQueryTextListener,
    EventosEntradaAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Inject
    lateinit var presenter: EventosEntradaContract.Presenter
    private lateinit var adapter: EventosEntradaAdapter
    private lateinit var activity: MainActivity
    private var werner: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
        presenter.attach(this)
        presenter.subscribe()
        activity = getActivity() as MainActivity
        activity.setToolbarName("Eventos Entrada")

        arguments?.let {
            werner = it.getBoolean(WERNER)
            activity.setToolbarName("Eventos Entrada Werner")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_eventos_entrada, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSearchView(eventos_search)
        swipe_container.setOnRefreshListener(this)
    }

    private fun setSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(this)
        searchView.isSubmitButtonEnabled = true
        searchView.queryHint = "Busqueda"
        searchView.isFocusable = false
        searchView.isIconified = false
        searchView.clearFocus()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (::adapter.isInitialized) {
            adapter.filter.filter(query)
        }
        return false
    }

    override fun onItemClicked(item: Evento) {
        if(werner == null) {
            val intent = Intent(activity, ScannerEntradaActivity::class.java)
            intent.putExtra("evento", item)
            startActivity(intent)
        } else {
            val intent = Intent(activity, ScannerEntradaWernerActivity::class.java)
            intent.putExtra("evento", item)
            startActivity(intent)
        }
    }

    override fun onRefresh() {
        adapter =
                EventosEntradaAdapter(
                    context!!, mutableListOf<Evento>(), this
                )
        presenter.fetchData()
    }

    override fun showProgress(show: Boolean) {
        val progressBar = activity.findViewById<FrameLayout>(R.id.progress_bar_main)
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE

            if (swipe_container.isRefreshing) {
                swipe_container.isRefreshing = false
            }
        }
    }

    override fun onFetchDataSuccess(items: MutableList<Evento>) {
        adapter =
                EventosEntradaAdapter(
                    context!!, items, this
                )
        eventos_list.adapter = adapter
        eventos_list.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        eventos_list.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        showProgress(false)
    }

    override fun OnMoreOptionsClicked(item: Evento) {
        showMoreOptionsDialog(item)
    }

    private fun showMoreOptionsDialog(evento: Evento) {
        val dialog = MaterialDialog.Builder(context!!)
            .title("Más opciones")
            .titleColorRes(R.color.colorPrimary)
            .dividerColorRes(R.color.colorPrimary)
            .customView(R.layout.more_options_dialog, false)
            .negativeText(R.string.negative)
            .onAny { dialog, which ->
                when (which) {
                    DialogAction.NEGATIVE -> {
                        dialog.dismiss()
                    }
                }
            }
            .show()

        val progressBar = dialog.findViewById(R.id.progress_bar) as ProgressBar
        val buttons = dialog.findViewById(R.id.buttons) as LinearLayout

        val inicioManiobraBtn = dialog.findViewById(R.id.inicio_maniobra_btn) as Button
        val finManiobraBtn = dialog.findViewById(R.id.fin_maniobra_btn) as Button

        val format = SimpleDateFormat("dd/MM/yyyy,HH:mm")

        inicioManiobraBtn.setOnClickListener {
            buttons.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            val date = format.format(Calendar.getInstance().time)
            val inicioManiobra = InicioManiobra(date, prefs.user, evento.eventoBodegaID)

            presenter.addSubscription(
                presenter.inicioManiobra(inicioManiobra).subscribe(
                    {
                        activity.showMessage("Inicio Maniobra Registrado")
                        dialog.dismiss()
                    },
                    { error ->
                        showErrorMessage(error.localizedMessage)
                        buttons.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                ))
        }

        finManiobraBtn.setOnClickListener {
            buttons.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            val date = format.format(Calendar.getInstance().time)
            val finManiobra = FinManiobra(date, prefs.user, evento.eventoBodegaID)

            presenter.addSubscription(
                presenter.finManiobra(finManiobra).subscribe(
                    {
                        activity.showMessage("Fin Maniobra Registrado")
                        dialog.dismiss()
                    },
                    { error ->
                        showErrorMessage(error.localizedMessage)
                        buttons.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                ))
        }

    }

    override fun onPostSuccesful(message: String) {
        activity.showMessage(message)
        showProgress(false)
    }

    override fun showErrorMessage(error: String) {
        activity.showMessage(error)
        showProgress(false)
    }

    private fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule(this))
            .build()
        fragmentComponent.inject(this)
    }


    companion object {

        @JvmStatic
        fun newInstance(werner: Boolean) =
            EventosEntradaFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(WERNER, werner)
                }
            }
    }
}