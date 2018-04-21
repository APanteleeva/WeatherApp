package com.apanteleeva.weatherapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

///////////////////////////////////////////////////////////////////////////
// main Activity
///////////////////////////////////////////////////////////////////////////
public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView rv_list;
    CustomElementsAdapter adapter;
    CityDataSource citiesDataSource;
    List<City> elements;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv_list = findViewById(R.id.am_rv_city);
        rv_list.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.am_btn_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Button pressed", Toast.LENGTH_SHORT).show();
                //adapter.addView("new Item");
                addElement();
            }
        });

        citiesDataSource = new CityDataSource(getApplicationContext());
        citiesDataSource.open();
        //массив элементов для списка
        elements = citiesDataSource.getAllRecords();
        adapter = new CustomElementsAdapter(this, elements);
        rv_list.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_clear){
            //Toast.makeText(MainActivity.this,"Pressed", Toast.LENGTH_LONG).show();
            adapter.clear();
        }
        return super.onOptionsItemSelected(item);
    }

   /* private List<String> getData(List<City> list){
        List<String> res = new ArrayList<>();
        for(int i=0;i<list.size();i++) {
            res.add(list.get(i).toString());
        }
        return res;
    }*/

    private void addElement() {
        // без этого блока в OnClickListener() не получить доступ к нашему EditText
        ///
        LayoutInflater factory = LayoutInflater.from(this);
        // final - для того чтобы использовать его в OnClickListener()
        final View alertView = factory.inflate(R.layout.layout_add_note, null);
        ///
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertView);
        builder.setTitle(R.string.alert_title_add);
        builder.setNegativeButton(R.string.alert_cancel, null);
        builder.setPositiveButton(R.string.menu_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText editTextNote = (EditText) alertView.findViewById(R.id.editTextNote);
                //0EditText editTextNoteTitle = (EditText) alertView.findViewById(R.id.editTextNoteTitle);
                // если использовать findViewById без alertView то всегда будем получать editText = null
                citiesDataSource.addCity(editTextNote.getText().toString());
                dataUpdated();

            }
        });
        builder.show();
    }

    private void editElement(int id) {
        citiesDataSource.editCity(elements.get(id).getId(), "Edited");
        dataUpdated();
    }

    private void deleteElement(int id) {
        citiesDataSource.deleteCity(id);
        dataUpdated();
    }

    private void dataUpdated() {
        elements.clear();
        elements.addAll(citiesDataSource.getAllRecords());
        adapter.notifyDataSetChanged();
        rv_list.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        citiesDataSource.close();
        super.onDestroy();
    }
}
