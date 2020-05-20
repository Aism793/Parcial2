package com.example.practicaparcial2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private RecyclerView recycler;
    private AdapterDatos adapterDatos;
    private ArrayList<Cancion> listCanciones;
    private static ArrayList<Cancion> listaAuxiliar = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        listCanciones = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        procesarDatos();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        if(getIntent().getStringExtra("name") != null){
            Cancion cancion = new Cancion();
            cancion.nombre = getIntent().getStringExtra("name");
            cancion.artista = getIntent().getStringExtra("artist");
            cancion.duracion = getIntent().getStringExtra("duration");
            listaAuxiliar.add(cancion);
            for(Cancion item: listaAuxiliar){
                listCanciones.add(item);
            }


        }
    }


    private void procesarDatos(){
        String url = "http://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&api_key=b284db959637031077380e7e2c6f2775&format=json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject json = response.getJSONObject("tracks");
                    JSONArray mJsonArray = json.getJSONArray("track");

                    Cancion cancion;
                    //listCanciones = new ArrayList<>();
                    for (int i = 0; i < mJsonArray.length(); i++){
                        cancion = new Cancion();
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        cancion.nombre = mJsonObject.getString("name");
                        cancion.duracion = mJsonObject.getString("duration");
                        JSONObject artista = mJsonObject.getJSONObject("artist");
                        cancion.artista = artista.getString("name");

                        listCanciones.add(cancion);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                TextView title = (TextView) findViewById(R.id.text_view_title);
                title.setText("MI LISTA DE CANCIONES\n"+ listCanciones.size() + " canciones");
                adapterDatos = new AdapterDatos(listCanciones);
                recycler.setAdapter(adapterDatos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        );
        requestQueue.add(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
