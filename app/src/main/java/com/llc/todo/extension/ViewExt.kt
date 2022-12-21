package com.llc.todo.extension

import android.app.Activity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.llc.todo.R

fun Toast.showCustomToast(message: String, activity: Activity?) {
    val layout = activity?.layoutInflater?.inflate (
        R.layout.custom_layout,
        activity.findViewById(R.id.custom_toast)
    )

    val textView = layout?.findViewById<TextView>(R.id.tv_toast)
    textView?.text = message

    this.apply {
        setGravity(Gravity.BOTTOM, 0, 200)
        duration = Toast.LENGTH_SHORT
        view = layout
        show()
    }
}