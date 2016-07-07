package com.pool.rippledrawabletest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pool.rippledrawabletest.widget.RippleStyleTab;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RippleStyleTab tab = (RippleStyleTab) findViewById(R.id.fragment_switch_tab);
        int[] resDrawable = {R.drawable.tab_club, R.drawable.tab_find, R.drawable.tab_self};
        String[] resTitle = {null, null, null};
        int[] resColor = getResources().getIntArray(R.array.tab_color_array);
        for (int i = 0; i < resDrawable.length; i++) {
            tab.addTab(resDrawable[i], resTitle[i], resColor[i]);
        }
    }

}
