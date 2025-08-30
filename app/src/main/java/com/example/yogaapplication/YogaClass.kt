package com.example.yogaapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class YogaClass(val id: Int,
                     val dateOfClass: String,
                     val teacher: String,
                     val comments: String,
                     val courseID: Int):Parcelable