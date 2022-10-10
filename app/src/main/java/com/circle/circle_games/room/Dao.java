package com.circle.circle_games.room;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.circle.circle_games.retrofit.model.DataItem;
import com.circle.circle_games.retrofit.model.ResponseListGame;

import java.util.List;

// Adding annotation
// to our Dao class
@androidx.room.Dao
public interface Dao {

    // below method is use to
    // add data to database.
    @Insert
    void insert(DataItem model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllGame(List<DataItem> list);

    // below method is use to update
    // the data in our database.
    @Update
    void update(DataItem model);

    // below line is use to delete a
    // specific course in our database.
    @Delete
    void delete(DataItem model);

    // on below line we are making query to
    // delete all courses from our database.
    @Query("DELETE FROM table_game")
    void deleteAllGame();

    // below line is to read all the courses from our database.
    // in this we are ordering our courses in ascending order
    // with our course name.
    @Query("SELECT * FROM table_game ORDER BY id ASC")
    LiveData<List<DataItem>> getAllGame();
}

