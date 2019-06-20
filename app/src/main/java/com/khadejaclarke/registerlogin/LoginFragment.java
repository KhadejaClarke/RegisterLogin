package com.khadejaclarke.registerlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {
    Activity activity;
    DBHelper dbHelper;
    EditText email, password;
    Button login, text_btn_home, text_btn_register;

    boolean userExists = false;
    boolean isSigningIn = false;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity = (Activity) context;
        dbHelper = new DBHelper(activity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidgets(view);
    }

    private void initWidgets(View view) {
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);

        login = view.findViewById(R.id.login);
        login.setOnClickListener(v -> {
            String text_email = email.getText().toString();
            String text_password = password.getText().toString();

            if (TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)) {
                Snackbar.make(view, "Cannot have empty fields", Snackbar.LENGTH_LONG).show();

            } else {
                isSigningIn = true;
                updateUI();
                checkRecords(text_email, text_password);
            }

            if (userExists) {
                Snackbar.make(view, "Login successful", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent();
                activity.setResult(100, intent);
                activity.finish();

            } else {
                Snackbar.make(view, "Invalid username or password. Please check your form.", Snackbar.LENGTH_LONG).show();
                isSigningIn = false;
                updateUI();
            }

        });

        text_btn_register = view.findViewById(R.id.text_btn_register);
        text_btn_register.setOnClickListener(v ->
                ((HomeActivity)activity).switchFragment(RegisterFragment.newInstance()));

        text_btn_home = view.findViewById(R.id.text_btn_home);
        text_btn_home.setOnClickListener(v ->
                ((HomeActivity) activity).clearBackStack());
    }

    private void updateUI() {
        if (isSigningIn) {
            email.setEnabled(false);
            password.setEnabled(false);
            login.setClickable(false);
            text_btn_home.setClickable(false);
            text_btn_register.setClickable(false);

        } else {
            email.setEnabled(true);
            password.setEnabled(true);
            login.setClickable(true);
            text_btn_home.setClickable(true);
            text_btn_register.setClickable(true);
        }
    }

    private void checkRecords(String text_email, String text_password) {
        for (User user : dbHelper.getAllUsers()) {
            if (user.getEmail().equals(text_email)) {
                byte[] salt = user.getSalt().getBytes();
                String securePassword = SHAHashing.get_SHA_256_SecurePassword(text_password, salt);
                if (user.getPassword().equals(securePassword)) {
                    userExists = true;
                    break;
                }
            }
        }
    }
}
