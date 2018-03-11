package zhe.charmu.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import zhe.charmu.MainActivity;
import zhe.charmu.model.User;
import zhe.charmu.R;

/**
 * Created by songz on 1/2/2018.
 */

public class RegisterActivity extends AppCompatActivity{
    private static final String TAG = "REGISTER_ACTIVITY";
    private static final String PROGRESS_DIALOG_MESSAGE = "Creating Account...";
    private static final String TOAST_USER_NAME = "Please fill in User Name";
    private static final String TOAST_EMAIL = "Please fill in Email";
    private static final String TOAST_PASSWORD = "Please fill in Password";
    private static final String STATUS_DEFAULT_VALUE = "Say Something...";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;

    private EditText userName;
    private HashMap<String, User> userMap;
    private EditText email;
    private EditText password;
    private Button registerButton;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);

        // Initialize Views
        registerButton = (Button) findViewById(R.id.register_button_Register);
        userName = (EditText) findViewById(R.id.userNameEditText);
        email = (EditText) findViewById(R.id.emailEditText_Register);
        password = (EditText) findViewById(R.id.passwordEt_Register);

        // Initialize FirebaseAuth instance and AuthStateListener method
        mAuth = FirebaseAuth.getInstance();

        // Initialize FirebaseDatabase
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");

        // Initialize ProgressDialog
        mProgressDialog = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "create account");
                createAccount();
            }
        });
    }

    private void createAccount() {
        final String userNameText = userName.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String pwdText = password.getText().toString().trim();

        if (!TextUtils.isEmpty(userNameText) && !TextUtils.isEmpty(emailText)
                && !TextUtils.isEmpty(pwdText)) {
            mProgressDialog.setMessage(PROGRESS_DIALOG_MESSAGE);
            mProgressDialog.show();

            // Create a new account with email and password
            mAuth.createUserWithEmailAndPassword(emailText, pwdText)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String userId = mAuth.getCurrentUser().getUid();

                                //createUser(userId);
                                User users = new User(userNameText,STATUS_DEFAULT_VALUE,
                                        "default","default",userId);

                                DatabaseReference curUserDb = mDatabaseReference.child(userId);
                                curUserDb.setValue(users);

                                mProgressDialog.dismiss();
                                // direct user to PostListActivity
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else{
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        } else {
            if (TextUtils.isEmpty(userNameText)) {
                Toast.makeText(RegisterActivity.this, TOAST_USER_NAME, Toast.LENGTH_SHORT)
                        .show();
            }

            if (TextUtils.isEmpty(emailText)) {
                Toast.makeText(RegisterActivity.this, TOAST_EMAIL, Toast.LENGTH_SHORT)
                        .show();
            }

            if (TextUtils.isEmpty(pwdText)) {
                Toast.makeText(RegisterActivity.this, TOAST_PASSWORD, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    public void createUser(String userID){

        mDatabaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,User>>
                        t = new GenericTypeIndicator<HashMap<String, User>>() {
                };
                userMap = dataSnapshot.getValue(t);
                if(userMap == null){
                    userMap = new HashMap<>();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}