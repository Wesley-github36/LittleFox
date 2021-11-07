package com.wesleymentoor.reel.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.wesleymentoor.reel.MainActivity
import com.wesleymentoor.reel.databinding.NoInternetAlertDialogBinding

/**
 * Show a alert dialog when no internet
 */
fun showDialog(context: Context, mainActivity: MainActivity): AlertDialog {

    val builder = AlertDialog.Builder(context)
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding = NoInternetAlertDialogBinding.inflate(inflater, null, false)

    builder.setView(binding.root)
    val dialog = builder.create()
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)

    binding.okBtn.setOnClickListener {
        dialog.dismiss()
        mainActivity.finish()
    }

    return dialog

}



