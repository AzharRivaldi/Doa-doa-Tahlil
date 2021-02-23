package com.project.doatahlil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cn.refactor.lib.colordialog.PromptDialog
import com.project.doatahlil.R
import com.project.doatahlil.adapter.AdapterDoa.ListViewHolder
import com.project.doatahlil.model.ModelDoa
import kotlinx.android.synthetic.main.list_item_doa.view.*
import java.util.*

/**
 * Created by Azhar Rivaldi on 05-02-2021
 */

class AdapterDoa(private val context: Context, private val modelBacaan: MutableList<ModelDoa>) :
        RecyclerView.Adapter<ListViewHolder>(), Filterable {

    var modelBacaanListFull: List<ModelDoa>

    init {
        modelBacaanListFull = ArrayList(modelBacaan)
    }

    override fun getFilter(): Filter {
        return modelBacaanFilter
    }

    private val modelBacaanFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<ModelDoa> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(modelBacaanListFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT)
                for (modelDoaFilter in modelBacaanListFull) {
                    if (modelDoaFilter.strTitle.toLowerCase().contains(filterPattern)) {
                        filteredList.add(modelDoaFilter)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            modelBacaan.clear()
            modelBacaan.addAll(results.values as List<*>)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_doa, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(listViewHolder: ListViewHolder, position: Int) {
        val dataModel = modelBacaan[position]
        listViewHolder.tvId.text = dataModel.strId
        listViewHolder.tvTitle.text = dataModel.strTitle
        listViewHolder.cardListDoa.setOnClickListener {
            PromptDialog(context)
                    .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                    .setAnimationEnable(true)
                    .setTitleText(dataModel.strTitle)
                    .setContentText(dataModel.strArabic + "\n\n" + dataModel.strTranslation)
                    .setPositiveListener("TUTUP") {
                        dialog -> dialog.dismiss()
                    }.show()
        }
    }

    override fun getItemCount(): Int {
        return modelBacaan.size
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardListDoa: CardView
        var tvId: TextView
        var tvTitle: TextView

        init {
            tvId = itemView.tvId
            tvTitle = itemView.tvTitle
            cardListDoa = itemView.cardListDoa
        }
    }

}