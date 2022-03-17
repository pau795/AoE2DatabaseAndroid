package com.aoedb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aoedb.R;

import java.util.ArrayList;
import java.util.List;

public class TextFilterAdapter extends ArrayAdapter<String> implements Filterable{
    Context context;
    int textViewResourceId;
    List<String> items, tempItems, suggestions;

    public TextFilterAdapter(Context context, int textViewResourceId, List<String> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.autocomplete_text_layout, parent, false);
        }
        String names = items.get(position);
        if (names != null) {
            TextView lblName = view.findViewById(R.id.autocomplete_item);
            if (lblName != null)
                lblName.setText(names);
        }
        return view;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((String) resultValue);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (String names : tempItems) {
                        if (names.toLowerCase().startsWith(constraint.toString().toLowerCase()) || names.toLowerCase().contains(constraint.toString().toLowerCase()))
                            suggestions.add(names);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<String> filterList = (List<String>) results.values;
            if (results.count > 0) {
                clear();
                for (String names : filterList) {
                    add(names);
                    notifyDataSetChanged();
                }
            }
        }
    };
}