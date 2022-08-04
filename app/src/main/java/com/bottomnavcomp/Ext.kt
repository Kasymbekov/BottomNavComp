package com.bottomnavcomp

import android.content.Context
import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bottomnavcomp.ui.home.NewsAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun printErrorLog(text: String) {
    Log.e("RESULT", text)
}



