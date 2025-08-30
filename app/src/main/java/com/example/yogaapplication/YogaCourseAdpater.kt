package com.example.yogaapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yogaapplication.databinding.CourseItemViewBinding

class YogaCourseAdpater(val courseList:List<YogaCourse>): RecyclerView.Adapter<YogaCourseAdpater.YogaCourseViewHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): YogaCourseViewHolder {
        val binding = CourseItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return YogaCourseViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: YogaCourseViewHolder,
        position: Int
    ) {
        val courseItem = courseList[position]
        holder.binding.txtViewDayOfWeek.text = courseItem.dayOfWeek
        holder.binding.txtViewTimeOfCourse.text = courseItem.timeOfCourse
        holder.binding.txtViewTypeOfClass.text = courseItem.typeOfClass
    }

    override fun getItemCount(): Int {
       return courseList.size
    }

    inner class YogaCourseViewHolder(val binding: CourseItemViewBinding):
        RecyclerView.ViewHolder(binding.root)
}