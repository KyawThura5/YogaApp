package com.example.yogaapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yogaapplication.databinding.ClassItemViewBinding

class YogaClassAdapter(val classList:List<YogaClass>,
                        val onEditClassClick:(YogaClass)-> Unit,
                        val onDeleteClassClick:(Int)-> Unit,
                        val onShowCourseDetailsFromClass: (Int) -> Unit,
): RecyclerView.Adapter<YogaClassAdapter.YogaClassViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): YogaClassViewHolder {
        val binding = ClassItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return YogaClassViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: YogaClassViewHolder,
        position: Int
    ) {
        val classItem = classList[position]
       holder.binding.apply {
           txtViewDateofClass.text = classItem.dateOfClass
           txtViewTeacherOfClass.text = classItem.teacher
           txtViewCommentsOfClass.text = classItem.comments

          btnEditClass.setOnClickListener {
               onEditClassClick(classItem)
           }

           btnDeleteClass.setOnClickListener {
               onDeleteClassClick(classItem.id)
           }

          btnShowCoursesFromClass.setOnClickListener {
               onShowCourseDetailsFromClass(classItem.id)
           }
       }
    }

    override fun getItemCount(): Int {
        return classList.size
    }

    inner class YogaClassViewHolder(val binding: ClassItemViewBinding):
        RecyclerView.ViewHolder(binding.root)
}