package com.nivelais.passplateform.ui.database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nivelais.passplateform.R

class DatabaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, ListFragment.newInstance())
//                .commitNow()
        }
    }

}
