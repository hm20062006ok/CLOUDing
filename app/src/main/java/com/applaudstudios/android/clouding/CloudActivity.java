package com.applaudstudios.android.clouding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CloudActivity extends AppCompatActivity {

    private List<Cloud> mCloudList = new ArrayList<>();
    private DatabaseHelper dbHelp;
    private RecyclerView mRecyclerView;
    private CloudAdapter mAdapter;
    private TextView cloudCountTextView;
    TextView emptyListTextView;
    private int cloudCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.applaudstudios.android.clouding.R.layout.activity_cloud);

        dbHelp = new DatabaseHelper(this);

        //dbHelp.deleteDatabase();

        //Funny how the database was not regenerated every time... delete everything! (while testing)
        //dbHelp.deleteAllData();

        mCloudList = dbHelp.getAllClouds();

        emptyListTextView = (TextView) findViewById(com.applaudstudios.android.clouding.R.id.empty_list_text_view);
        checkEmptyList();

        cloudCountTextView = (TextView) findViewById(com.applaudstudios.android.clouding.R.id.cloud_count);
        updateCloudCount();

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(com.applaudstudios.android.clouding.R.id.recycler_view);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CloudAdapter(CloudActivity.this, mCloudList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                mAdapter.setCloudList(dbHelp.getAllClouds());
                checkEmptyList();
                updateCloudCount();
            case 2:
                mAdapter.setCloudList(dbHelp.getAllClouds());
                checkEmptyList();
                updateCloudCount();
        }
    }

    public void updateCloudCount() {
        cloudCountTextView.setText(getString(R.string.clouds) + Integer.toString(mCloudList.size()));
    }

    public void checkEmptyList() {
        TextView emptyListTextView = (TextView) findViewById(com.applaudstudios.android.clouding.R.id.empty_list_text_view);
        if (mCloudList.size() > 0) {
            emptyListTextView.setVisibility(View.GONE);
        } else {
            emptyListTextView.setVisibility(View.VISIBLE);
        }
    }

    public void openNewCloudActivity(View view) {
        /*
        The Add Cloud button in the other activity adds the cloud to the db and also, closes
        that activity. That triggers the onActivityResult here, which updates the data in the
        Adapter... it works, not sure if it is the best way to do it though. I wanted to trigger
        it straight from the other activity... but no idea how to get a reference to the adapter
        over there.
        */
        startActivityForResult(new Intent(CloudActivity.this, CloudCreation.class), 1);
    }

}
