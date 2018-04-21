package com.apanteleeva.weatherapp;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

interface OnCustomAdapterClickListener{
    void removeView(int position);
    void editView(int position);
}

public class CustomElementsAdapter extends RecyclerView.Adapter<CustomElementsAdapter.CustomViewHolder> implements OnCustomAdapterClickListener{

    private List<City> cities;
    private LayoutInflater inflater;
    Context cnx;

    CustomElementsAdapter(Context context, List<City> myDataSet){
        this.cnx=context;
        this.cities = myDataSet;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CustomElementsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = inflater.inflate(R.layout.item_element, parent, false);
        CustomViewHolder vh = new CustomViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomElementsAdapter.CustomViewHolder holder, int position) {
        holder.bind(cities.get(position).getName(), this);

    }


    @Override
    public int getItemCount() {
        return cities.size();
    }

   /* public void addView(City newCity) {

        cities.add();
        notifyDataSetChanged();
    }
*/
    @Override
    public void removeView(int position) {
        //cities.remove(position);
        City city = cities.get(position);
        long id = city.getId();
        CityDataSource db = new CityDataSource(cnx);
        db.open();
        if (db.deleteCity(id)) {
            cities.remove(position);
        }else{
            Toast.makeText(cnx,"Unable to delete", Toast.LENGTH_SHORT).show();
        }
        db.close();
        this.notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void editView(int position) {
       // cities.set(position, "Edit");
        notifyItemChanged(position);
    }

    public void clear() {
        CityDataSource db = new CityDataSource(cnx);
        db.open();
        if (db.deleteAll()){
            cities.clear();
        }else{
            Toast.makeText(cnx,"Unable to delete all records", Toast.LENGTH_SHORT).show();
        }
        db.close();
        notifyDataSetChanged();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        TextView mTextView;
        TextView mContextView;

        private OnCustomAdapterClickListener callbacks;

        CustomViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.txtTitle);
            mContextView = v.findViewById(R.id.txtOptionDigit);
            mContextView.setOnClickListener(this);
        }

        void bind(String text, OnCustomAdapterClickListener callbacks) {
            this.callbacks = callbacks;
            mTextView.setText(text);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txtOptionDigit: {
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.getMenuInflater().inflate(R.menu.context_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(this);
                    popup.show();
                    break;
                }
            }
        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_edit:
                  if (callbacks!= null) callbacks.editView(getAdapterPosition());
                   return true;
                case R.id.menu_delete:
                    if (callbacks!= null) callbacks.removeView(getAdapterPosition());
                        return true;

                default:
                    return false;
            }
        }
    }

}
