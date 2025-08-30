package com.example.yogaapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

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

}