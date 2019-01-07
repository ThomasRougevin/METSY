package com.example.trougevin.metsy;

public class Serveur {

    String ip;
    int port;

    String message;
    public String retour;

    int wait_time;
    int wait_iteration;


    //////////////////////////////////////////////////////////////////
    //
    //      Constructeur
    //
    //////////////////////////////////////////////////////////////////

    public Serveur (){
        ip = "80.11.106.244";
        port = 666;

        wait_iteration = 100;
        wait_time = 50;
    }
    public Serveur (String val){
        ip = val;
        port = 666;

        wait_iteration = 100;
        wait_time = 50;
    }
    public Serveur (int val){
        ip = "80.11.106.244";
        port = val;
    }
    public Serveur (String val, int val2){
        ip = val;
        port = val2;

        wait_iteration = 100;
        wait_time = 50;
    }

    //////////////////////////////////////////////////////////////////
    //
    //      getteur / setteur
    //
    //////////////////////////////////////////////////////////////////

    public void set_ip (String val){
        this.ip = val;
    }
    public void set_port (int val){
        this.port = val;
    }
    public void set_wait_time (int val){
        this.wait_time = val;
    }
    public void set_wait_iteration (int val){
        this.wait_iteration = val;
    }
    //public void set_retour (String val){
    //    this.retour = val;
    //}

    public String get_ip (){
        return this.ip;
    }
    public int get_port (){
        return this.port;
    }
    public int get_wait_time (){
        return this.wait_time;
    }
    public int get_wait_iteration (){
        return this.wait_iteration;
    }
    public String get_retour (){
        return this.retour;
    }

    //////////////////////////////////////////////////////////////////
    //
    //      Fonctions
    //
    //////////////////////////////////////////////////////////////////

    public String send_1 (String login, String password, String cmd, String arg) throws InterruptedException {

        String mes = ""+login+" "+password+" "+cmd+" "+arg+"\0";
        int compte = 0;

        Client client = new Client (ip,port,mes);
        client.execute();
        client.get_message = "none";

        while (client.get_message.compareTo("none")==0){
            Thread.sleep(100);
            if (compte > 100)
                break;
            compte++;
        }

        return client.get_message;
    }
    public boolean send_2 (String login, String password, String cmd, String arg) throws InterruptedException {

        String mes = ""+login+" "+password+" "+cmd+" "+arg+"\0";
        int compte = 0;

        Client client = new Client (ip,port,mes);
        client.execute();
        client.get_message = "none";

        while (client.get_message.compareTo("none")==0){
            Thread.sleep(100);
            if (compte > 20)
                break;
            compte++;
        }

        if (client.get_message.compareTo("none")==0)
            return false;

        String ret = client.get_message.substring(0,7);
        //if (cmd.compareTo("get") != 0)
        retour = client.get_message;

        if (ret.compareTo("FAILLED")==0)
            return false;
        return true;
    }
}
