package com.example.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.Data.friends;

import java.util.List;

/**
 * Created by Nawif on 2/20/18.
 *
 */

public class friendsAdapter extends ArrayAdapter<friends> {
    static boolean hideChecked=true;

    friendsAdapter(@NonNull Context context, @NonNull List<friends> objects) {
        super(context, 0, objects);
    }
    @NonNull
    @SuppressLint("SetTextI18n")
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.singleperson,parent,false);
        // Declare Views
        CheckBox isSelected=listItem.findViewById(R.id.isSelected);
        TextView name=listItem.findViewById(R.id.name);
        TextView price=listItem.findViewById(R.id.balance);
//        listItem.setAlpha(0.75f);
//        listItem.setBackgroundColor(Color.LTGRAY);
        // Giving it values
        friends fr = getItem(position);
        if (fr != null) {
            name.setText(fr.getName());
        }
        if (fr != null) {
            price.setText(fr.getTotal()+"");
        }
        if(hideChecked)
         isSelected.setVisibility(View.INVISIBLE);
        else
            isSelected.setVisibility(View.VISIBLE);
//        if(CalcuteTotal.checkedBoxes.contains(position))
//            isSelected.setVisibility(View.VISIBLE);
        isSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcuteTotal.isChecked(position);
                getView(position, convertView,parent);
            }
        });
        return listItem;
    }
}
