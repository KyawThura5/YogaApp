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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


        binding.btnSaveCourse.setOnClickListener{

            val checkedId = binding.rdoGroupType.checkedRadioButtonId
            if(checkedId!=1){
                val rdoBtn = findViewById<RadioButton>(checkedId)
                typeOfClass =rdoBtn.text.toString()
            }

            description = binding.editDescription.text.toString()
            Toast.makeText(this,"$selectedDay,$typeOfClass,$description",Toast.LENGTH_LONG).show()

        }
    }
}