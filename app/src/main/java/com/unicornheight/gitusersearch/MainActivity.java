package com.unicornheight.gitusersearch;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements GitUserAdapter.GitUserAdapterOnClickHandler {

    RecyclerView mRecycleView;
    TextView mError;
    Button refreshButton;
    private GitUserAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mError = (TextView) findViewById(R.id.error_message);
        refreshButton = (Button) findViewById(R.id.refresh);
        mRecycleView = (RecyclerView) findViewById(R.id.recycleView_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setHasFixedSize(true);
        mAdapter = new GitUserAdapter(this, this);
        mRecycleView.setAdapter(mAdapter);
        loadGitUser();
    }
    private void loadGitUser(){
        URL gitSearchUrl = NetworkAccess.buildGitUrl(getString(R.string.PARAM_SEARCH));
        if (!isNetworkAvailable()) {
            showError();
            mError.setText(getString(R.string.internet_fail));
        } else {
            new GitUserList().execute(gitSearchUrl);
        }
    }

    public class GitUserList extends AsyncTask<URL, Void, String[]>{
          final ProgressDialog dialog = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String[] doInBackground(URL... params) {
            URL location = params[0];
            String[] UserJsonData;
            try {
                    String jsonResponse = NetworkAccess
                            .getResponseFromHttpUrl(location);
                UserJsonData = UserJsonHandler
                            .getUserStringsFromJson(MainActivity.this, jsonResponse);
                return UserJsonData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] userData) {
            dialog.dismiss();
            if (userData !=null && !userData.equals("") ){
                showData();
                mAdapter.setUserData(userData);
            }else {
                showError();
            }
        }
    }

    private void showData(){
        mError.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.INVISIBLE);
        mRecycleView.setVisibility(View.VISIBLE);

    }

    public void Refresh(View v){
        loadGitUser();
    }

    private void showError(){
        mRecycleView.setVisibility(View.INVISIBLE);
        mError.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String gitData) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, gitData);
        startActivity(intentToStartDetailActivity);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
