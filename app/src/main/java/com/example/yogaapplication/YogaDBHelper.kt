package com.example.yogaapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.annotation.IntegerRes

class YogaDBHelper(context: Context): SQLiteOpenHelper(context,"yoga.db",null,1) {
    private val courseTableName ="course"
    private val id="id"
    private val dayOfWeek ="day_of_week"
    private val timeOfCourse = "time_of_course"
    private val capacity ="capacity"
    private val duration ="duration"
    private val price = "price"
    private val typeOfClass = "type_of_class"
    private val description ="description"

    private val classTableName = "class"
    private val dateOfClass ="date_of_class"
    private val teacher ="teacher"
    private val comments = "comments"

    private val courseID ="course_id"

    override fun onCreate(db: SQLiteDatabase?) {

        val sqlCourse ="create table $courseTableName ($id integer primary key autoincrement, " +
                "$dayOfWeek varchar(20)," +
                "$timeOfCourse varchar(10)," +
                "$capacity varchar(5)," +
                "$duration varchar(5)," +
                "$price varchar(10)," +
                "$typeOfClass varchar(50)," +
                "$description text" +
        ")"

        db?.execSQL(sqlCourse)

        val sqlClass ="create table $classTableName ($id integer primary key autoincrement, " +
                "$dateOfClass varchar(20)," +
                "$teacher varchar(10)," +
                "$comments text," +
                "$courseID integer, "+
                "foreign key ($courseID) references $courseTableName($id) on delete cascade"+
                ")"

        db?.execSQL(sqlClass)

        Log.i("Yoga DB","Two Tables Created Successfully!")
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        val sqlCourse = "drop table if exists $courseTableName"
        db?.execSQL(sqlCourse)

        val sqlClass = "drop table if exists $classTableName"
        db?.execSQL(sqlClass)

        onCreate(db)

        Log.i("Yoga DB","Two Tables Upgraded Successfully!")
    }

    public fun saveCourse (ypgaCourse: YogaCourse):Long{
        Log.i("Yoga DB","******Save Course*****")
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(dayOfWeek,ypgaCourse.dayOfWeek)
        values.put(timeOfCourse,ypgaCourse.timeOfCourse)
        values.put(capacity,ypgaCourse.capacity)
        values.put(duration,ypgaCourse.duration)
        values.put(price,ypgaCourse.price)
        values.put(typeOfClass,ypgaCourse.typeOfClass)
        values.put(description,ypgaCourse.description)
        val result =  db.insert(courseTableName,null,values)
        db.close()
        return  result
    }
    fun getAllCourses(): List<YogaCourse> {
        val courseList = mutableListOf<YogaCourse>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $courseTableName"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val cId = cursor.getInt(cursor.getColumnIndexOrThrow(id))
                val day = cursor.getString(cursor.getColumnIndexOrThrow(dayOfWeek))
                val time = cursor.getString(cursor.getColumnIndexOrThrow(timeOfCourse))
                val capacity = cursor.getString(cursor.getColumnIndexOrThrow(capacity))
                val duration = cursor.getString(cursor.getColumnIndexOrThrow(duration))
                val price = cursor.getString(cursor.getColumnIndexOrThrow(price)) ?: "0"
                val typeOfClass = cursor.getString(cursor.getColumnIndexOrThrow(typeOfClass))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(description))

