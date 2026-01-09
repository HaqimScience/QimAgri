package com.msu.qimagri;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.msu.qimagri.util.DatabaseHelper;
import com.msu.qimagri.util.SessionManager;

public class LoginFragment extends Fragment {

    // 1. Define the listener interface
    public interface OnLoginSuccessListener {
        void onLoginSuccess();
    }

    private EditText emailAddress, password;
    private Button loginButton, registerSignup;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private OnLoginSuccessListener loginSuccessListener;

    // 2. Get a reference to the listener in onAttach
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginSuccessListener) {
            loginSuccessListener = (OnLoginSuccessListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnLoginSuccessListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        dbHelper = new DatabaseHelper(getActivity());
        sessionManager = new SessionManager(getActivity());

        emailAddress = view.findViewById(R.id.email_address);
        password = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.login_button);
        registerSignup = view.findViewById(R.id.register_signup);

        emailAddress.setHint("Email Address");
        password.setHint("Password");

        loginButton.setOnClickListener(v -> loginUser());

        registerSignup.setOnClickListener(v -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_frame_layout, new SignupFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Login");
        }
        // Clear the fields every time the fragment is resumed
        emailAddress.setText("");
        password.setText("");
    }

    private void loginUser() {
        String email = emailAddress.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = { DatabaseHelper.COLUMN_USER_ID };
        String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?" + " AND " + DatabaseHelper.COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, pass};

        Cursor cursor = db.query(DatabaseHelper.TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null, null, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            sessionManager.createLoginSession(email);
            Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
            // 3. Notify the MainActivity of the successful login
            if (loginSuccessListener != null) {
                loginSuccessListener.onLoginSuccess();
            }
        } else {
            Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loginSuccessListener = null;
    }
}
