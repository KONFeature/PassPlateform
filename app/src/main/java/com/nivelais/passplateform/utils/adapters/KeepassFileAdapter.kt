package com.nivelais.passplateform.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nivelais.passplateform.R
import de.slackspace.openkeepass.domain.Entry
import de.slackspace.openkeepass.domain.Group
import de.slackspace.openkeepass.domain.KeePassFileElement
import java.util.*

class KeepassFileAdapter(
    private val files: ArrayList<KeePassFileElement>,
    private val context: Context,
    private val folderCallback: (UUID) -> Unit
) : RecyclerView.Adapter<KeepassFileAdapter.KeepassFileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeepassFileViewHolder =
        KeepassFileViewHolder(LayoutInflater.from(context).inflate(R.layout.item_keepass_file, parent, false))

    override fun getItemCount() = files.size

    override fun onBindViewHolder(holder: KeepassFileViewHolder, position: Int) {
        val file = files[position]
        if(file is Group) {
            holder.name.text = file.name
            holder.icon.setImageResource(R.drawable.ic_folder)
            holder.onClickListener { folderCallback.invoke(file.uuid) }
        } else if(file is Entry) {
            holder.name.text = file.title
            holder.icon.setImageResource(R.drawable.ic_file)
        }
    }

    fun add(entry: KeePassFileElement) {
        files.add(entry)
        notifyItemInserted(files.size)
    }

    fun add(entries: List<KeePassFileElement>) {
        entries.forEach { entry ->
            add(entry)
        }
    }

    fun clear() {
        val oldSize = files.size
        files.clear()
        notifyItemRangeRemoved(0, oldSize)
    }


    inner class KeepassFileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.text_view_entry_name)
        val icon = view.findViewById<ImageView>(R.id.image_view_file_type)

        fun onClickListener(call: () -> Unit) {
            view.setOnClickListener {
                call.invoke()
            }
        }
    }
}