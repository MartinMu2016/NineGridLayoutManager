package com.martin.layoutmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * <p>
 * Package Name:com.martin.layoutmanager
 * </p>
 * <p>
 * Class Name:MainActivity
 * <p>
 * Description:
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/10/31 4:55 PM Release
 * @Reviser:
 * @Modification Time:2018/10/31 4:55 PM
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Adapter mAdapter = new Adapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


}