                val yogaCourseObj = YogaCourse(cId,day,time,capacity,duration,price,typeOfClass,description)
                courseList.add(yogaCourseObj)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return courseList
    }

    fun editCourse (yogaCourse: YogaCourse):Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(id,yogaCourse.id)
        values.put(dayOfWeek,yogaCourse.dayOfWeek)
        values.put(timeOfCourse,yogaCourse.timeOfCourse)
        values.put(capacity,yogaCourse.capacity)
        values.put(duration,yogaCourse.duration)
        values.put(price,yogaCourse.price)
        values.put(typeOfClass,yogaCourse.typeOfClass)
        values.put(description,yogaCourse.description)
       val result = db.update(courseTableName,values,"$id=?",
           arrayOf(yogaCourse.id.toString()))
        db.close()
        Log.i("Yoga Db","Updated Record : $result")
        return  result
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    fun deleteCourse(courseID: Int):Int{
        val db = this.writableDatabase
       val result = db.delete(courseTableName,"$id=?",arrayOf(courseID.toString()))
        db.close()
        Log.i("Yoga Db","Deleted Record : $result")
        return result
    }

    fun resetCourse():Int{
        val db = this.writableDatabase
        val result = db.delete(courseTableName,null,null)
        db.close()
        return result
    }

    fun saveClass(yogaClass: YogaClass): Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.apply {
            put(dateOfClass,yogaClass.dateOfClass)
            put(teacher,yogaClass.teacher)
            put(comments,yogaClass.comments)
            put(courseID,yogaClass.courseID)
        }
        val result = db.insert(classTableName,null,values)
        return result
    }

    fun showCourseDetails(id: Int):List<YogaClass>{
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $classTableName WHERE $courseID = ?",
            arrayOf(id.toString())
        )
        val classList = mutableListOf<YogaClass>()
        if(cursor.moveToFirst()){
            do{
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(dateOfClass))
                val teacher = cursor.getString(cursor.getColumnIndexOrThrow(teacher))
                val comment = cursor.getString(cursor.getColumnIndexOrThrow(comments))
                val cID = cursor.getInt(cursor.getColumnIndexOrThrow(courseID))

                classList.add(
                    YogaClass(id,date,teacher,comment,cID)
                )
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return  classList
    }
    fun getAllClass(): List<YogaClass> {
        val classList = mutableListOf<YogaClass>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $classTableName",
            null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(id))
                val dateOfClass = cursor.getString(cursor.getColumnIndexOrThrow(dateOfClass))
                val teacher = cursor.getString(cursor.getColumnIndexOrThrow(teacher))
                val comments = cursor.getString(cursor.getColumnIndexOrThrow(comments))
                val courseId = cursor.getInt(cursor.getColumnIndexOrThrow(courseID))

                val classItem = YogaClass(id, dateOfClass, teacher, comments, courseId)
                classList.add(classItem)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return classList
    }

    fun deleteClass(classID: Int):Int{
        val db = this.writableDatabase
        val result = db.delete(classTableName,"$id=?",arrayOf(classID.toString()))
        db.close()
        Log.i("Yoga Db","Deleted Record : $result")
        return result
    }

    fun editClass(yogaClass: YogaClass ): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(dateOfClass, yogaClass.dateOfClass)
            put(teacher, yogaClass.teacher)
            put(comments, yogaClass.comments)
            put(courseID, yogaClass.courseID)
        }

        // update the row where id matches
        val result = db.update(
            classTableName,
            values,
            "id = ?",
            arrayOf(yogaClass.id.toString())
        )

        db.close()
        return result  // returns number of rows updated
    }

    fun getClassById(clID: Int): YogaClass? {
        val db = this.readableDatabase
        var result: YogaClass? = null

        val cursor = db.rawQuery(
            "SELECT * FROM $classTableName WHERE id = ?",
            arrayOf(clID.toString())
        )

        if (cursor.moveToFirst()) {
            result = YogaClass(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(id)),
                dateOfClass = cursor.getString(cursor.getColumnIndexOrThrow(dateOfClass)),
                teacher = cursor.getString(cursor.getColumnIndexOrThrow(teacher)),
                comments = cursor.getString(cursor.getColumnIndexOrThrow(comments)),
                courseID = cursor.getInt(cursor.getColumnIndexOrThrow(courseID))
            )
        }

        cursor.close()
        db.close()
        return result
    }

    fun getCourseById(courseID: Int): YogaCourse? {
        val db = this.readableDatabase
        var course: YogaCourse? = null

        val cursor = db.rawQuery(
            "SELECT * FROM $courseTableName WHERE id = ?",
            arrayOf(courseID.toString())
        )
        if (cursor.moveToFirst()) {
            course = YogaCourse(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(id)),
                dayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(dayOfWeek)),
                timeOfCourse = cursor.getString(cursor.getColumnIndexOrThrow(timeOfCourse)),
                capacity = cursor.getString(cursor.getColumnIndexOrThrow(capacity)),
                duration = cursor.getString(cursor.getColumnIndexOrThrow(duration)),
                price = cursor.getString(cursor.getColumnIndexOrThrow(price)),
                typeOfClass = cursor.getString(cursor.getColumnIndexOrThrow(typeOfClass)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(description))
            )
        }

        cursor.close()
        db.close()
        return course
    }



}