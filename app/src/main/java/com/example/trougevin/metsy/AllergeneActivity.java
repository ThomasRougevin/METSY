package fr.isep.ii3510.assignment3musiclibrary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AllergeneActivity extends Activity {

    @BindView(R.id.allerg_list) ListView allergList;
    @BindView(R.id.textView) TextView textview;
    private ArrayList menuList = new ArrayList<>(), // link to ListView
            AllergeneList = new ArrayList<>(),  //list of allergenes in CSV file
            selected = new ArrayList<>(); //list of selected allergenes. TO COMPARE WITH SCANED FOOD INGREDIENTS
    public String currentAllergene; // linebyline

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band);

        ButterKnife.bind(AllergeneActivity.this);

        allergList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        menuList = AllergeneList;


        ArrayAdapter<String> adapter = new ArrayAdapter<>( //creating adapter to bind the array to the listView
                AllergeneActivity.this,                           //concerned application
                android.R.layout.simple_list_item_activated_1,    //where it must be displayed
                (List<String>) menuList);                          //to be displayed


        //READING OF CSV FILE
        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.allergenes2));
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            SampleReadFile sample = new SampleReadFile();
            sample.setAllerg(line);
            if (AllergeneList.contains(sample.getAllerg()) == false) {
                AllergeneList.add(line);
            }
        }



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
    }

    public void ViewSelectedItems(View view) {
        //String item ="";
 /*     for(Object item:selected)
        {
            currentAllergene += item.toString();
            selected.add(item);
        }*/

        //Toast.makeText(this, currentAllergene, Toast.LENGTH_LONG);
        //Toast.makeText(this.currentAllergene,Toast.LENGTH_LONG)
        textview.setText(selected.toString());
    }
}
