package com.example.yogaapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.yogaapplication.databinding.ActivityCourseEntryBinding
import com.example.yogaapplication.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale

class CourseEntryActivity : AppCompatActivity() {

    lateinit var binding: ActivityCourseEntryBinding
    private val days = listOf("..Select the day of week","Monday","TueDay","Wednesday","Thursday","Friday","Saturday","Sunday")
    private  var selectedDay:String=""
    private  var description:String=""

    private var selectedTime: String=""

    private var typeOfClass:String =""

    private  var selectedDate: String=""

    private var capacity:String =""

    private var duration:String =""

    private var price:String =""

    private var flag: Boolean = false

    lateinit var dbHelper: YogaDBHelper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHelper = YogaDBHelper(this)
        binding = ActivityCourseEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,days)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDayOfWeek.adapter = arrayAdapter

        binding.spinnerDayOfWeek.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedDay = days[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        ///////////////////////////////////////
        //Time of Course / TimePicker
        val calendar = Calendar.getInstance()
        binding.txtTimePicker.setOnClickListener {
            val timePickerDialog = TimePickerDialog(this,{
               _,hr,min ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY,hr)
                cal.set(Calendar.MINUTE,min)
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                 selectedTime = timeFormat.format(cal.time)

                binding.txtTimePicker.text="Time of course:$selectedTime"                               },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
                )
            timePickerDialog.show()
        }

//        val calendar = Calendar.getInstance()
//        binding.txtTimePicker.setOnClickListener {
//            val DatePickerDialog = DatePickerDialog(this,{
//                    _,y,m,d ->
//                val cal = Calendar.getInstance()
//                cal.set(y,m,d)
//
//                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//                selectedDate = dateFormat.format(cal.time)
//
//                binding.txtTimePicker.text="Selected Date :$selectedDate"
//                  },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            )
//            DatePickerDialog.show()
//        }

        //////////////////////////////////////////
        //Type of Class (radio)


        binding.btnSaveCourse.setOnClickListener {

            flag = false
            if (selectedDay == days[0]) {
                selectedDay = "No selected Day of the week"
                flag = true
            }

            val checkedId = binding.rdoGroupType.checkedRadioButtonId

            if (checkedId != -1) {
                val rdoBtn = findViewById<RadioButton>(checkedId)
                typeOfClass = rdoBtn.text.toString()
            } else {
                typeOfClass = "No selected type of Class"
                flag = true
            }

            if (selectedTime == "") {
                selectedTime = "No selected Time"
                flag = true
            }
            capacity = binding.editCapacity.text.toString()

            if (capacity.isEmpty()) {
                binding.editCapacity.error = "Please fill the capacity"
                flag = true
            }

            duration = binding.editDuration.text.toString()

            if (duration.isEmpty()) {
                binding.editDuration.error = "Please fill the duration"
                flag = true
            }

            price = binding.editPrice.text.toString()

            if (price.isEmpty()) {
                binding.editPrice.error = " Please fill the price"
                flag = true
            }

            description = binding.editDescription.text.toString()

            if (description.isEmpty()) {
                binding.editDescription.error = " Please fill description"
                flag = true
            }

            Toast.makeText(this, "$selectedDay,$typeOfClass,$description", Toast.LENGTH_LONG).show()

            if (!flag) {
                val stringBuilder = StringBuilder()
                stringBuilder.append("Are you sure you want to save? \n")
                    .append("Course Information \n")
                    .append("Day of Week: $selectedDay \n")
                    .append("Selected Time: $selectedTime \n")
                    .append("Capacity: $capacity \n")
                    .append("Duration : $duration \n")
                    .append("Price: $price \n")
                    .append("Type of Class: $typeOfClass\n")
                    .append("Description : $description\n")

                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Confirmation")
                    .setMessage(stringBuilder.toString())
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, which ->
                        //Toast.makeText(this, "Yes Click!", Toast.LENGTH_LONG).show()
                        dbHelper.saveCourse("Yoga Course");
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        Toast.makeText(this, "No Click!", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }

                val alert = alertDialog.create()
                alert.show()

            }
        }
    }
}