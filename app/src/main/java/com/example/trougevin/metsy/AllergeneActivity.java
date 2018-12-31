package com.example.trougevin.metsy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Scanner;



public class AllergeneActivity extends Activity {
    EditText search;
   ListView allergList;
   TextView textview;
   String [] items;

   User user;

    // link to ListView
    private ArrayList<String> AllergeneList = new ArrayList<>();  //list of allergenes in CSV file
    private ArrayList<String> selected = new ArrayList<>(); //list of selected allergenes. TO COMPARE WITH SCANED FOOD INGREDIENTS
    public String currentAllergene; // linebyline
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergene);

        allergList = findViewById(R.id.allerg_list);
        textview = findViewById(R.id.textView);
        search = findViewById(R.id.searchView);


        allergList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayList<String> menuList = AllergeneList;

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (AllergeneActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("userInfo");
            int taille = user.getAllergens().size();
            for(String i : user.getAllergens()){
                if(!selected.contains(i)){ //changer selecter par AllergeneList p-ê ?
                    selected.add(i);
                }
                else{
                    selected.remove(i);
                }
            }
        }

        adapter = new ArrayAdapter<>( //creating adapter to bind the array to the listView
                AllergeneActivity.this,                           //concerned application
                android.R.layout.simple_list_item_activated_1,    //where it must be displayed
                menuList);                          //to be displayed


        //READING OF CSV FILE
        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.allergens));
        // scanner.nextLine();
        //while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] col = line.split(",");



        for (String i : col)
        {
            if (!AllergeneList.contains(i)) {
                AllergeneList.add(i);

                Serveur serveur = new Serveur();
                try {
                    serveur.send_2(user.getName(), user.getPassword(), "check", i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            else{
                AllergeneList.remove(i);
            }
        }

              /*  SampleReadFile sample = new SampleReadFile();
                sample.setAllerg(line);*/
                /*if (!AllergeneList.contains(sample.getAllerg())) {
                    AllergeneList.add(line);
                }*/
        //}



        allergList.setAdapter(adapter);
        allergList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentAllergene = ((TextView)view).getText().toString();

                if(selected.contains(currentAllergene)){
                    selected.remove(currentAllergene);
                }
                else{

                    selected.add(currentAllergene);


                }
            }
        });

/*        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equals("")){
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    public void ViewSelectedItems(View view) {

        //textview.setText(selected.toString());


         Intent intent = new Intent(view.getContext(), BarcodeScanner.class);
         intent.putExtra("AllergenSelected", selected.toString());
         startActivity(intent);
    }

   /*
    public void searchItem(String textToSearch){
        for(String item:items){
            if(!item.contains(textToSearch)){
                AllergeneList.remove(item);
                adapter.notifyDataSetChanged();
            }
        }

    }*/

}

