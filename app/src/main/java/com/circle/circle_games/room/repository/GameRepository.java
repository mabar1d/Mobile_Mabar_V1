package com.circle.circle_games.room.repository;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.circle.circle_games.retrofit.model.DataItem;
import com.circle.circle_games.room.Dao;
import com.circle.circle_games.room.database.GameDatabase;

import java.util.List;

public class GameRepository {

    // below line is the create a variable
    // for dao and list for all game.
    private Dao dao;
    private LiveData<List<DataItem>> allGame;

    // creating a constructor for our variables
    // and passing the variables to it.
    public GameRepository(Application application) {
        GameDatabase database = GameDatabase.getInstance(application);
        dao = database.Dao();
        allGame = dao.getAllGame();
    }

    // creating a method to insert the data to our database.
    public void insert(DataItem model) {
        new InsertGameAsyncTask(dao).execute(model);
    }

   /* public void insertAll(List<DataItem> dataItems){
        new InsertAllGameAsyncTask(dao).execute(dataItems);
    }*/

    // creating a method to update data in database.
    public void update(DataItem model) {
        new UpdateGameAsyncTask(dao).execute(model);
    }

    // creating a method to delete the data in our database.
    public void delete(DataItem model) {
        new DeleteGameAsyncTask(dao).execute(model);
    }

    // below is the method to delete all the game.
    public void deleteAllGame() {
        new DeleteAllGameAsyncTask(dao).execute();
    }

    // below method is to read all the game.
    public LiveData<List<DataItem>> getAllGame() {
        return allGame;
    }

    // we are creating a async task method to insert new game.
    private static class InsertGameAsyncTask extends AsyncTask<DataItem, Void, Void> {
        private Dao dao;

        private InsertGameAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(DataItem... model) {
            // below line is use to insert our modal in dao.
            dao.insert(model[0]);
            return null;
        }
    }

    /*private static class InsertAllGameAsyncTask extends AsyncTask<List<DataItem>, Void, Void> {
        private Dao dao;

        private InsertAllGameAsyncTask(Dao dao) {
            this.dao = dao;
        }

        *//*@Override
        protected Void doInBackground(List<DataItem> model) {
            // below line is use to insert our modal in dao.
            dao.insertAllGame(model);
            return null;
        }*//*

        @Override
        protected Void doInBackground(List<DataItem>... lists) {
            dao.insertAllGame(lists);
            return null;
        }
    }*/

    // we are creating a async task method to update our game.
    private static class UpdateGameAsyncTask extends AsyncTask<DataItem, Void, Void> {
        private Dao dao;

        private UpdateGameAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(DataItem... models) {
            // below line is use to update
            // our modal in dao.
            dao.update(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete game.
    private static class DeleteGameAsyncTask extends AsyncTask<DataItem, Void, Void> {
        private Dao dao;

        private DeleteGameAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(DataItem... models) {
            // below line is use to delete
            // our course modal in dao.
            dao.delete(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all game.
    private static class DeleteAllGameAsyncTask extends AsyncTask<Void, Void, Void> {
        private Dao dao;
        private DeleteAllGameAsyncTask(Dao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method
            // to delete all game.
            dao.deleteAllGame();
            return null;
        }
    }
}
