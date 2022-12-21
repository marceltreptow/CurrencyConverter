package com.example.currencyconverter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Map;

public class CurrencyListAdapter extends BaseAdapter {
    private String[] currencys;
    private ExchangeRateDatabase exchangeRateDatabase;

    public CurrencyListAdapter(String[] strings, ExchangeRateDatabase exchangeRateDatabase){
        this.currencys = strings;
        this.exchangeRateDatabase = exchangeRateDatabase;

    }
    /*public int getPos(String string){
        return currenc
    }

     */

    @Override
    public int getCount() {
        return currencys.length;
    }

    @Override
    public String getItem(int i) {
        return currencys[i] + ": " + exchangeRateDatabase.getExchangeRate(currencys[i]);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        String entry = getItem(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_textview_imageview, null, false);
        }
        TextView textView = (TextView)view.findViewById(R.id.textView_it_item);
        textView.setText(entry);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageview_it_item);

        int imageId = context.getResources().getIdentifier("flag_"+currencys[i].toLowerCase(),"drawable", context.getPackageName());
        imageView.setImageResource(imageId);
        return view;
    }
}
