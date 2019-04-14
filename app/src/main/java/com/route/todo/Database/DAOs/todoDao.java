package com.route.todo.Database.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.route.todo.Database.Model.Todo;

import java.util.List;

@Dao
public interface todoDao{

@Insert
    public void Additem(Todo item);

@Delete
public void deleteitem(Todo item);

@Update
public void updateitem(Todo item);

@Query("Select * From Todo;")
public List<Todo> getalltodo();

@Query("Select * from Todo Where Id=:id;")
    Todo searchbyid(int id);
}
