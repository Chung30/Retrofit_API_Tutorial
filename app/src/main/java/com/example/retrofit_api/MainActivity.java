package com.example.retrofit_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button btnAll, btnActive, btnNonActive, btnAdd;
    private EditText edtName, edtAddress, edtState;
    private ListView lvUser;
    private ArrayList<Student> lUser = new ArrayList<>();
    private StudentAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
        UpdateData(-1);

        btnAll.setOnClickListener(v->{
            UpdateData(-1);
            userAdapter.notifyDataSetChanged();
        });
        btnNonActive.setOnClickListener(v->{
            UpdateData(0);
            userAdapter.notifyDataSetChanged();
        });
        btnActive.setOnClickListener(v->{
            UpdateData(1);
            userAdapter.notifyDataSetChanged();
        });

        btnAdd.setOnClickListener(v->{
            AddUser();
        });
    }

    private void AddUser() {
        String name = edtName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        int state = Integer.parseInt(edtState.getText().toString());

        postStudent student = new postStudent(name, address, state);

        // Gọi phương thức postUser và truyền đối tượng student vào
        ApiService.apiService.postUser(student).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateData(int i) {
        ApiService.apiService.getListUsers(i).enqueue(new Callback<ArrayList<Student>>() {
            @Override
            public void onResponse(Call<ArrayList<Student>> call, Response<ArrayList<Student>> response) {
                lUser = response.body();
                userAdapter = new StudentAdapter(lUser, MainActivity.this);
                lvUser.setAdapter(userAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Student>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("----" + t);
            }
        });
    }

    private void Init() {
        lvUser = findViewById(R.id.lvUser);
        btnAll = findViewById(R.id.btnAll);
        btnActive = findViewById(R.id.btnActive);
        btnNonActive = findViewById(R.id.btnNonActive);
        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtState = findViewById(R.id.edtState);
        btnAdd = findViewById(R.id.btnAdd);
    }
}