package com.marsianets.orderbook;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements SummaryOrderFragment.SummaryFragmentListener,
        FullOrderFragment.FullFragmentListener {

    boolean withDetails = true;

    public static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = this.getApplicationContext();
        setContentView(R.layout.activity_main);
        withDetails = (findViewById(R.id.fullFragment) != null);
        if (savedInstanceState == null) {
            setMyViews();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void setMyViews() {
        SummaryOrderFragment summaryFragment;
        if (withDetails) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            summaryFragment = new SummaryOrderFragment();
            fragmentTransaction.add(R.id.summaryFragment, summaryFragment);
            FullOrderFragment fullFragment = FullOrderFragment.newInstance(Order.getOrdersCount() + 1);
            fragmentTransaction.add(R.id.fullFragment, fullFragment);
            fragmentTransaction.commit();
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            summaryFragment = new SummaryOrderFragment();
            fragmentTransaction.add(R.id.summaryFragment, summaryFragment);
            fragmentTransaction.commit();
        }
        FloatingActionButton newOrderButton = findViewById(R.id.newOrderButton);
        newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newOrderButtonClicked(new Date());
            }
        });

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
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveOrderButtonClicked() {
        SummaryOrderFragment summaryOrderFragment = new SummaryOrderFragment();
        getFragmentManager().beginTransaction().replace(R.id.summaryFragment, summaryOrderFragment).commit();
    }

    @Override
    public void newOrderButtonClicked(Date date) {
        if (withDetails) {
            FragmentManager fragmentManager = getFragmentManager();
            FullOrderFragment fullOrderFragment = FullOrderFragment.newInstance(date);
            SummaryOrderFragment summaryOrderFragment = new SummaryOrderFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fullFragment, fullOrderFragment);
            fragmentTransaction.replace(R.id.summaryFragment, summaryOrderFragment);
            fragmentTransaction.commit();
        } else {
            startActivityForResult(new Intent(this, FullOrderActivity.class).putExtra("date", date), 1);
        }
    }

    @Override
    public void currentOrderClicked(int orderId) {
        if (withDetails) {
            FragmentManager fragmentManager = getFragmentManager();
            FullOrderFragment fullOrderFragment = FullOrderFragment.newInstance(orderId);
            fragmentManager.beginTransaction().replace(R.id.fullFragment, fullOrderFragment).commit();
        } else {
            startActivityForResult(new Intent(this, FullOrderActivity.class).putExtra("orderId", orderId), 1);
            Toast toast = Toast.makeText(this, "You choose order No " + orderId, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void deleteCurrentOrder(int orderId) {
        Order.deleteOrderByID(orderId);
        SummaryOrderFragment summaryOrderFragment = new SummaryOrderFragment();
        getFragmentManager().beginTransaction().replace(R.id.summaryFragment, summaryOrderFragment).commit();
        newOrderButtonClicked(new Date());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String returnCommand = data.getStringExtra("returnCommand");
        if (returnCommand.equals("exit")) {
            this.finish();
        }
        if (returnCommand.equals("save")) {
            saveOrderButtonClicked();
        }
    }
}

