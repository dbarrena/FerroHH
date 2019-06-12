package expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.ui.eventos_entrada_werner

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.R
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.DetalleEvento
import expertosbd.ferroservicios.ferroserviciosbodegasecosscannerhardware.model.DetalleEventoWerner

class DetalleEventosAdapter(val context: Context, private val items: MutableList<DetalleEventoWerner>,
                            activity: Activity
) :
    RecyclerView.Adapter<DetalleEventosAdapter.EventoViewHolder>() {

    private val listener: DetalleEventosAdapter.OnItemClickListener

    init {
        this.listener = activity as DetalleEventosAdapter.OnItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.scanner_list_item, parent, false)
        return DetalleEventosAdapter.EventoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            listener.onItemClicked(item)
        }
    }

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detalleEventoID: TextView = itemView.findViewById(R.id.id_detalle_evento)
        val barcode: TextView = itemView.findViewById(R.id.barcode)
        fun bind(item: DetalleEventoWerner) {
            detalleEventoID.text = item.detalleEventoID.toString()
            barcode.text = item.codigoBarras
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: DetalleEventoWerner)
    }

}