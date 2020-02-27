package com.nivelais.kpass.presentation.ui.launch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nivelais.kpass.domain.entities.KFileEntity
import com.nivelais.kpass.presentation.databinding.ItemLaunchKfileBinding

/**
 * Adapter for kfile items in the launch screen
 */
class LaunchKFileAdapter(
    private var kfiles: List<KFileEntity>,
    private val selectListener: ((KFileEntity) -> Unit),
    private val removeListener: ((KFileEntity) -> Unit)
) :
    RecyclerView.Adapter<LaunchKFileAdapter.LaunchKFileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchKFileViewHolder =
        LaunchKFileViewHolder(
            ItemLaunchKfileBinding.inflate(LayoutInflater.from(parent.context))
        )

    override fun getItemCount(): Int = kfiles.size

    override fun onBindViewHolder(holder: LaunchKFileViewHolder, position: Int) =
        holder.bind(kfiles[position], selectListener, removeListener)


    /**
     * Update KFile list
     */
    fun updateKFiles(newKfiles: List<KFileEntity>) {
        kfiles = newKfiles
        notifyDataSetChanged()
    }

    /**
     * View Holder for a kfile item
     */
    class LaunchKFileViewHolder(val binding: ItemLaunchKfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind a KFile object to the view
         */
        fun bind(
            kfile: KFileEntity,
            selectListener: ((KFileEntity) -> Unit),
            removeListener: ((KFileEntity) -> Unit)
        ) {
            // Bind the item to the view
            binding.txtKfileName.text = kfile.name
            binding.txtKfileLocation.text = kfile.path

            // Bind the action to the listener
            binding.root.setOnClickListener {
                selectListener.invoke(kfile)
            }
            binding.btnDelete.setOnContextClickListener {
                removeListener.invoke(kfile)
                true
            }
        }
    }
}