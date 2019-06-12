package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_salida.scanner_salida

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.R
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.DetalleEventoSalida

class ScannerSalidaCompletedAdapter(val context: Context,
                                    private var items: MutableList<DetalleEventoSalida> = mutableListOf(),
                                    activity: Activity
) :
    RecyclerView.Adapter<ScannerSalidaCompletedAdapter.ScannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannerViewHolder {
        val itemView =
            LayoutInflater.from(context)
                .inflate(R.layout.list_item_scanner_completed, parent, false)
        return ScannerSalidaCompletedAdapter.ScannerViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ScannerViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    fun addDetalleEventoSalida(evento: DetalleEventoSalida) {
        items.add(evento)
    }

    fun removeDetalleEventoSalidaByID(id: Int) {
        val iterator = items.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().detalleEventoID == id)
                iterator.remove()
        }
    }

    fun findDetalleEventoSalidaByID(id: Int): DetalleEventoSalida? {
        for (evento in items) {
            if (evento.detalleEventoID == id) {
                return evento
            }
        }
        return null
    }

    fun findDetalleEventoSalidaByBarcode(barcode: String): DetalleEventoSalida? {
        for (evento in items) {
            if (evento.codigoBarras == barcode) {
                return evento
            }
        }
        return null
    }

    class ScannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val codigoBarras: TextView = itemView.findViewById(R.id.producto)
        fun bind(item: DetalleEventoSalida) {
            codigoBarras.text = item.codigoBarras
        }
    }
}