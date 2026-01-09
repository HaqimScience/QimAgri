
package com.msu.qimagri;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.msu.qimagri.util.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignupFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profilePicture;
    private EditText inputFullName, inputEmailAddress, inputPhoneNumber, inputPassword;
    private Button buttonSignUp, buttonBackToLogin;
    private Uri imageUri;

    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sign Up");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        dbHelper = new DatabaseHelper(getActivity());

        profilePicture = view.findViewById(R.id.profilePicture);
        inputFullName = view.findViewById(R.id.input_full_name);
        inputEmailAddress = view.findViewById(R.id.input_email_address);
        inputPhoneNumber = view.findViewById(R.id.input_phone_number);
        inputPassword = view.findViewById(R.id.input_password);
        buttonSignUp = view.findViewById(R.id.button_sign_up);
        buttonBackToLogin = view.findViewById(R.id.button_back_to_login);

        profilePicture.setOnClickListener(v -> openFileChooser());

        buttonSignUp.setOnClickListener(v -> signUpUser());

        buttonBackToLogin.setOnClickListener(v -> {
            // Navigate back to LoginFragment
            getParentFragmentManager().popBackStack();
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void signUpUser() {
        String name = inputFullName.getText().toString().trim();
        String email = inputEmailAddress.getText().toString().trim();
        String phone = inputPhoneNumber.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (profilePicture.getDrawable() == null) {
            Toast.makeText(getActivity(), "Please select a profile picture", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, name);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_USER_PHONE, phone);
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_USER_IMAGE, imageInByte);

        long newRowId = db.insert(DatabaseHelper.TABLE_USER, null, values);

        if (newRowId != -1) {
            Toast.makeText(getActivity(), "User registered successfully", Toast.LENGTH_SHORT).show();
            // Navigate to LoginFragment
            getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getActivity(), "Error registering user", Toast.LENGTH_SHORT).show();
        }
    }
}
