package com.example.trougevin.metsy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Serveur {

    private String ip;
    private int port;

    private String message;

    ////////////////////////////////////////////////////////////////////////////////
    //
    //      CONSTRUCTEURS
    //
    ////////////////////////////////////////////////////////////////////////////////

    public Serveur (){
        ip = "80.11.106.244";
        port = 666;
    }
    public Serveur (String nip){
        ip = nip;
        port = 666;
    }
    public Serveur (int nport){
        ip = "80.11.106.244";
        port = nport;
    }
    public Serveur (String nip, int nport){
        ip = nip;
        port = nport;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //
    //      GET / SET
    //
    ////////////////////////////////////////////////////////////////////////////////

    public String GetIp (){
        return ip;
    }
    public int GetPort(){
        return port;
    }
    public String GetMessage(){
        return message;
    }

    public void SetIp (String val){
        ip = val;
    }
    public void SetPort(int val){
        port = val;
    }
    public void SetMessage(String val){
        message = val;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //
    //      FONCTIONS
    //
    ////////////////////////////////////////////////////////////////////////////////

    public String send (String login, String password, String cmd, String arg){

        String line = login+" "+password+" "+cmd+" "+arg+"\0";
        String ret = "none";

        Client myClient = new Client("80.11.106.244", 666, ret, line);
        myClient.execute();

        return ret;
    }
}
