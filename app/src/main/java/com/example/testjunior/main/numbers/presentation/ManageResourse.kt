package com.example.testjunior.main.numbers.presentation

import android.content.Context
import androidx.annotation.StringRes

interface ManageResources {

    fun addString(@StringRes id: Int): String

    class Base(private val context: Context) : ManageResources {
        override fun addString(id: Int): String = context.getString(id )

    }
}