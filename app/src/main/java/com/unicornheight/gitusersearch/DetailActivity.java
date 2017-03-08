package com.unicornheight.gitusersearch;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;



/**
 * Created by deboajagbe on 3/5/17.
 */

public class DetailActivity extends AppCompatActivity {


    TextView mName;
    ImageView mImageView;
    TextView mUrl;
    LinearLayout mTitleHolder;
    String userurl,username,userimage ;
    Button mReach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mImageView = (ImageView) findViewById(R.id.placeImage);
        mName = (TextView) findViewById(R.id.textView);
        mUrl = (TextView) findViewById(R.id.url_display);
        mReach = (Button) findViewById(R.id.rech);
        mTitleHolder = (LinearLayout) findViewById(R.id.placeNameHolder);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

        mReach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.home_url) + username));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(DetailActivity.this, getString(R.string.app_err), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {

            String userDetails = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

            String[] parts = userDetails.split("-");

            username = parts[0];
            userurl = parts[1];
            userimage = parts[2];

            mName.setText("Username - "+ username);
            mUrl.setText(getString(R.string.home_url) + username);
            Glide.with(DetailActivity.this).load(userimage)
                    .placeholder(R.drawable.imageholder)
                    .error(R.drawable.imageholder)
                    .into(mImageView);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        if (id == R.id.Share){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = getString(R.string.userShare) + username
                    +  ", " + getString(R.string.home_url) + username ;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        return super.onOptionsItemSelected(item);
    }
}