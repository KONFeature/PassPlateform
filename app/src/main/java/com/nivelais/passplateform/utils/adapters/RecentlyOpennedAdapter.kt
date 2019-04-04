package com.nivelais.passplateform.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toFile
import androidx.recyclerview.widget.RecyclerView
import com.nivelais.passplateform.data.local.entities.PassDatabase
import com.nivelais.passplateform.R
import com.nivelais.passplateform.utils.Provider
import kotlinx.android.synthetic.main.item_recently_openned_db.view.*

class RecentlyOpennedAdapter(
    val databases: ArrayList<PassDatabase>,
    private val context: Context,
    private val callback: (Long) -> Unit
) : RecyclerView.Adapter<RecentlyOpennedAdapter.RecentlyOpennedViewHolder>() {

    // Create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecentlyOpennedViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_recently_openned_db,
                parent,
                false
            )
        )

    // Get item number
    override fun getItemCount() = databases.size

    // Bind the view holder
    override fun onBindViewHolder(holder: RecentlyOpennedViewHolder, position: Int) {
        holder.dbName?.text = databases[position].name
        holder.dbProvider?.text = context.getString(databases[position].provider.titleId)
        holder.dbPath?.text = databases[position].distPath.path
        holder.onClickListener { callback.invoke(databases[position].id) }
    }

    // View Holder for this adapter
    inner class RecentlyOpennedViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val dbName = view.text_view_db_name
        val dbProvider = view.text_view_db_provider
        val dbPath = view.text_view_db_path

        fun onClickListener(call: () -> Unit) {
            view.setOnClickListener {
                call.invoke()
            }
        }
    }
}