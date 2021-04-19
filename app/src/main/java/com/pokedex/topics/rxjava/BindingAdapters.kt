package com.pokedex.topics.rxjava

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.pokedex.utils.RemoteStatus

@BindingAdapter( "statusProgress")
fun bindStatusProgress(statusProgressBar: ProgressBar, status: RemoteStatus?){
    when(status){
        RemoteStatus.LOADING -> {
            statusProgressBar.visibility = View.VISIBLE
        }
        else -> {
            statusProgressBar.visibility = View.GONE
        }
    }
}