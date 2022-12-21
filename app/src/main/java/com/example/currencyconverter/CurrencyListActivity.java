package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CurrencyListActivity extends AppCompatActivity {
    private ExchangeRateDatabase exchangeRateDatabase = new ExchangeRateDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_view);
        ListView listView = findViewById(R.id.my_list_view);
        String[] list = exchangeRateDatabase.getCurrencies();
        //ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,R.layout.layout_textview,R.id.my_text_view,list);
        //listView.setAdapter(adapter1);

        CurrencyListAdapter currencyListAdapter = new CurrencyListAdapter(exchangeRateDatabase.getCurrencies(),exchangeRateDatabase);
        listView.setAdapter(currencyListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = currencyListAdapter.getItem(i);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.de/maps/search/" + exchangeRateDatabase.getCapital(selected.substring(0, 3)))));
                //System.out.println(adapterView.getSelectedItem().getClass());
            }
        });

    }
}