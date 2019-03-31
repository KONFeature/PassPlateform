package com.nivelais.passplateform.utils.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nivelais.passplateform.App
import com.nivelais.passplateform.R
import com.nivelais.passplateform.utils.Provider
import kotlinx.android.synthetic.main.item_provider.view.*
class ProviderAdapter(val providers: ArrayList<Provider>,
                      private val context: Context,
                      private val callback: (Provider) -> Unit) : RecyclerView.Adapter<ProviderAdapter.ProviderViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderViewModel {
        val view = LayoutInflater.from(context).inflate(R.layout.item_provider, parent, false)
        return ProviderViewModel(view)
    }

    override fun getItemCount() = providers.size

    override fun onBindViewHolder(holder: ProviderViewModel, position: Int) {
        holder.providerName.text = context.getString(providers[position].titleId)
        holder.providerIcon.setImageResource(providers[position].iconId)
        holder.onClickListener { callback.invoke(providers[position]) }
    }

    inner class ProviderViewModel(private val view: View) : RecyclerView.ViewHolder(view) {
        var providerName = view.text_view_providder_name
        var providerIcon = view.image_view_provider_icon

        fun onClickListener(call: () -> Unit) {
            view.setOnClickListener {
                call.invoke()
            }
        }
    }
}

