package com.example.pinterbest.utilities

import android.widget.TextView

object HandlerError {
    fun handleResourceNotFoundError(textView: TextView, message: String) {
        textView.text = "Извините, ресурс не найден!"
    }

    fun handleInternalServerError(textView: TextView, message: String) {
        textView.text = "Извините, произошла внутренняя ошибка сервера!"
    }

    fun handleNetworkExceptionError(textView: TextView, message: String) {
        textView.text = "Не удается установить соединение с Интернетом!"
    }
}
