package com.example.practicaparcial2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddActivity extends AppCompatActivity {

    private Button btnBuscar;
    private Button btnAdd;
    private EditText edtBuscar;
    private RequestQueue requestQueue;
    private TextView textViewCancion;
    private TextView textViewArtista;
    private TextView textViewUrl;
    private TextView textViewEncabezado;
    private TextView textCancion;
    private TextView textArtista;
    private TextView textUrl;

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        requestQueue = Volley.newRequestQueue(this);

        edtBuscar = (EditText) findViewById(R.id.edit_text_buscar);
        textViewCancion = (TextView) findViewById(R.id.text_view_cancion);
        textViewArtista = (TextView) findViewById(R.id.text_view_artista);
        textViewUrl = (TextView) findViewById(R.id.text_view_url);
        textViewEncabezado = (TextView) findViewById(R.id.encabezado);
        btnBuscar = (Button)findViewById(R.id.btn_buscar);
        btnAdd = (Button)findViewById(R.id.btn_add);
        textCancion = (TextView)findViewById(R.id.text_cancion);
        textArtista = (TextView)findViewById(R.id.text_artista);
        textUrl = (TextView)findViewById(R.id.text_url);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarCancion();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                añadirCancion();
            }
        });
    }

    private void buscarCancion(){
        String nameCancion = edtBuscar.getText().toString();
        if(nameCancion.equals("")){
            Toast.makeText(getApplicationContext(), "Escriba el nombre de la canción", Toast.LENGTH_SHORT).show();
        }else {
            String Url = "https://ws.audioscrobbler.com/2.0/?method=track.search&track=" + nameCancion +
                    "&api_key=b284db959637031077380e7e2c6f2775&format=json";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonResults = response.getJSONObject("results");
                        JSONObject jsonTrackmatches = jsonResults.getJSONObject("trackmatches");
                        JSONArray jsonArray = jsonTrackmatches.getJSONArray("track");

                        JSONObject jsonObject = jsonArray.optJSONObject(0);
                        textViewEncabezado.setText("Datos de la canción");
                        textCancion.setText("CANCIÓN");
                        textViewCancion.setText(jsonObject.getString("name"));
                        textArtista.setText("ARTISTA");
                        textViewArtista.setText(jsonObject.getString("artist"));
                        textUrl.setText("URL");
                        textViewUrl.setText(jsonObject.getString("url"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }
            );
            requestQueue.add(request);
        }
    }

    public void añadirCancion(){
        if(textViewCancion.getText() != ""){
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            intent.putExtra("name", textViewCancion.getText());
            intent.putExtra("artist", textViewArtista.getText());
            intent.putExtra("duration", "0");
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "No se pudo agregar la canción", Toast.LENGTH_LONG).show();
        }
    }

}
