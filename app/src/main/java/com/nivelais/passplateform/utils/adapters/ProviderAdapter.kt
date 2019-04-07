package com.nivelais.passplateform.utils.adapters

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.nivelais.passplateform.data.Provider

class ProviderAdapter(val providers: ArrayList<Provider>,
                      private val context: Context,
                      private val callback: (Provider) -> Unit) : RecyclerView.Adapter<ProviderAdapter.ProviderViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderViewModel {
        return ProviderViewModel(MaterialButton(context))
    }

    override fun getItemCount() = providers.size

    override fun onBindViewHolder(holder: ProviderViewModel, position: Int) {
        holder.btn.text = context.getString(providers[position].titleId)
        holder.btn.icon = context.getDrawable(providers[position].iconId)
        holder.btn.isEnabled = providers[position].available
        holder.btn.setOnClickListener {
            callback.invoke(providers[position])
        }
    }

    inner class ProviderViewModel(private val view: MaterialButton) : RecyclerView.ViewHolder(view) {
        val btn = view
    }
}

class ProviderItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            top = spaceHeight
            left =  spaceHeight
            right = spaceHeight
            bottom = spaceHeight
        }
    }
}


