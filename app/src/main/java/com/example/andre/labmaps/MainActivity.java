package com.example.andre.labmaps;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends ListActivity {
    private ArrayAdapter<String> adapter;
    private List<String> wordList;
    private CountryDataSource datasource;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datasource = new CountryDataSource(this);
        datasource.open();

        wordList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                wordList);

        setListAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void getData(View view) {
        ApiClient.getCountryClient().getCountries().enqueue(new Callback<List<Country>>() {
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response ){
                if (response.isSuccessful() ) {
                    List<Country> countries = response.body();
                    datasource.clearDatabase();
                    for (Country country : countries) {
                        country.setLatitudeAndLongitude(country.getLatlng());
                        datasource.createCountry(country.getName(),country.getLatitude(),country.getLongitude());
                        wordList.add(country.getName()+":["+country.getLatitude()+","+country.getLongitude() +"]");
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext() , "Banco atualizado", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(response.errorBody());
                    Toast.makeText(getApplicationContext() , "Erro encontrado", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void seeCountry(View view) {
        EditText countryToFind = (EditText) findViewById(R.id.edittext);
        String countryName = countryToFind.getText().toString().trim();
        boolean found = false;
        Country country = datasource.getCountry(countryName);
        if(country != null){
            Intent intent = new Intent(getBaseContext(), MapsActivity.class);
            intent.putExtra("LATITUDE",country.getLatitude());
            intent.putExtra("LONGITUDE",country.getLongitude());
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext() , "Pais não encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}





