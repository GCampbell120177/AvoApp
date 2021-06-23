package za.co.appwitch.avoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference userRef;

    TextView checkRegisterStatus;
    TextInputLayout userFullName, userMobile, userEmail, userStreet, userSuburb, userLocality, userPassword;
    AppCompatButton registerUser;
    
    String fullName, mobile, email, street, suburb, city, password;
    String autoLogInEmail, autoLogInPassword;
    
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference();

        checkRegisterStatus = findViewById(R.id.check_register_status_edit_text);
        checkRegisterStatus.setVisibility(View.VISIBLE);
        userFullName = findViewById(R.id.name_edit_text);
        userFullName.setVisibility(View.GONE);
        userMobile = findViewById(R.id.mobile_edit_text);
        userMobile.setVisibility(View.GONE);
        userEmail = findViewById(R.id.email_edit_text);
        userEmail.setVisibility(View.GONE);
        userStreet = findViewById(R.id.street_edit_text);
        userStreet.setVisibility(View.GONE);
        userSuburb= findViewById(R.id.suburb_edit_text);
        userSuburb.setVisibility(View.GONE);
        userLocality = findViewById(R.id.city_edit_text);
        userLocality.setVisibility(View.GONE);
        userPassword = findViewById(R.id.password_edit_text);
        userPassword.setVisibility(View.GONE);
        
        registerUser = findViewById(R.id.register_user_button);
        registerUser.setVisibility(View.GONE);

        userID = mAuth.getUid();
        if (userID != null)
        {
            autoLogInEmail = Paper.book().read(Constants.userEmailKey);
            autoLogInPassword = Paper.book().read(Constants.userPassword);
            mAuth.signInWithEmailAndPassword(autoLogInEmail, autoLogInPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent goHome = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(goHome);
                        finish();
                    }
                }
            });
        }
        else
        {
            checkRegisterStatus.setVisibility(View.GONE);
            userFullName.setVisibility(View.VISIBLE);
            userMobile.setVisibility(View.VISIBLE);
            userEmail.setVisibility(View.VISIBLE);
            userStreet.setVisibility(View.VISIBLE);
            userSuburb.setVisibility(View.VISIBLE);
            userLocality.setVisibility(View.VISIBLE);
            userPassword.setVisibility(View.VISIBLE);

            registerUser.setVisibility(View.VISIBLE);
        }
    }

    public void RegisterUser(View view) {
        if (ValidateForm())
        {
            CreateUserAuthentication();
        }
    }

    public boolean ValidateForm()
    {
        fullName = userFullName.getEditText().getText().toString().trim();
        mobile = userMobile.getEditText().getText().toString().trim();
        email = userEmail.getEditText().getText().toString().trim();
        street = userStreet.getEditText().getText().toString().trim();
        suburb = userSuburb.getEditText().getText().toString().trim();
        city = userLocality.getEditText().getText().toString().trim();
        password = userPassword.getEditText().getText().toString().trim();
        
        if (TextUtils.isEmpty(fullName))
        {
            userFullName.setError("Field Cannot be Empty");
            return false;
        }
        if (TextUtils.isEmpty(mobile))
        {
            userMobile.setError("Field Cannot be Empty");
            return false;
        }
        if (TextUtils.isEmpty(email))
        {
            userEmail.setError("Field Cannot be Empty");
            return false;
        }
        if (TextUtils.isEmpty(street))
        {
            userStreet.setError("Field Cannot be Empty");
            return false;
        }
        if (TextUtils.isEmpty(suburb))
        {
            userSuburb.setError("Field Cannot be Empty");
            return false;
        }
        if (TextUtils.isEmpty(city))
        {
            userLocality.setError("Field Cannot be Empty");
            return false;
        }
        if (TextUtils.isEmpty(password))
        {
            userPassword.setError("Field Cannot be Empty");
            return false;
        }
        return true;
    }

    private void CreateUserAuthentication() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "User Authentication Successful", Toast.LENGTH_SHORT).show();
                    userID = mAuth.getUid();
                    Paper.book().write(Constants.userEmailKey, email);
                    Paper.book().write(Constants.userPassword, password);
                    UploadUserData();
                }
            }
        });
    }

    private void UploadUserData() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userFullName", fullName);
        userMap.put("userMobile", mobile);
        userMap.put("userEmail", email);
        userMap.put("userStreet", street);
        userMap.put("userCity", city);
        userMap.put("userSuburb", suburb);
        
        userRef.child("users").child(userID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "User Data Saved. Welcome to the AVO APP", Toast.LENGTH_SHORT).show();
                    Intent goHome = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(goHome);
                    finish();
                }
            }
        });
    }
}