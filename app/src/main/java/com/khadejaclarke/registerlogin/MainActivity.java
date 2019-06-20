package com.khadejaclarke.registerlogin;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redirect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 200) && (resultCode == 100)) {
            setContentView(R.layout.activity_main);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Logout Confirmation")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> redirect())
                    .setNegativeButton(android.R.string.no, (dialog, which) -> {})
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void redirect() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivityForResult(intent, 200);
    }
}
