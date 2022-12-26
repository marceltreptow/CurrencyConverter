package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.Editable;
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
    private TextView valuein;
    private ExchangeRateDatabase exchangeRateDatabase = new ExchangeRateDatabase();
    private Toolbar toolbar;
    private MenuItem item1;
    private MenuItem item2;
    private ShareActionProvider shareActionProvider;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OmdbDbHelper dbHelper = new OmdbDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                OmdbDbHelper.OMDB_COL_NAME,
                OmdbDbHelper.OMDB_COL_RATE
        };
        Cursor c = db.query(OmdbDbHelper.OMDB_TABLE, projection, null, null,
                null, null, null);
        while (c.moveToNext()) {
            try {
                exchangeRateDatabase.setExchangeRate(c.getString(c.getColumnIndexOrThrow(OmdbDbHelper.OMDB_COL_NAME)), Double.parseDouble(c.getString(c.getColumnIndexOrThrow(OmdbDbHelper.OMDB_COL_RATE))));
            }catch (Exception e) {
                System.out.println(e.getCause());
            }
        }
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


    }

    @Override
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
            double value = 0.0;
            try {
                value = exchangeRateDatabase.convert(Double.parseDouble(valuein.getText().toString()), spinner1.getSelectedItem().toString().substring(0, 3), (String) spinner2.getSelectedItem().toString().substring(0, 3));
            }catch (Exception e){
                System.out.println(e.getCause());
            }
            valueout.setText(""+value);
            setShareText(""+value);

        }
    }

    public void updateCurrencies() {
        OmdbDbHelper dbHelper = new OmdbDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(OmdbDbHelper.OMDB_TABLE,null,null);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL u = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
                    URLConnection urlConnection = u.openConnection();
                    XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                    parser.setInput(urlConnection.getInputStream(), urlConnection.getContentEncoding());

                    int eventType = parser.getEventType();

                    while(eventType != XmlPullParser.END_DOCUMENT){
                        if(eventType == XmlPullParser.START_TAG){
                            if("Cube".equals(parser.getName())){
                                String currency = parser.getAttributeValue(null,"currency");
                                if(currency != null) {
                                    exchangeRateDatabase.setExchangeRate(currency, Double.parseDouble(parser.getAttributeValue(null, "rate")));
                                    ContentValues values = new ContentValues();
                                    values.put(OmdbDbHelper.OMDB_COL_NAME, currency);
                                    values.put(OmdbDbHelper.OMDB_COL_RATE, (parser.getAttributeValue(null, "rate")));
                                    db.insert(OmdbDbHelper.OMDB_TABLE, null, values);
                                }
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
        /*OmdbDbHelper dbHelper = new OmdbDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OmdbDbHelper.OMDB_COL_NAME, "AUD");
        values.put(OmdbDbHelper.OMDB_COL_RATE, "187");
        long newRowId = db.insert(OmdbDbHelper.OMDB_TABLE, null, values);
         */
        /*OmdbDbHelper dbHelper = new OmdbDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OmdbDbHelper.OMDB_COL_RATE, "42");
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { "1" };
        db.update(OmdbDbHelper.OMDB_TABLE, values, selection, selectionArgs);
         */
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("savein",Float.valueOf(valuein.getText().toString()));
        editor.putString("saveout",valueout.getText().toString());
        editor.putString("currencyin",spinner1.getSelectedItem().toString());
        editor.putString("currencyout",spinner2.getSelectedItem().toString());
        editor.apply();

    }



    @Override
    protected void onResume(){
        super.onResume();
        String valueinstring = Float.toString(prefs.getFloat("savein",0));
        if(valueinstring.equals(""))
            valuein.setText("0.0");
        else
            valuein.setText(valueinstring);
        valueout.setText(prefs.getString("saveout",""));
        spinner1.setSelection(Arrays.asList(exchangeRateDatabase.getCurrencies()).indexOf((prefs.getString("currencyin","").substring(0,3))));
        spinner2.setSelection(Arrays.asList(exchangeRateDatabase.getCurrencies()).indexOf((prefs.getString("currencyout","").substring(0,3))));

    }
}
