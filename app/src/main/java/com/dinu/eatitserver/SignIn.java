package com.dinu.eatitserver;

import androidx.appcompat.app.AppCompatActivity;
import info.hoang8f.widget.FButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dinu.eatitserver.Common.Common;
import com.dinu.eatitserver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText edtPhone,edtPassword;
    Button btnSignIn;

    FirebaseDatabase database;
    DatabaseReference tbl_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        btnSignIn = (FButton)findViewById(R.id.btnSignIn);

        //Initialize the firebase
        database = FirebaseDatabase.getInstance();
        tbl_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInUser(edtPhone.getText().toString(),edtPassword.getText().toString());

            }

            private void signInUser(String phone, String password) {

                final ProgressDialog msgDialog =  new ProgressDialog(SignIn.this);
                msgDialog.setMessage("Please wait...");
                msgDialog.show();

                final String localPhone=phone;
                final String localPw= password;
                tbl_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(localPhone).exists()){

                            Log.i("innnnnnnn","edtPW "+dataSnapshot.child(edtPassword.getText().toString()));

                            // Get user information
                            msgDialog.dismiss();
                            User user = dataSnapshot.child(localPhone).getValue(User.class);
                            user.setPhone(localPhone);

                            if(Boolean.parseBoolean(user.getIsStaff())){

                                if (user.getPassword().equals(localPw)){

                                    Intent homeIntent= new Intent(SignIn.this,Home.class);
                                    Common.currentUser=user;
                                    startActivity(homeIntent);
                                    finish();

                                }else {
                                    Log.i("PW","Password "+localPw);
                                    Toast.makeText(SignIn.this ,"Invalid Password", Toast.LENGTH_LONG).show();

                                }

                            }else {
                                Toast.makeText(SignIn.this, "Sign In Failed, Please log with staff account", Toast.LENGTH_LONG).show();

                            }

                        }

                        else {
                            msgDialog.dismiss();
                            Toast.makeText(SignIn.this, "User does not exits", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}
