package com.example.yogaapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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

        val adpater = YogaCourseAdpater(
            courseList,
            onEditClick ={yogaCourse ->
                editAction(yogaCourse)
            },
            onDeleteClick ={courseId ->
                deleteAction(courseId)
            }
        )

        adpater.notifyDataSetChanged()
        binding.recyclerViewCourses.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCourses.adapter = adpater
    }

private fun ShowCoursesActivity.editAction(yogaCourse: YogaCourse) {
    val alertDialog = AlertDialog.Builder(this)

    val dialogLayout = LayoutInflater.from(this).inflate(R.layout.edit_course_layout, null)

    alertDialog.setView(dialogLayout)
    val days = dialogLayout.findViewById<EditText>(R.id.editTextDayOfWeek)
    val capacity = dialogLayout.findViewById<EditText>(R.id.editTextUpdateCapacity)
    val duration = dialogLayout.findViewById<EditText>(R.id.editTextUpdateDuration)
    val price = dialogLayout.findViewById<EditText>(R.id.editTextUpdatePrice)
    val btnUpdate = dialogLayout.findViewById<Button>(R.id.btnUpdateCourseDialog)

    days.setText(yogaCourse.dayOfWeek)
    capacity.setText(yogaCourse.capacity)
    duration.setText(yogaCourse.duration)
    price.setText(yogaCourse.price)

    btnUpdate.setOnClickListener {
        val newYogaCourse = YogaCourse(
            yogaCourse.id,
            days.text.toString(),//new
            yogaCourse.timeOfCourse,
            capacity.text.toString(),//new
            duration.text.toString(),//new
            price.text.toString(),//new
            yogaCourse.typeOfClass,
            yogaCourse.description
        )

        val result = dbHelper.editCourse(newYogaCourse)
        if (result == 0) {
            Toast.makeText(this, "Update Error!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Updated Course!", Toast.LENGTH_LONG).show()
        }
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    val alert = alertDialog.create()
    alert.show()

}
    private fun ShowCoursesActivity.deleteAction(courseId: Int) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Course")
            .setMessage("Are you sure you want to delete it?")
            .setCancelable(false)
            .setPositiveButton("YES") { dialog, which ->
                val result = dbHelper.deleteCourse(courseId)
                if (result == 0) {
                    Toast.makeText(this, "Delete Error!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Deleted Course!", Toast.LENGTH_LONG).show()
                }

                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
            .setNegativeButton("NO") { dialog, which ->
                dialog.dismiss()
            }
            .create().show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.course_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       val result =  when (item.itemId){
           R.id.itemResetCourses-> {
               val alertDialog = AlertDialog.Builder(this)
               alertDialog.setTitle("Reset Course")
                   .setMessage("Are you sure you want to Reset it?")
                   .setCancelable(false)
                   .setPositiveButton("YES") { dialog, which ->
                       val result = dbHelper.resetCourse()
                       if (result == 0) {
                           Toast.makeText(this, "Reset Error!", Toast.LENGTH_LONG).show()
                       } else {
                           Toast.makeText(this, "Reset Course!", Toast.LENGTH_LONG).show()
                       }

                       intent = Intent(this, MainActivity::class.java)
                       startActivity(intent)
                       finish()

                   }
                   .setNegativeButton("NO") { dialog, which ->
                       dialog.dismiss()
                   }
                   .create().show()
               true
           }

           R.id.itemSetting -> {
               Toast.makeText(this, "Setting Item", Toast.LENGTH_LONG).show()
               true
           }
           else -> super.onOptionsItemSelected(item)
       }
        return result
    }
    }



