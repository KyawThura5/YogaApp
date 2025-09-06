package com.example.yogaapplication

import android.content.DialogInterface
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
            },
            {yogaCourse ->
                addAction(yogaCourse)

            },
            onShowDetailsCourse={yogaCourse -> 
                showDetails(yogaCourse)
                        
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

private fun ShowCoursesActivity.showDetails(yogaCourse: com.example.yogaapplication.YogaCourse) {
 val classList = dbHelper.showCourseDetails(yogaCourse.id)
    val builder = StringBuilder()
    builder.append("Course: ${yogaCourse.dayOfWeek}\n")
    builder.append("Time: ${yogaCourse.timeOfCourse}\n")
    builder.append("Type: ${yogaCourse.typeOfClass}\n")
    builder.append("Classes: \n")
    if (classList.isEmpty()){
        builder.append("No classes available.")
    }else{
        classList.forEachIndexed { index, yogaClass ->
            builder.append("${index + 1} . Date : ${yogaClass.dateOfClass} ,")
            builder.append("Teacher : ${yogaClass.teacher} ,")
            builder.append("Comment : ${yogaClass.comments}\n\n")
        }
    }
    AlertDialog.Builder(this)
        .setTitle("Course Details")
        .setMessage(builder.toString())
        .setPositiveButton("OK", null)
        .show()
}

private fun ShowCoursesActivity.addAction(yogaCourse: YogaCourse) {
    val dialogView = layoutInflater.inflate(R.layout.add_class_layout,null)
    val dateInput = dialogView.findViewById<EditText>(R.id.dateOfClass)
    val teacherInput = dialogView.findViewById<EditText>(R.id.teacher)
    val commentInput = dialogView.findViewById<EditText>(R.id.comment)

    AlertDialog.Builder(this)
        .setTitle("Add Class ${yogaCourse.dayOfWeek} for ${yogaCourse.typeOfClass}")
        .setView(dialogView)
        .setPositiveButton("ADD", DialogInterface.OnClickListener {dialog, which->
            val date = dateInput.text.toString()
            val teacher = teacherInput.text.toString()
            val comment = commentInput.text.toString()

            if (date.isEmpty() || teacher.isEmpty()) {
                Toast.makeText(this, "Date and Teacher cannot be empty", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val yogaClass = YogaClass(
                id = 0,   // auto-increment in DB
                dateOfClass = date,
                teacher = teacher,
                comments = comment,
                courseID = yogaCourse.id   // link class to course
            )

            val result = dbHelper.saveClass(yogaClass)

            if (result > 0) {
                Toast.makeText(this, "Class added successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to add class!", Toast.LENGTH_SHORT).show()
            }
        })
        .setNegativeButton("Cancel", null)
        .show()
}



