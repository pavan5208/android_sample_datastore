package com.sample.android_sample_preference_datastore.proto

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.lifecycleScope
import com.sample.android_sample_preference_datastore.R
import com.sample.android_sample_preference_datastore.UserStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProtoSampleActivity: AppCompatActivity() {

    private val DATA_STORE_FILE_NAME = "user_store.pb"

    val Context.userDataStore: DataStore<UserStore> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = UserStoreSerializer
    )
    private var userRepo : ProtoUserRepo?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userRepo = ProtoUserRepoImpl( userDataStore)
        initListeners()
        setDataToUI()
    }


    private fun setDataToUI() {
        lifecycleScope.launch {
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