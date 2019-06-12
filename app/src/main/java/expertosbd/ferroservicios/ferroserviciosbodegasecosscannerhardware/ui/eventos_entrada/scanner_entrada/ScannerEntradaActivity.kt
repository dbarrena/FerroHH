package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada.scanner_entrada

import android.content.*
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.smartdevicesdk.device.DeviceInfo
import com.smartdevicesdk.device.DeviceManage
import com.smartdevicesdk.scanner.ScannerHelper3501
import com.smartdevicesdk.utils.HandlerMessage
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.R
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.component.DaggerActivityComponent
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module.ActivityModule
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.*
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.base.BaseActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.utils.Utils
import kotlinx.android.synthetic.main.activity_scanner_entrada.*
import javax.inject.Inject

private const val EVENTO = "evento"

class ScannerEntradaActivity : BaseActivity(), ScannerEntradaContract.View, DetalleEventosAdapter.OnItemClickListener {
    private val ACTION_BARCODE_DATA = "com.honeywell.sample.action.BARCODE_DATA"
    private val ACTION_CLAIM_SCANNER = "com.honeywell.aidc.action.ACTION_CLAIM_SCANNER"
    private val ACTION_RELEASE_SCANNER = "com.honeywell.aidc.action.ACTION_RELEASE_SCANNER"
    private val EXTRA_SCANNER = "com.honeywell.aidc.extra.EXTRA_SCANNER"
    private val EXTRA_PROFILE = "com.honeywell.aidc.extra.EXTRA_PROFILE"
    private val EXTRA_PROPERTIES = "com.honeywell.aidc.extra.EXTRA_PROPERTIES"

    internal var sdkVersion = 0

