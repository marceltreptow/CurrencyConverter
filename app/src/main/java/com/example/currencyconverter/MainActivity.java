package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner1;
    private Spinner spinner2;
    private Button calculatebutton;
    private TextView valueout;
    private EditText valuein;
    private ExchangeRateDatabase exchangeRateDatabase = new ExchangeRateDatabase();
    private Toolbar toolbar;
    private MenuItem item1;
    private MenuItem item2;
    private ShareActionProvider shareActionProvider;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner1 = findViewById(R.id.spinner);
        //ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,R.layout.layout_textview,R.id.my_text_view,list);
        CurrencyListAdapter adapter1 = new CurrencyListAdapter(exchangeRateDatabase.getCurrencies(),exchangeRateDatabase);
        spinner1.setAdapter(adapter1);
        spinner2 = findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter1);
        calculatebutton = findViewById(R.id.calculatebutton);
        valueout = findViewById(R.id.valueout);
        valuein = findViewById(R.id.valuein);
        prefs = getPreferences(Context.MODE_PRIVATE);

        /*toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        startActivity(new Intent(MainActivity.this, CurrencyListActivity.class));
                        break;
                }
                return true;
            }
        });

         */

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        item1 = findViewById(R.id.item1);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareText(null);
        item2 = findViewById(R.id.item1);
        return true;
    }

    private void setShareText(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (text != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        }
        shareActionProvider.setShareIntent(shareIntent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                startActivity(new Intent(MainActivity.this, CurrencyListActivity.class));
                break;
            case R.id.item2:
                updateCurrencies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view){
        if(view.getId() == R.id.calculatebutton) {
            double value = exchangeRateDatabase.convert(Double.parseDouble(valuein.getText().toString()),spinner1.getSelectedItem().toString().substring(0,3),(String) spinner2.getSelectedItem().toString().substring(0,3));
            valueout.setText(""+value);
            setShareText(""+value);

        }
    }

    public void updateCurrencies() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL u = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
                    URLConnection urlConnection = u.openConnection();

                    XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                    parser.setInput(urlConnection.getInputStream(), urlConnection.getContentEncoding());

                    int eventType = parser.getEventType();
                    int i = 0;

                    while(eventType != XmlPullParser.END_DOCUMENT){
                        if(eventType == XmlPullParser.START_TAG){
                            if("Cube".equals(parser.getName())){
                                String currency = parser.getAttributeValue(null,"currency");
                                if(currency != null)
                                    exchangeRateDatabase.setExchangeRate(currency, Double.parseDouble(parser.getAttributeValue(null, "rate")));

                            }
                        }
                        eventType = parser.next();
                    }
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }

            }
        });
        thread.start();

    }*/

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        //editor.putFloat("savein", Float.valueOf(String.valueOf(valuein.getText())));
        //editor.putString("saveout",valueout.getText().toString());
        editor.putString("saveout","hallo");
        //editor.putString("currencyin",(String)spinner1.getSelectedItem());
        //editor.putInt("currencyout",spinner2.getId());
        editor.apply();

    }



    @Override
    protected void onResume(){
        super.onResume();
        for (int i = 0; i < 100; i++)
            System.out.println("onResume");
        //valuein.setText(Float.toString(prefs.getFloat("savein",0)));
        String saveout = prefs.getString("saveout","");
        System.out.println("saveout = " + saveout);
        valueout.setText(saveout);
        //Arrays.asList(exchangeRateDatabase.getCurrencies()).indexOf((prefs.getString("currencyin","").substring(0,3))
        //spinner1.setSelection(Arrays.asList(exchangeRateDatabase.getCurrencies()).indexOf((prefs.getString("currencyin","").substring(0,3))));
        //spinner2.setSelection(prefs.getInt("currencyout",0));
        /*SharedPreferences prefs2 = getPreferences(Context.MODE_PRIVATE);
        String from = prefs2.getString("from", "");
        if (!from.equals("")) {
            valueout.setText(from);
        } else
            valueout.setText("Malte der Huan");*/
    }
}
