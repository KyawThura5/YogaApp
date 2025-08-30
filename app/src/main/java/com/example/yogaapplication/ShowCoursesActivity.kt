package com.example.yogaapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yogaapplication.databinding.ActivityShowCoursesBinding
import com.example.yogaapplication.databinding.CourseItemViewBinding

class ShowCoursesActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowCoursesBinding
    lateinit var dbHelper: YogaDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityShowCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = YogaDBHelper(this)
        val courseList = dbHelper.getAllCourses()
        Log.i("******YogaDB********","***************Course List*********************"+courseList.size)
        val adpater = YogaCourseAdpater(courseList)
        adpater.notifyDataSetChanged()
        binding.recyclerViewCourses.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCourses.adapter = adpater
    }
}