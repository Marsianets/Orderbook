package com.marsianets.orderbook;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

public class FullOrderActivity extends AppCompatActivity implements FullOrderFragment.FullFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullorder_layout);
        Toolbar toolbar = findViewById(R.id.fullOrderActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (savedInstanceState == null) {
            try {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FullOrderFragment fullOrderFragment = FullOrderFragment.newInstance(Order.getOrdersCount());
                fragmentTransaction.add(R.id.fullFragment, fullOrderFragment);
                fragmentTransaction.commit();
                Date dateFromArgs = (Date) getIntent().getSerializableExtra("date");
                int orderId = getIntent().getIntExtra("orderId", -1);
                if (dateFromArgs != null) {
                    fullOrderFragment = FullOrderFragment.newInstance(dateFromArgs);
                    getFragmentManager().beginTransaction().replace(R.id.fullFragment, fullOrderFragment).commit();
                }

                if (orderId != -1) {
                    fullOrderFragment = FullOrderFragment.newInstance(orderId);
                    getFragmentManager().beginTransaction().replace(R.id.fullFragment, fullOrderFragment).commit();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }


    @Override
    public void saveOrderButtonClicked() {
        Intent intent = new Intent();
        intent.putExtra("returnCommand", "save");
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        this.saveOrderButtonClicked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the mainmenu.xml; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_exit) {
            Intent intent = new Intent();
            intent.putExtra("returnCommand", "exit");
            setResult(RESULT_OK, intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
