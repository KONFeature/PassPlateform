package com.nivelais.passplateform.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.slackspace.openkeepass.domain.Entry
import com.nivelais.passplateform.R

class EntryAdapter(
    private val entries: ArrayList<Entry>,
    private val context: Context
) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder =
        EntryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_entry, parent, false))

    override fun getItemCount() = entries.size

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.entryName.text = entries[position].title
    }

    fun add(entry: Entry) {
        entries.add(entry)
        notifyItemInserted(entries.size)
    }

    fun add(entries: List<Entry>) {
        entries.forEach { entry ->
            add(entry)
        }
    }


    inner class EntryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val entryName = view.findViewById<TextView>(R.id.text_view_entry_name)

    }
}