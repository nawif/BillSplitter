package com.alquaid.BillSplitter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alquaid.BillSplitter.Data.nameAndTotal;

import java.util.List;

/**
 * Created by Nawif on 2/20/18.
 *
 */

public class friendsAdapter extends ArrayAdapter<nameAndTotal> {
    static boolean hideChecked=true;

    friendsAdapter(@NonNull Context context, @NonNull List<nameAndTotal> objects) {
        super(context, 0, objects);
    }
    @NonNull
    @SuppressLint("SetTextI18n")
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.card,parent,false);
        // Declare Views
        final CheckBox isSelected=listItem.findViewById(R.id.isSelected);
        TextView name=listItem.findViewById(R.id.name);
        TextView price=listItem.findViewById(R.id.balance);
        // Giving it values
        nameAndTotal fr = getItem(position);
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
        isSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcuteTotal.isChecked(position);
                getView(position, convertView,parent);
            }
        });
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CalcuteTotal.isChecked(position))
                    isSelected.setChecked(true);
                else
                    isSelected.setChecked(false);
                getView(position, convertView,parent);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            listItem.setElevation(10);
            return listItem;
    }
}
