package com.route.todo.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.route.todo.Database.DAOs.todoDao;
import com.route.todo.Database.Model.Todo;

@Database(entities = {Todo.class},version = 2,exportSchema = false)
public abstract class Mydatabase extends RoomDatabase {


    public abstract todoDao todoDaoobj();
    private static Mydatabase mydatabase;

    public static Mydatabase getInstance(Context context)
    { if(mydatabase==null)
    { mydatabase= Room.databaseBuilder(context.getApplicationContext(),Mydatabase.class,"database")
                .allowMainThreadQueries().build();}
           return mydatabase;
    }
}
