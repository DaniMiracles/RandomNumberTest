package com.example.testjunior.main.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.testjunior.R
import com.example.testjunior.main.numbers.presentation.NumbersFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container,NumbersFragment())
            .commit()
    }
}

interface ShowFragment {

    fun show(fragment: Fragment)
}