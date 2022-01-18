package com.example.mabar_v1.utility;

import android.content.Context;

public class GlobalMethod {

    SessionUser sessionUser;
    /*public String getUriImage(String path){
        //String url = "https://image.tmdb.org/t/p/w500"+path;
        //return url;
    }*/

    public String getKeyApi(){
        String keyApi = "2ca7c12f40da3aeb0502d71884939cc2";
        return keyApi;
    }

    public void deleteLocalSession(Context context){
        sessionUser = new SessionUser(context);
        sessionUser.clearSess();
    }

}
