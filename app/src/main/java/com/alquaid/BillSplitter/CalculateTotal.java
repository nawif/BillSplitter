package com.alquaid.BillSplitter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.alquaid.BillSplitter.Data.AddType;
import com.alquaid.BillSplitter.Data.nameAndTotal;
import com.fxn.stash.Stash;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;
import com.valdesekamdem.library.mdtoast.MDToast;
import java.util.ArrayList;
import java.util.Stack;

import lib.kingja.switchbutton.SwitchMultiButton;

/**
 * Created by Nawif on 2/20/18.
 */

public class CalculateTotal extends Activity {
    MyGridView gr;
    Button increase, undoButton,resetButton, checkAll,UncheckAll;
    EditText userValue;
    TextView totalPrice;
    static ArrayList<Integer> checkedBoxes=new ArrayList<>();
    SwitchMultiButton splitWay;
    ArrayList<nameAndTotal> frls;
    friendsAdapter frAd;
    NumberPicker numberPicker;
    Stack<ArrayList<nameAndTotal>> prevFriend;
    // this method is called when a checkbox is checked, and its register its position,
    // if its already checked it remove the position from the array
    public static boolean isChecked(int position){
        if(checkedBoxes.contains(position)) {
            checkedBoxes.add(position);
            return true;
        }
        else
            checkedBoxes.remove(Integer.valueOf(position));
        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Stash.put("TAG_DATA_ARRAYLIST",frls);
        Log.d("onDestroy","onDestroy");
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
            if(checkedBoxes.size()==0){
                MDToast.makeText(CalculateTotal.this,getText(R.string.didnt_Choose_participants).toString(),MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
                return;
            }

            for (int i=0;i<checkedBoxes.size();i++){
                nameAndTotal tmp = frls.get(checkedBoxes.get(i));
                double newTotal=tmp.getTotal()+value;
                Log.e("addTotal 'newTotal' ",checkedBoxes.get(i)+"");
                tmp.setTotal(newTotal);
                frls.set(checkedBoxes.get(i),tmp);
            }
            frAd= new friendsAdapter(this,frls);
            gr.setAdapter(frAd);
//            checkedBoxes=new ArrayList<>();
            updateTotal();

        }catch (NumberFormatException e){
            MDToast.makeText(CalculateTotal.this,getText(R.string.didnt_Enter_value).toString(),MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
        }
    }
    private double getEveryPersonValue(AddType addType){
        int x =1;
        if(addType == AddType.MINUS)
            x=-1;
        double val=x*Double.parseDouble(userValue.getText().toString());
        if(splitWay.getSelectedTab()==1)
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
    private nameAndTotal dumbData(){
        return new nameAndTotal(getString(R.string.userNmae)+" "+(frls.size()+1),0);
    }
    private ArrayList<nameAndTotal> ArrayListCopier(){
        ArrayList<nameAndTotal> mCopiedArrayList=new ArrayList<>();
        for(int i=0;i<frls.size();i++){
            nameAndTotal current =new nameAndTotal(frls.get(i));
            mCopiedArrayList.add(current);
        }
        return mCopiedArrayList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_total);
        Stash.init(this);
        init();
        frAd = new friendsAdapter(this,frls);
        gr.setAdapter(frAd);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevFriend.push(ArrayListCopier());
                addTotal(AddType.PLUS);
                frAd.notifyDataSetChanged();
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevFriend.push(ArrayListCopier());
                for (int i=0;i<frls.size();i++) {
                    Log.d("resetButton Listener","i="+i);
                    nameAndTotal current=frls.get(i);
                    current.setTotal(0);
                    frls.set(i, current);
                }
                frAd = new friendsAdapter(CalculateTotal.this, frls);
                gr.setAdapter(frAd);
                updateTotal();
            }
        });
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoProgress();
            }
        });
        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frAd.CheckBoxes(true);
            }
        });
        UncheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frAd.CheckBoxes(false);
            }
        });
        }
    @Override
    protected void onPause() {
        super.onPause();
        Stash.put("TAG_DATA_ARRAYLIST",frls);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Stash.put("TAG_DATA_ARRAYLIST",frls);
    }


    @SuppressLint("SetTextI18n")
    private void init() {
        frls=new ArrayList<>();
        prevFriend =new Stack<>();
        frls = Stash.getArrayList("TAG_DATA_ARRAYLIST", nameAndTotal.class);
        if(frls.size()==0)
            setNumOfPeople(3);
        gr = findViewById(R.id.gridview);
        increase =findViewById(R.id.add);
        splitWay=findViewById(R.id.splitway);
        userValue=findViewById(R.id.price);
        totalPrice=findViewById(R.id.totalprice);
        undoButton=findViewById(R.id.undoBtn);
        resetButton=findViewById(R.id.resetBtn);
        checkAll =findViewById(R.id.checkAll);
        UncheckAll =findViewById(R.id.UncheckAll);
        totalPrice.setText(0+"");
        numberPicker =findViewById(R.id.numOfPeople);
        numberPicker.setMin(2);
        numberPicker.setValue(frls.size());
        gr.setVerticalScrollBarEnabled(false);
        numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                setNumOfPeople(value);

            }
        });
        updateTotal();
    }
    private void setNumOfPeople(int value) {
        try {
            prevFriend.push(ArrayListCopier());
            if(value<frls.size())
                frls.remove(frls.size()-1);
            else {
                frls.trimToSize();
                for (int i = frls.size(); i < value; i++)
                    frls.add(dumbData());
            }
            frAd = new friendsAdapter(this, frls);
            gr.setAdapter(frAd);
            updateTotal();
        }catch (NullPointerException e){
            prevFriend.pop();
            Log.e("setNumOfPeople",e.getMessage());
        }
        Log.d("setNumOfPeople",frls.size()+"");
    }
    private void undoProgress(){
        if(prevFriend.empty()) {
            Log.d("undoProgress","Empty Stack");
            return;
        }
        Log.d("undoProgress", prevFriend.empty() +"");
        frls= prevFriend.pop();
        frAd = new friendsAdapter(this, frls);
        gr.setAdapter(frAd);
        updateTotal();
        numberPicker.setValue(frls.size());
    }
}

