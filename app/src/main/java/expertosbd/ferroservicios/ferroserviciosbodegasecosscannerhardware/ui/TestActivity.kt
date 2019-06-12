package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui

import android.content.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    private val TAG = "IntentApiSample"
    private val ACTION_BARCODE_DATA = "com.honeywell.sample.action.BARCODE_DATA"
    private val ACTION_CLAIM_SCANNER = "com.honeywell.aidc.action.ACTION_CLAIM_SCANNER"
    private val ACTION_RELEASE_SCANNER = "com.honeywell.aidc.action.ACTION_RELEASE_SCANNER"
    private val EXTRA_SCANNER = "com.honeywell.aidc.extra.EXTRA_SCANNER"
    private val EXTRA_PROFILE = "com.honeywell.aidc.extra.EXTRA_PROFILE"
    private val EXTRA_PROPERTIES = "com.honeywell.aidc.extra.EXTRA_PROPERTIES"
    private val EXTRA_CONTROL = "com.honeywell.aidc.action.ACTION_CONTROL_SCANNER"
    private val EXTRA_SCAN = "com.honeywell.aidc.extra.EXTRA_SCAN"

    internal var sdkVersion = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        sdkVersion = android.os.Build.VERSION.SDK_INT
        Log.d(TAG, "sdkVersion=$sdkVersion\n")
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
            //for Android O above "gives W/BroadcastQueue: Background execution not allowed: receiving Intent"
            //either set targetSDKversion to 25 or use implicit broadcast
            sendImplicitBroadcast(applicationContext, intent)
        }
    }

    private fun setText(text: String) {
        if (data != null) {
            runOnUiThread { data.setText(text) }
        }
    }

    private fun bytesToHexString(arr: ByteArray?): String {
        var s = "[]"
        if (arr != null) {
            s = "["
            for (i in arr.indices) {
                s += "0x" + Integer.toHexString(arr[i].toInt()) + ", "
            }
            s = s.substring(0, s.length - 2) + "]"
        }
        return s
    }
}
