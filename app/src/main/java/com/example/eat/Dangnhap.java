package com.example.eat;

import android.app.ProgressDialog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.Toast;

import com.example.eat.Common.Common;
import com.example.eat.Model.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;


public class Dangnhap extends AppCompatActivity {
    EditText edtsdt,edtmk;
    Button btndangnhap1;
    CheckBox ckbRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        edtsdt=(MaterialEditText)findViewById(R.id.edtsdt);
        edtmk=(MaterialEditText)findViewById(R.id.edtmk);
        btndangnhap1=(Button)findViewById(R.id.btndangnhap1);
        ckbRemember=(CheckBox)findViewById(R.id.ckbRemember);
        //Khoi tao Paper
        //Paper.init(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btndangnhap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Kiểm tra kết nối Internet
                if(Common.isConnectedToInternet(getBaseContext())){
                    //Lưu tên đăng nhập và mật khẩu
                    /*
                    if(ckbRemember.isChecked()){
                        Paper.book().write(Common.USER_KEY,edtsdt.getText().toString());
                        Paper.book().write(Common.PWD_KEY,edtmk.getText().toString());
                    }
                    */
                    final ProgressDialog process = new ProgressDialog(Dangnhap.this);
                    process.setMessage("Vui lòng đợi");
                    process.show();
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //Kiểm tra người dùng có tồn tại trong database
                            if (dataSnapshot.child(edtsdt.getText().toString()).exists()) {
                                // Lấy thông tin người dùng
                                process.dismiss();
                                User user = dataSnapshot.child(edtsdt.getText().toString()).getValue(User.class);
                                user.setPhone(edtsdt.getText().toString()); // set Phone cho người dùng
                                if(user.getPassword()==null ||edtmk.getText().toString().isEmpty()){
                                    Toast.makeText(Dangnhap.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                                }
                                else if (user.getPassword().equals(edtmk.getText().toString())) {
                                    Toast.makeText(Dangnhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    Intent homeIntent = new Intent(Dangnhap.this,Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();//Chuyển qua activity mới và hủy activity hiện tại

                                } else {
                                    Toast.makeText(Dangnhap.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                process.dismiss();
                                Toast.makeText(Dangnhap.this,"Người dùng không tồn tại",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(Dangnhap.this,"Vui lòng kiểm tra kết nối Internet",Toast.LENGTH_LONG).show();
                    return;
                }
            }

        });
    }
}
