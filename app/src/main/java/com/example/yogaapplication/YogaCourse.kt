package com.example.yogaapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class YogaCourse(val id: Int,
                      val dayOfWeek: String,
                      val timeOfCourse: String,
                      val capacity:String,
                      val duration: String,
                      val price: String,
                      val typeOfClass: String,
                      val description: String): Parcelable
