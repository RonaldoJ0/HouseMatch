package br.com.pdm.unemat.housematch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText etNome, etTelefone, etEmail, etSenha;
    FirebaseAuth mAuth;
    FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FloatingActionButton btnBack = findViewById(R.id.backButton);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etNome = findViewById(R.id.nomeEditText);
        etTelefone = findViewById(R.id.telefoneEditText);
        etEmail = findViewById(R.id.emailEditText);
        etSenha = findViewById(R.id.senhaEditText);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        Button btnRegistro = findViewById(R.id.registerButton);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaUsuario();
            }
        });
    }

    private void criaUsuario() {
        String nome = etNome.getText().toString();
        String telefone = etTelefone.getText().toString();
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        if (TextUtils.isEmpty(nome)){
            etNome.setError("O campo de nome não pode ficar vazio");
            etNome.requestFocus();
        }else if (TextUtils.isEmpty(telefone)){
            etTelefone.setError("O campo de telefone não pode ficar vazio");
            etTelefone.requestFocus();
        }else if (TextUtils.isEmpty(email)){
            etEmail.setError("O campo de E-mail não pode ficar vazio");
            etEmail.requestFocus();
        }else if (TextUtils.isEmpty(senha)){
            etSenha.setError("O campo de senha não estar vazia");
            etSenha.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference userRef = database.collection("users").document(userId);
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("nome", nome);
                        userData.put("telefone", telefone);
                        userData.put("email", email);

                        userRef.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(RegisterActivity.this, "Usuário Registrado com Sucesso", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Erro ao salvar dados do usuário na base de dados", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Erro ao salvar usuário:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}