package com.example.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.android.Data.AddType;
import com.example.android.Data.friends;
import com.valdesekamdem.library.mdtoast.MDToast;
import java.util.ArrayList;
import me.omidh.liquidradiobutton.LiquidRadioButton;

/**
 * Created by Nawif on 2/20/18.
 *
 */

public class CalcuteTotal extends Activity {
    LiquidRadioButton divide_by_every_user,add_to_all_equally,add_to_all,add_to_selected_users;
    GridView gr;
    Button increasebtn,deccreasebtn;
    EditText userValue;
    TextView totalPrice;
    static ArrayList<Integer> checkedBoxes=new ArrayList<>();
    ArrayList<friends> frls;
    friendsAdapter frAd;
    // this method is called when a checkbox is checked, and its register its position,
    // if its already checked it remove the position from the array
    public static void isChecked(int position){
        if(!checkedBoxes.contains(position))
            checkedBoxes.add(position);
        else
            checkedBoxes.remove(Integer.valueOf(position));
    }
    @SuppressWarnings("unused")
    public static int inList(ArrayList<Integer> ls, int val){
        try {

            for (int i = 0; i < ls.size(); i++) {
                if (ls.get(i) == val)
                    return i;
            }
        }catch (NullPointerException e){
            return -1;
        }
        return -1;
    }



    public void addAll(AddType addType){
        frls.trimToSize();
        checkedBoxes=new ArrayList<>();
        for(int i=0;i<frls.size();i++)
            checkedBoxes.add(i,i);
        addTotal(addType);

    }


    private void addTotal(AddType addType) {
        try {
            double value = getEveryPersonValue(addType);
            Log.e("addTotal 'value' ",value+"");
            for (int i=0;i<checkedBoxes.size();i++){
                friends tmp = frls.get(checkedBoxes.get(i));
                double newTotal=tmp.getTotal()+value;
                Log.e("addTotal 'newTotal' ",checkedBoxes.get(i)+"");
                tmp.setTotal(newTotal);
                frls.set(checkedBoxes.get(i),tmp);
            }
            frAd= new friendsAdapter(this,frls);
            gr.setAdapter(frAd);
            checkedBoxes=new ArrayList<>();
            updateTotal();

        }catch (NumberFormatException e){
            MDToast.makeText(CalcuteTotal.this,getText(R.string.didnt_Enter_value).toString(),MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
        }
    }
    private double getEveryPersonValue(AddType addType){
        int x =1;
        if(addType == AddType.MINUS)
            x=-1;
        double val=x*Double.parseDouble(userValue.getText().toString());
        if(add_to_all_equally.isChecked())
            return val;
        else
            return val/checkedBoxes.size();
    }
    @SuppressLint("SetTextI18n")
    private void updateTotal(){
        double total=0;
        for(int i=0;i<frls.size();i++)
            total+=frls.get(i).getTotal();
        totalPrice.setText( Math.floor(total * 100) / 100.0 +"");
    }



    public friends dumbData(){
        return new friends(getString(R.string.userNmae)+" "+(frls.size()+1),0);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_total);
        gr = findViewById(R.id.gridview);
        frls=new ArrayList<>();
        for(int i=0;i<5;i++){
            frls.add(dumbData());
        }
        frAd = new friendsAdapter(this,frls);
        gr.setAdapter(frAd);
        divide_by_every_user = findViewById(R.id.radbtn_22);
        add_to_all_equally=findViewById(R.id.radbtn_21);
        add_to_all=findViewById(R.id.radbtn_11);
        add_to_selected_users=findViewById(R.id.radbtn_12);
        increasebtn=findViewById(R.id.plusButton);
        deccreasebtn=findViewById(R.id.minusButton);
        userValue=findViewById(R.id.price);
        add_to_all_equally.setChecked(true);
        add_to_all.setChecked(true);
        totalPrice=findViewById(R.id.totalprice);
        totalPrice.setText(0+"");
        gr.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        //first 2 radioButtons
        divide_by_every_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_to_all_equally.setChecked(false);
            }
        });
        add_to_all_equally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                divide_by_every_user.setChecked(false);
            }
        });

        //last Two
        add_to_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_to_selected_users.setChecked(false);
                if(!friendsAdapter.hideChecked) { //if its not hidden,then change the flag and load the adapter.
                    friendsAdapter.hideChecked = true;
                     gr.setAdapter(frAd);
                }
            }
        });
        add_to_selected_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedBoxes= new ArrayList<>();
                if(friendsAdapter.hideChecked) {
                    add_to_all.setChecked(false);
                    friendsAdapter.hideChecked = false;
                    gr.setAdapter(frAd);
                }
            }
        });

        increasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add_to_all.isChecked())
                    addAll(AddType.PLUS);
                else
                    if(checkedBoxes.isEmpty())
                        MDToast.makeText(CalcuteTotal.this,getText(R.string.didnt_Choose_participants).toString(),MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
                    else
                        addTotal(AddType.PLUS);

            }

        });
        deccreasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add_to_all.isChecked())
                    addAll(AddType.MINUS);
                else
                if(checkedBoxes.isEmpty())
                    MDToast.makeText(CalcuteTotal.this,getText(R.string.didnt_Choose_participants).toString(),MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
                else
                    addTotal(AddType.MINUS);

            }

        });


    }
}

