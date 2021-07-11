package com.sample.android_sample_preference_datastore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val PREFERENCES_NAME_USER =  "sample_datastore_prefs"
    private val Context.prefsDataStore by preferencesDataStore(
        name = PREFERENCES_NAME_USER)

    //Use for migrating exiting preferences
//    val sharedPrefsMigration = SharedPreferencesMigration(
//        this,
//        "mySharedPreferencesName"
//    )
//    private val Context.dataStore by preferencesDataStore(
//        name = PREFERENCES_NAME_USER,
//        produceMigrations = { context ->
//            listOf(sharedPrefsMigration)
//        }
//    )


    private var userRepo : UserRepo ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userRepo = UserRepoImpl( prefsDataStore)
        initListeners()
        setDataToUI()
    }

    private fun setDataToUI() {
        lifecycleScope.launch() {
            userRepo?.getUserLoggedInState()?.collect {state->
                withContext(Dispatchers.Main) {
                  updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: Boolean) {
        findViewById<TextView>(R.id.txt_login_status)?.text =
            "User Logged-in state ${state}"
        if(state){
            findViewById<View>(R.id.parent_layout)?.setBackgroundColor(ContextCompat.getColor(this,R.color.purple_200))
        }else{
            findViewById<View>(R.id.parent_layout)?.setBackgroundColor(ContextCompat.getColor(this,R.color.design_default_color_secondary))
        }
    }

    private fun initListeners() {
        findViewById<View>(R.id.btn_login)?.setOnClickListener {
            lifecycleScope.launch {
                userRepo?.saveUserLoggedInState(true)
            }
        }
        findViewById<View>(R.id.btn_logout)?.setOnClickListener {
            lifecycleScope.launch {
                userRepo?.saveUserLoggedInState(false)
            }
        }
    }
}