package br.com.pdm.unemat.housematch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (currentUser != null) {
                    DocumentReference userRef = db.collection("users").document(currentUser.getUid());
                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("id", currentUser.getUid());
                                    editor.putString("nome", document.getString("nome"));
                                    editor.putString("email", document.getString("email"));
                                    editor.putString("telefone", document.getString("telefone"));
                                    editor.apply();
                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                } else {
                                    // O documento não existe, faça o tratamento apropriado, como redirecionar para a tela de login
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                            } else {
                                // Ocorreu um erro na obtenção dos dados, faça o tratamento apropriado
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                            finish();
                        }
                    });
                } else {
                    Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DELAY);
    }
}