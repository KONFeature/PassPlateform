package com.nivelais.passplateform.ui.start

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nivelais.passplateform.App

import com.nivelais.passplateform.R
import com.nivelais.passplateform.data.local.entities.PassDatabase
import com.nivelais.passplateform.ui.opendb.OpenDbViewModel
import com.nivelais.passplateform.utils.adapters.RecentlyOpennedAdapter
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    // Define and load the view vm
    private val vm by lazy {
        ViewModelProviders.of(this)[StartViewModel::class.java]
    }

    // Listener for ur fragment callback
    private lateinit var listener: OnStartFragmentAction

    // All the other ui component relative to this activity
    private lateinit var recentDatabasesView: RecyclerView
    private lateinit var recentTextView: TextView
    private lateinit var openDbButton: Button

    // When view is created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        // Load the ui component
        recentDatabasesView = view.findViewById(R.id.recycler_view_recently_open)
        recentTextView = view.findViewById(R.id.text_view_recently_open)
        openDbButton = view.findViewById(R.id.button_open_db)
        openDbButton.setOnClickListener { listener.onClickOpenDb() }

        // Init recycler view
        activity?.let { ctx ->
            recentDatabasesView.itemAnimator = SlideInLeftAnimator()
            recentDatabasesView.layoutManager = LinearLayoutManager(ctx)
            recentDatabasesView.adapter = RecentlyOpennedAdapter(vm.getDatabases(), ctx)
        }

        return view
    }

    // When fragment is attached to context
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Attach listener to fragment
        if(context is OnStartFragmentAction)
            listener = context
        else
            throw ClassCastException("Caller context of start fragment must implement OnStartFragmentAction")
    }

    interface OnStartFragmentAction {
        fun onClickOpenDb()
    }
}
