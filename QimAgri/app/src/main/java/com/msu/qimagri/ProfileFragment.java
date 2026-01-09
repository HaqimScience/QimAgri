package com.msu.qimagri;

import android.app.Activity;
import android.content.Context;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.msu.qimagri.util.DatabaseHelper;
import com.msu.qimagri.util.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    // 1. Define the interface
    public interface OnLogoutListener {
        void onLogout();
    }

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profilePicture;
    private EditText inputFullName, inputEmailAddress, inputPhoneNumber, inputPassword;
    private Button buttonUpdate, buttonDelete;
    private Uri imageUri;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private String userEmail;
    private OnLogoutListener logoutListener;

    // 2. Get a reference to the listener in onAttach
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLogoutListener) {
            logoutListener = (OnLogoutListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnLogoutListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new DatabaseHelper(getActivity());
        sessionManager = new SessionManager(getActivity());

        profilePicture = view.findViewById(R.id.profilePicture);
        inputFullName = view.findViewById(R.id.input_full_name);
        inputEmailAddress = view.findViewById(R.id.input_email_address);
        inputPhoneNumber = view.findViewById(R.id.input_phone_number);
        inputPassword = view.findViewById(R.id.input_password);
        buttonUpdate = view.findViewById(R.id.button_save);
        buttonDelete = view.findViewById(R.id.button_delete_account);

        userEmail = sessionManager.getUserEmail();
        if (userEmail != null) {
            loadUserProfile();
        }

        profilePicture.setOnClickListener(v -> openFileChooser());
        buttonUpdate.setOnClickListener(v -> updateUserProfile());
        buttonDelete.setOnClickListener(v -> deleteUserProfile());

        return view;
    }

    private void loadUserProfile() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                DatabaseHelper.COLUMN_USER_NAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_PHONE,
                DatabaseHelper.COLUMN_USER_PASSWORD,
                DatabaseHelper.COLUMN_USER_IMAGE
        };
        String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};

        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            inputFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
            inputEmailAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)));
            inputPhoneNumber.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE)));
            inputPassword.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_IMAGE));
            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                profilePicture.setImageBitmap(bitmap);
            }
        }
        cursor.close();
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

    private void updateUserProfile() {
        String name = inputFullName.getText().toString().trim();
        String phone = inputPhoneNumber.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, name);
        values.put(DatabaseHelper.COLUMN_USER_PHONE, phone);
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, password);

        if (profilePicture.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageInByte = baos.toByteArray();
            values.put(DatabaseHelper.COLUMN_USER_IMAGE, imageInByte);
        }

        String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};

        int count = db.update(DatabaseHelper.TABLE_USER, values, selection, selectionArgs);

        if (count > 0) {
            Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Error updating profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUserProfile() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};
        int deletedRows = db.delete(DatabaseHelper.TABLE_USER, selection, selectionArgs);

        if (deletedRows > 0) {
            Toast.makeText(getActivity(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
            logout();
        } else {
            Toast.makeText(getActivity(), "Error deleting account", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        if (logoutListener != null) {
            logoutListener.onLogout();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logoutListener = null;
    }
}
