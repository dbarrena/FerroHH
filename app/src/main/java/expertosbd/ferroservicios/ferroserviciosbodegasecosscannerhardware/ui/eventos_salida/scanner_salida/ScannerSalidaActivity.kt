package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida.scanner_salida

import android.content.*
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.smartdevicesdk.device.DeviceInfo
import com.smartdevicesdk.device.DeviceManage
import com.smartdevicesdk.scanner.ScannerHelper3501
import com.smartdevicesdk.utils.HandlerMessage
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.R
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.component.DaggerActivityComponent
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.di.module.ActivityModule
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.DetalleEventoSalida
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.Evento
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.UpdateEventoSalida
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_scanner_salida.*
import java.util.*
import javax.inject.Inject

private const val EVENTO = "evento"

class ScannerSalidaActivity : BaseActivity(), ScannerSalidaContract.View {
    private val ACTION_BARCODE_DATA = "com.honeywell.sample.action.BARCODE_DATA"
    private val ACTION_CLAIM_SCANNER = "com.honeywell.aidc.action.ACTION_CLAIM_SCANNER"
    private val ACTION_RELEASE_SCANNER = "com.honeywell.aidc.action.ACTION_RELEASE_SCANNER"
    private val EXTRA_SCANNER = "com.honeywell.aidc.extra.EXTRA_SCANNER"
    private val EXTRA_PROFILE = "com.honeywell.aidc.extra.EXTRA_PROFILE"
    private val EXTRA_PROPERTIES = "com.honeywell.aidc.extra.EXTRA_PROPERTIES"

    internal var sdkVersion = 0

    @Inject
    lateinit var presenter: ScannerSalidaContract.Presenter
    private var barcodeScanningEnabled = false
    private lateinit var selectedEvent: Evento

    lateinit var pendingAdapter: ScannerSalidaPendingAdapter
    lateinit var completedAdapter: ScannerSalidaCompletedAdapter
    private val detalleEventosMap: HashMap<String, DetalleEventoSalida> = HashMap()

    lateinit var player: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scanner_salida)
        injectDependency()
        setSupportActionBar(toolbar_scanner)
        rootLayoutID = R.id.scanner_root
        presenter.attach(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sdkVersion = android.os.Build.VERSION.SDK_INT

        selectedEvent = intent.getSerializableExtra(EVENTO) as Evento
        supportActionBar?.title = "Evento ${selectedEvent.eventoBodegaID}"
        presenter.fetchEventosSalida(selectedEvent.eventoBodegaID)

        pending_items_button.setOnClickListener { expandable_layout_pending.toggle() }
        completed_items_button.setOnClickListener { expandable_layout_completed.toggle() }

        scan_result.setOnClickListener {
            val index = "VQJL26668-L-SDFSL-4445-H".indexOf("-")
            val barcode = "VQJL26668-L-SDFSL-4445-H".substring(0, index)

            if(index != -1){
                scan_result.setText(barcode)
            } else {
                scan_result.setText(barcode)

            }
        }
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
                    processScannedResult(intent.getStringExtra("data"))
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


    override fun onFetchEventosSalidaSuccess(items: MutableList<DetalleEventoSalida>) {
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)

        val layoutManagerPending = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val pendingItems = mutableListOf<DetalleEventoSalida>()

        val layoutManagerCompleted = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val completedItems = mutableListOf<DetalleEventoSalida>()

        items.forEach {
            detalleEventosMap[it.codigoBarras] = it
            if(it.estatusDetalleEventoID == 1004) {
                pendingItems.add(it)
            } else if (it.estatusDetalleEventoID == 1005) {
                completedItems.add(it)
            }
        }

        pendingAdapter = ScannerSalidaPendingAdapter(this, pendingItems, this)
        scanner_items_pending.apply {
            adapter = pendingAdapter
            layoutManager = layoutManagerPending
            addItemDecoration(dividerItemDecoration)
            isNestedScrollingEnabled = false
        }

        completedAdapter = ScannerSalidaCompletedAdapter(this, completedItems, this)
        scanner_items_completed.apply {
            adapter = completedAdapter
            layoutManager = layoutManagerCompleted
            addItemDecoration(dividerItemDecoration)
            isNestedScrollingEnabled = false
        }

        showProgress(false)
    }

    override fun onPostSuccessful(message: String, eventoID: Int) {
        showMessage(message)
        updateAdapters(eventoID)
    }

    override fun showErrorMessage(error: String) {
        showMessage(error)
        showProgress(false)
    }

    private fun processScannedResult(barcode: String){
        val detalleEventoSalida = detalleEventosMap[barcode]
        val detalleEventoCompleted: DetalleEventoSalida? =
            completedAdapter.findDetalleEventoSalidaByBarcode(barcode)

        try {
            val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
            tg.startTone(ToneGenerator.TONE_PROP_BEEP)

            detalleEventoCompleted?.let { showErrorMessage("Este paquete ya esta registrado") }
                ?: run {

                    detalleEventoSalida?.let {
                        presenter.updateEvento(UpdateEventoSalida(it.detalleEventoID))
                    } ?: showErrorMessage("No se encuentra el codigo de barras: $barcode")

                }

        } catch (e: Exception) {
            showErrorMessage(e.localizedMessage)
        }
    }

    override fun showProgress(show: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar_scanner)
        if (show) {
            progressBar.visibility = View.VISIBLE
            report.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            report.visibility = View.VISIBLE
            pending_items_text.text = "ASIGNADOS: ${pendingAdapter.itemCount}"
            completed_items_text.text = "EMBARCADOS: ${completedAdapter.itemCount}"
        }
    }

    private fun updateAdapters(paqueteID: Int) {
        pendingAdapter.findDetalleEventoSalidaByID(paqueteID)?.let {
            completedAdapter.addDetalleEventoSalida(it)
            pendingAdapter.removeDetalleEventoSalidaByID(paqueteID)

            completedAdapter.notifyDataSetChanged()
            pendingAdapter.notifyDataSetChanged()

            pending_items_text.text = "ASIGNADOS: ${pendingAdapter.itemCount}"
            completed_items_text.text = "EMBARCADOS: ${completedAdapter.itemCount}"
        }
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

    private fun setText(text: String) {
        if (scan_result != null) {
            runOnUiThread { scan_result.setText(text) }
        }
    }

}

