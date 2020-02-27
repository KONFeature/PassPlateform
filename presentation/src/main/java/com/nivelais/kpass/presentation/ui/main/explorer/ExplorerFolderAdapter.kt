package com.nivelais.kpass.presentation.ui.main.explorer

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nivelais.kpass.domain.entities.EntryEntity
import com.nivelais.kpass.domain.entities.FileEntity
import com.nivelais.kpass.domain.entities.FolderEntity
import com.nivelais.kpass.presentation.R
import com.nivelais.kpass.presentation.inflate
import kotlinx.android.synthetic.main.item_explorer_file.view.*
import kotlinx.android.synthetic.main.item_explorer_folder.view.*

/**
 * Adapter for kfile items in the launch screen
 */
class ExplorerFolderAdapter(
    private var entries: List<EntryEntity>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_FOLDER = 1
        const val TYPE_ENTRY = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == TYPE_ENTRY) EntryViewHolder(
            parent.inflate(
                R.layout.item_explorer_folder,
                false
            )
        )
        else FolderViewHolder(parent.inflate(R.layout.item_explorer_file, false))

    override fun getItemCount(): Int = entries.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (getItemViewType(position) == TYPE_ENTRY)
            (holder as EntryViewHolder).bind(
                entries[position] as FileEntity
            )
        else (holder as FolderViewHolder).bind(
            entries[position] as FolderEntity
        )

    override fun getItemViewType(position: Int): Int =
        when (entries[position]::class.java) {
            FileEntity::class.java -> TYPE_ENTRY
            FolderEntity::class.java -> TYPE_FOLDER
            else -> TYPE_FOLDER
        }

    /**
     * Update entries displayed
     */
    fun updateEntries(entries: List<EntryEntity>) {
        this.entries = entries
        notifyDataSetChanged()
    }

    /**
     * View Holder for a kfile item
     */
    class FolderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        /**
         * Bind a KFile object to the view
         */
        fun bind(folder: FolderEntity) {
            // Bind all the simple element
            view.txt_explorer_folder_name.text = folder.name
            view.txt_explorer_folder_entry_count.text = folder.entries?.size.toString()?:"0"
        }
    }

    /**
     * View Holder for a kfile item
     */
    class EntryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        /**
         * Bind a KFile object to the view
         */
        fun bind(file: FileEntity) {
            // Bind all the simple element
            view.txt_explorer_file_name.text = file.name
        }
    }
}