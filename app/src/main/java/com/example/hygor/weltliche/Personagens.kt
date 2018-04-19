package com.example.hygor.weltliche

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class Personagens : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personagens)

        //Escondenr Action-bar
        getSupportActionBar()!!.hide()


    }
}
