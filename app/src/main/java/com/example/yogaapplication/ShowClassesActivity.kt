package com.example.yogaapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yogaapplication.databinding.ActivityShowClassesBinding

class ShowClassesActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowClassesBinding
    lateinit var dbHelper: YogaDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = YogaDBHelper(this)

        val classList = dbHelper.getAllClass()

        Log.i("******YogaDB********","***************Class List*********************"+classList.size)

        val adpater = YogaClassAdapter(
            classList,
            onEditClassClick ={yogaClass ->
                editAction(yogaClass)
            },
            onDeleteClassClick ={classID ->
                deleteAction(classID)
            },
            {classID ->
                showCourseDetailsFromClassAction(classID)

            }
        )

        adpater.notifyDataSetChanged()
        binding.recyclerViewViewClasses.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewViewClasses.adapter = adpater

    }


    private fun ShowClassesActivity.editAction(yogaClass: YogaClass) {
        val alertDialog = AlertDialog.Builder(this)

        val dialogLayout = LayoutInflater.from(this).inflate(R.layout.edit_class_layout, null)

        alertDialog.setView(dialogLayout)
        val days = dialogLayout.findViewById<EditText>(R.id.editTextDayOfWeekClass)
        val teacher = dialogLayout.findViewById<EditText>(R.id.editTextTeacherOfClass)
        val comment = dialogLayout.findViewById<EditText>(R.id.editTextCommentOfClass)

        val btnUpdate = dialogLayout.findViewById<Button>(R.id.btnUpdateClassDialog)

        days.setText(yogaClass.dateOfClass)
        teacher.setText(yogaClass.teacher)
        comment.setText(yogaClass.comments)

        btnUpdate.setOnClickListener {
            val newYogaClass = YogaClass(
               yogaClass.id,
                days.text.toString(),
                teacher.text.toString(),
                comment.text.toString(),
                yogaClass.courseID
            )

            val result = dbHelper.editClass(newYogaClass)
            if (result == 0) {
                Toast.makeText(this, "Update Error!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Updated Class!", Toast.LENGTH_LONG).show()
            }
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val alert = alertDialog.create()
        alert.show()

    }
    private fun ShowClassesActivity.deleteAction(classID: Int) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Class")
            .setMessage("Are you sure you want to delete it?")
            .setCancelable(false)
            .setPositiveButton("YES") { dialog, which ->
                val result = dbHelper.deleteClass(classID)
                if (result == 0) {
                    Toast.makeText(this, "Delete Error!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Deleted Class!", Toast.LENGTH_LONG).show()
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


    private fun ShowClassesActivity.showCourseDetailsFromClassAction(clID: Int) {
        val classItem = dbHelper.getClassById(clID)
        if (classItem == null) {
            Toast.makeText(this, "Class not found", Toast.LENGTH_SHORT).show()
            return
        }

        val yogaCourse = dbHelper.getCourseById(classItem.courseID)
        if (yogaCourse == null) {
            Toast.makeText(this, "Course not found", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = StringBuilder()
        builder.append("Course Details\n")
        builder.append("Day: ${yogaCourse.dayOfWeek}\n")
        builder.append("Time: ${yogaCourse.timeOfCourse}\n")
        builder.append("Type: ${yogaCourse.typeOfClass}\n")
        builder.append("Capacity: ${yogaCourse.capacity}\n")
        builder.append("Duration: ${yogaCourse.duration}\n")
        builder.append("Price: ${yogaCourse.price}\n")
        builder.append("Description: ${yogaCourse.description}\n\n")
        builder.append("Class Details\n")
        builder.append("Date: ${classItem.dateOfClass}\n")
        builder.append("Teacher: ${classItem.teacher}\n")
        builder.append("Comments: ${classItem.comments ?: "-"}\n")

        AlertDialog.Builder(this)
            .setTitle("Course & Class Details")
            .setMessage(builder.toString())
            .setPositiveButton("OK", null)
            .show()
    }

}