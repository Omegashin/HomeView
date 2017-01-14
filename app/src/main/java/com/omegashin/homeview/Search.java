package com.omegashin.homeview;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Search extends Activity implements View.OnClickListener {

    EditText editText;
    CardView searchBar;
    ImageView GTag, clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBar = (CardView) findViewById(R.id.search_card);
        GTag = (ImageView) findViewById(R.id.g_tag);
        clearButton = (ImageView) findViewById(R.id.clear);
        clearButton.setOnClickListener(this);
        GTag.setVisibility(View.INVISIBLE);

        Animation searchCardRevealAnim = AnimationUtils.loadAnimation(this, R.anim.search_card_reveal);
        searchBar.startAnimation(searchCardRevealAnim);

        searchCardRevealAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                GTag.setVisibility(View.VISIBLE);
                Animation GTagAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fab_scale);
                GTagAnim.setStartOffset(-40);
                GTag.startAnimation(GTagAnim);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        editText = (EditText) findViewById(R.id.search_field);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    openSearchResults();
                    return true;
                }
                return false;
            }
        });
        editText.requestFocus();
    }

    public void openSearchResults() {
        try {

            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            String term = editText.getText().toString();
            intent.putExtra(SearchManager.QUERY, term);
            startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.clear) {
            editText.setText("");
        }
    }

    public void backPress(View view) {
        finish();
    }
}