    @Inject
    lateinit var presenter: ScannerEntradaContract.Presenter
    private lateinit var adapter: DetalleEventosAdapter
    private lateinit var selectedEvent: Evento
    private val estatusDetalleLOV = hashMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner_entrada)
        injectDependency()

        setSupportActionBar(toolbar_scanner)
        rootLayoutID = R.id.scanner_root

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        selectedEvent = intent.getSerializableExtra(EVENTO) as Evento
        supportActionBar?.title = "Evento ${selectedEvent.eventoBodegaID}"

        presenter.attach(this)
        presenter.fetchDetalleEventosData(selectedEvent.eventoBodegaID)
        presenter.fetchDetalleEventosEstatusData()

        showProgress(true)

        sdkVersion = android.os.Build.VERSION.SDK_INT
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(barcodeDataReceiver, IntentFilter(ACTION_BARCODE_DATA))
        claimScanner()
        Log.d("IntentApiSample: ", "onResume")
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(barcodeDataReceiver)
        releaseScanner()
        Log.d("IntentApiSample: ", "onPause")
    }

    private fun claimScanner() {
        Log.d("IntentApiSample: ", "claimScanner")
        val properties = Bundle()
        properties.putBoolean("DPR_DATA_INTENT", true)
        properties.putString("DPR_DATA_INTENT_ACTION", ACTION_BARCODE_DATA)

        properties.putInt("TRIG_AUTO_MODE_TIMEOUT", 2)
        properties.putString(
            "TRIG_SCAN_MODE",
            "readOnRelease"
        ) //This works for Hardware Trigger only! If scan is started from code, the code is responsible for a switching off the scanner before a decode

        mysendBroadcast(
            Intent(ACTION_CLAIM_SCANNER)
                .putExtra(EXTRA_SCANNER, "dcs.scanner.imager")
                .putExtra(EXTRA_PROFILE, "DEFAULT")// "MyProfile1")
                .putExtra(EXTRA_PROPERTIES, properties)
        )
    }

    private fun releaseScanner() {
        Log.d("IntentApiSample: ", "releaseScanner")
        mysendBroadcast(Intent(ACTION_RELEASE_SCANNER))
    }

    private val barcodeDataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("IntentApiSample: ", "onReceive")
            if (ACTION_BARCODE_DATA == intent.action) {
                /*
               These extras are available:
               "version" (int) = Data Intent Api version
               "aimId" (String) = The AIM Identifier
               "charset" (String) = The charset used to convert "dataBytes" to "data" string
               "codeId" (String) = The Honeywell Symbology Identifier
               "data" (String) = The barcode data as a string
               "dataBytes" (byte[]) = The barcode data as a byte array
               "timestamp" (String) = The barcode timestamp
               */
                val version = intent.getIntExtra("version", 0)
                if (version >= 1) {
                    setText(intent.getStringExtra("data"))
                    showScannerDialog(intent.getStringExtra("data"))
                }
            }
        }
    }

    private fun sendImplicitBroadcast(ctxt: Context, i: Intent) {
        val pm = ctxt.packageManager
        val matches = pm.queryBroadcastReceivers(i, 0)

        for (resolveInfo in matches) {
            val explicit = Intent(i)
            val cn = ComponentName(
                resolveInfo.activityInfo.applicationInfo.packageName,
                resolveInfo.activityInfo.name
            )

            explicit.component = cn
            ctxt.sendBroadcast(explicit)
        }
    }


    private fun mysendBroadcast(intent: Intent) {
        if (sdkVersion < 26) {
            sendBroadcast(intent)
        } else {
            sendImplicitBroadcast(applicationContext, intent)
        }
    }

    override fun onPostSuccessful(message: String) {
        showMessage(message)
        showProgress(false)
        presenter.fetchDetalleEventosData(selectedEvent.eventoBodegaID)
    }

    override fun onItemClicked(item: DetalleEvento) {
        showScannerDialog(item)
    }

    private fun showScannerDialog(barcode: String) {
        val dialog = MaterialDialog.Builder(this)
            .title(barcode)
            .customView(R.layout.dialog_scanner, false)
            .positiveText(R.string.positive)
            .negativeText(R.string.negative)
            .onAny { dialog, which ->
                when (which) {
                    DialogAction.POSITIVE -> {
                        val pesoNeto = dialog.findViewById(R.id.peso_neto) as EditText
                        val datosAdicionales =
                            dialog.findViewById(R.id.datos_adicionales) as EditText
                        val detalleEventoEstatusSpinner =
                            dialog.findViewById(R.id.estatus) as Spinner

                        val estatusID =
                            estatusDetalleLOV.get(detalleEventoEstatusSpinner.selectedItem)

                        val nuevoEvento = NuevoEvento(barcode, pesoNeto.text.toString(),
                            datosAdicionales.text.toString(), selectedEvent.eventoBodegaID,
                            estatusID!!)

                        presenter.postEvento(nuevoEvento)
                        showProgress(true)
                    }
                    DialogAction.NEGATIVE -> {
                        dialog.dismiss()
                    }
                }
            }
            .show()

        val detalleEventoEstatus: Spinner = dialog.findViewById(R.id.estatus) as Spinner
        setEstatusSpinner(detalleEventoEstatus)
    }

    private fun showScannerDialog(detalleEvento: DetalleEvento) {

        val dialog = MaterialDialog.Builder(this)
            .title(detalleEvento.codigoBarras)
            .customView(R.layout.dialog_scanner, false)
            .positiveText(R.string.positive)
            .negativeText(R.string.negative)
            .onAny { dialog, which ->
                when (which) {
                    DialogAction.POSITIVE -> {
                        val pesoNeto = dialog.findViewById(R.id.peso_neto) as EditText
                        val datosAdicionales =
                            dialog.findViewById(R.id.datos_adicionales) as EditText
                        val detalleEventoEstatusSpinner =
                            dialog.findViewById(R.id.estatus) as Spinner

                        val estatusID =
                            estatusDetalleLOV.get(detalleEventoEstatusSpinner.selectedItem)

                        val nuevoEvento = UpdateEvento(detalleEvento.codigoBarras,
                            pesoNeto.text.toString(), datosAdicionales.text.toString(),
                            selectedEvent.eventoBodegaID, estatusID!!, detalleEvento.detalleEventoID)

                        presenter.updateEvento(nuevoEvento)

                        showProgress(true)
                    }
                    DialogAction.NEGATIVE -> {
                        dialog.dismiss()
                    }
                }
            }
            .show()


        val pesoNeto = dialog.findViewById(R.id.peso_neto) as EditText
        val datosAdicionales =
            dialog.findViewById(R.id.datos_adicionales) as EditText
        val detalleEventoEstatusSpinner =
            dialog.findViewById(R.id.estatus) as Spinner
        setEstatusSpinner(detalleEventoEstatusSpinner)

        detalleEventoEstatusSpinner.let {
            it.setSelection(
                Utils().getSpinnerIndexByValue(it, detalleEvento.estatusDetalleEvento))
        }
        pesoNeto.setText(detalleEvento.pesoNeto.toString())
        datosAdicionales.setText(detalleEvento.datosAdicionales)
    }

    override fun onFetchDetalleEventosDataSuccess(items: MutableList<DetalleEvento>) {
        adapter = DetalleEventosAdapter(this, items, this)

        detalle_eventos_list.adapter = adapter
        detalle_eventos_list.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        detalle_eventos_list.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        report_title.text = "Pallets confirmados: ${items.size}"

        showProgress(false)
    }

    override fun onFetchDetalleEventosEstatusDataSuccess(items: MutableList<EstatusDetalleEvento>) {
        for (item in items) {
            estatusDetalleLOV[item.estatusDetalleEvento] = item.estatusDetalleEventoID
        }
    }

    private fun setEstatusSpinner(spinner: Spinner) {
        val spinnerArray = arrayListOf<String>()
        for (key in estatusDetalleLOV.keys) {
            spinnerArray.add(key)
        }
        val spinnerArrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray)

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerArrayAdapter
        spinner.setSelection(spinnerArray.indexOf("CORRECTO"))
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }

    override fun showErrorMessage(error: String) {
        showMessage(error)
        showProgress(false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return true
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        activityComponent.inject(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == 135 || keyCode == 136) {
            scan_result.text = "Escanea con el boton del lado derecho o con el boton 'SCAN'"
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun setText(text: String) {
        if (scan_result != null) {
            runOnUiThread { scan_result.setText(text) }
        }
    }
}
