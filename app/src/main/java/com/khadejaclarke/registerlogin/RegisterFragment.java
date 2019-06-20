package com.khadejaclarke.registerlogin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class RegisterFragment extends Fragment {
    Activity activity;
    DBHelper dbHelper;
    EditText forename, surname, email, password, password2;
    Button register, text_btn_home, text_btn_login;

    boolean isSigningUp;

    public static Fragment newInstance() {
        return new RegisterFragment();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidgets(view);
    }

    private void initWidgets(View view) {
        forename = view.findViewById(R.id.forename);
        surname = view.findViewById(R.id.surname);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        password2 = view.findViewById(R.id.password2);

        register = view.findViewById(R.id.register);
        register.setOnClickListener(v -> {
            String text_forename = forename.getText().toString();
            String text_surname = surname.getText().toString();
            String text_email = email.getText().toString();
            String text_password = password.getText().toString();
            String text_password2 = password2.getText().toString();

            if (TextUtils.isEmpty(text_forename) || TextUtils.isEmpty(text_surname) || TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password) || TextUtils.isEmpty(text_password2)) {
                Snackbar.make(view, "Cannot have empty fields", Snackbar.LENGTH_LONG).show();

            } else if (!text_password.equals(text_password2)) {
                Snackbar.make(view, "Passwords do not match.", Snackbar.LENGTH_LONG).show();

            } else {
                isSigningUp = true;
                updateUI();

                try {
                    byte[] salt = SHAHashing.getSalt();
                    String securePassword = SHAHashing.get_SHA_256_SecurePassword(text_password, salt);
                    dbHelper.insertUser(text_forename, text_surname, text_email, securePassword, Arrays.toString(salt));
                    Snackbar.make(view, "Registration successful", Snackbar.LENGTH_LONG).show();
                    ((HomeActivity) activity).switchFragment(LoginFragment.newInstance());

                } catch (NoSuchAlgorithmException e) {
                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    isSigningUp = false;
                    updateUI();
                }
            }

        });

        text_btn_login = view.findViewById(R.id.text_btn_login);
        text_btn_login.setOnClickListener(v ->
                ((HomeActivity)activity).switchFragment(LoginFragment.newInstance()));

        text_btn_home = view.findViewById(R.id.text_btn_home);
        text_btn_home.setOnClickListener(v -> new AlertDialog.Builder(activity)
                .setTitle("Cancel Registration")
                .setMessage("Are you sure you want to cancel registration?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> ((HomeActivity) activity).clearBackStack())
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
    }

    private void updateUI() {
        if (isSigningUp) {
            forename.setEnabled(false);
            surname.setEnabled(false);
            email.setEnabled(false);
            password.setEnabled(false);
            password2.setEnabled(false);
            register.setClickable(false);
            text_btn_home.setClickable(false);
            text_btn_login.setClickable(false);

        } else {
            forename.setEnabled(true);
            surname.setEnabled(true);
            email.setEnabled(true);
            password.setEnabled(true);
            password2.setEnabled(true);
            register.setClickable(true);
            text_btn_home.setClickable(true);
            text_btn_login.setClickable(true);
        }
    }
}
