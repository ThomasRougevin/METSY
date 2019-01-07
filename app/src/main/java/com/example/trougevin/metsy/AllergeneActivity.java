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

        int j=0;

        //GETTING USER'S PROFILE
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("userInfo");
            int taille = user.getAllergens().size();
            for(String i : user.getAllergens()){
                if(!selected.contains(i)){
                    selected.add(i);
                }
                else{
                    selected.remove(i);
                }
            }
        }


        //GETTING ALLERGENS PREVIOUSLY SELECTED
        for (String i:user.getAllergens())
        {
            currentAllergene = i ;
            selected.add(i);
            if(AllergeneList.contains(i)){
             //   AllergeneList.i.getPostion;
            }
            j++;
        }


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


        adapter = new ArrayAdapter<>( //creating adapter to bind the array to the listView
                AllergeneActivity.this,                           //concerned application
                android.R.layout.simple_list_item_activated_1,    //where it must be displayed
                menuList);                          //to be displayed


        //READING OF CSV FILE
        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.allergens));
        String line = scanner.nextLine();
        String[] col = line.split(",");

        //COPY OF CSV FILE IN LIST VIEW
        for (String i : col)
        {
            if (!AllergeneList.contains(i)) {
                AllergeneList.add(i);
            }

            else{
                AllergeneList.remove(i);
            }
        }


        allergList.setAdapter(adapter);
        allergList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentAllergene = ((TextView)view).getText().toString();
                Serveur serveur = new Serveur ();

                try {
                    if (!serveur.send_2(user.getName(),user.getPassword(),"check", currentAllergene)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(AllergeneActivity.this, R.style.AlertDialogStyle);
                        builder.setTitle("ERROR");
                        builder.setMessage(currentAllergene);
                        AlertDialog alertDialog = builder.create();
                        if(!alertDialog.isShowing()){
                            alertDialog.show();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(selected.contains(currentAllergene)){
                    selected.remove(currentAllergene);
                }
                else{
                    selected.add(currentAllergene);
                }
            }
        });
    }

    public void ViewSelectedItems(View view) {
        //textview.setText(selected.toString());
         Intent intent = new Intent(view.getContext(), BarcodeScanner.class);
         intent.putExtra("AllergenSelected", selected.toString());
         startActivity(intent);
    }

    public void monprofil_clk(View view) {
        Intent intent = new Intent (AllergeneActivity.this, ProfilePage.class);
        Bundle b = new Bundle();
        b.putParcelable("userInfo",user);
        intent.putExtras(b);
        startActivity(intent);
    }

}