package com.example.sakkar.canvaspainttest;

import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CustomLayout layout;
    CheckBox box,box2;
    SeekBar seekBar1,seekBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        box= (CheckBox) findViewById(R.id.box);
        box2= (CheckBox) findViewById(R.id.box2);
        seekBar1= (SeekBar) findViewById(R.id.seekbar1);
        seekBar2= (SeekBar) findViewById(R.id.seekbar2);

        layout= (CustomLayout) findViewById(R.id.item_list);
        ArrayList<String> itemList=new ArrayList<>();
        itemList.add("Tumpa");
        itemList.add("Shuvo");
        itemList.add("Rupma");
        itemList.add("Bristy");
        itemList.add("Antu");
        itemList.add("Sakkar");
        itemList.add("Turjo");
        itemList.add("Barsha");
        itemList.add("Neha");
        itemList.add("Shupto");
        itemList.add("Adrita");
        itemList.add("Suchona");
        itemList.add("Isika");
        itemList.add("Soha");
        layout.addItemList(itemList);

        seekBar1.setProgress(layout.getItemHorizontalSpacing());
        seekBar2.setProgress(layout.getItemVerticalSpacing());

        box.setChecked(!layout.isSingleSelectable());
        box2.setChecked(layout.isGravityCenter());

        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    layout.setSelectionState(CustomLayout.SelectionState.MULTIPLE);
                else
                    layout.setSelectionState(CustomLayout.SelectionState.SINGLE);
            }
        });

        box2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    layout.setGravity(CustomLayout.Gravity.CENTER);
                else
                    layout.setGravity(CustomLayout.Gravity.START);
            }
        });

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                layout.setItemHorizontalSpacing(getValuePx(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                layout.setItemVerticalSpacing(getValuePx(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    int getValuePx(int x) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, getResources().getDisplayMetrics());
    }
}
