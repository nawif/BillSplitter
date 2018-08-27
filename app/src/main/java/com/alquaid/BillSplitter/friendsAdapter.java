package com.alquaid.BillSplitter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.alquaid.BillSplitter.Data.nameAndTotal;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Nawif on 2/20/18.
 *
 */

@SuppressWarnings("ALL")
public class friendsAdapter extends ArrayAdapter<nameAndTotal> {
    private static final String TAG = friendsAdapter.class.getCanonicalName();
    static boolean hideChecked=false;
    ArrayList<Pair<CheckBox,Integer>> checkBoxes;
    ArrayList<nameAndTotal> listONameAndTotals;
    friendsAdapter(@NonNull Context context, @NonNull List<nameAndTotal> objects) {
        super(context, 0, objects);
        listONameAndTotals= (ArrayList<nameAndTotal>) objects;
        checkBoxes= new ArrayList<>();
    }
    private void addAndReload(CheckBox isSelected,final int position, @Nullable final View convertView, @NonNull final ViewGroup parent){
        Log.d(TAG, "addAndReload:"+isSelected.isChecked());
        if(isSelected.isChecked()) {
            CalculateTotal.checkedBoxes.remove(((Integer)position));
        }
        else {
            CalculateTotal.checkedBoxes.add(position);
        }
        isSelected.setChecked(!isSelected.isChecked());
        getView(position, convertView,parent);
    }
    @NonNull
    @SuppressLint("SetTextI18n")
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.card,parent,false);
        // Declare Views
        final CheckBox isSelected=listItem.findViewById(R.id.isSelected);
        if(!isAlreadyAdded(position))
            checkBoxes.add(new Pair<CheckBox, Integer>(isSelected,position));
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
                if(CalculateTotal.checkedBoxes.contains(position))
                    CalculateTotal.checkedBoxes.remove(((Integer)position));
                else
                    CalculateTotal.checkedBoxes.add(position);
                getView(position, convertView,parent);
            }
        });
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAndReload(isSelected,position,convertView,parent);

            }
        });
        if(CalculateTotal.checkedBoxes.contains(position))
            isSelected.setChecked(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            listItem.setElevation(10);
            return listItem;
    }
    public void CheckBoxes(Boolean isChecked){
        CalculateTotal.checkedBoxes=new ArrayList<>();
        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).first.setChecked(isChecked);
            if(isChecked)
                CalculateTotal.checkedBoxes.add(i,i);
        }
    }
    public boolean isAlreadyAdded(int position){
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).second.equals(position))
                return true;
        }
        return false;
    }
}
