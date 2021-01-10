package com.barbeiroemcasa.extensions

import androidx.appcompat.widget.AppCompatEditText


fun AppCompatEditText.stringText(): String{
    return this.text.toString()
}

fun AppCompatEditText.isEmptyOrBlankString(): Boolean{
    return this.text.isNullOrBlank()
}
