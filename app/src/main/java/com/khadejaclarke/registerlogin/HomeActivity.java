package com.khadejaclarke.registerlogin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Button;

public class HomeActivity extends FragmentActivity {
    Button home_btn_register;
    Button home_btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initWidgets();
    }

    private void initWidgets() {
        home_btn_register = findViewById(R.id.home_btn_register);
        home_btn_register.setOnClickListener(v -> switchFragment(RegisterFragment.newInstance()));

        home_btn_login = findViewById(R.id.home_btn_login);
        home_btn_login.setOnClickListener(v -> switchFragment(LoginFragment.newInstance()));
    }

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void clearBackStack() {
        String name = getSupportFragmentManager().getBackStackEntryAt(0).getName();
        getSupportFragmentManager().popBackStackImmediate(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
