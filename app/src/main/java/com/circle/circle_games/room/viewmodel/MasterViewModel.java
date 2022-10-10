package com.circle.circle_games.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.circle.circle_games.retrofit.model.DataItem;
import com.circle.circle_games.room.repository.GameRepository;

import java.util.List;

public class MasterViewModel extends AndroidViewModel {

    // creating a new variable for course repository.
    private GameRepository repository;

    // below line is to create a variable for live
    // data where all the courses are present.
    private LiveData<List<DataItem>> allGame;

    // constructor for our view modal.
    public MasterViewModel(@NonNull Application application) {
        super(application);
        repository = new GameRepository(application);
        allGame = repository.getAllGame();
    }

    // below method is use to insert the data to our repository.
    public void insert(DataItem model) {
        repository.insert(model);
    }

    /*public void insertAllGame(List<DataItem> listGame){
        repository.insertAll(listGame);
    }*/

    // below line is to update data in our repository.
    public void update(DataItem model) {
        repository.update(model);
    }

    // below line is to delete the data in our repository.
    public void delete(DataItem model) {
        repository.delete(model);
    }

    // below method is to delete all the courses in our list.
    public void deleteAllGame() {
        repository.deleteAllGame();
    }

    // below method is to get all the courses in our list.
    public LiveData<List<DataItem>> getAllGame() {
        return allGame;
    }
}

