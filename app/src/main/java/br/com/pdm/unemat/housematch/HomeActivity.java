package br.com.pdm.unemat.housematch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerViewImoveis;
    private List<Imovel> listaImoveis;
    private ImovelAdapter imovelAdapter;

    TextView nameTV;

    FirebaseAuth mAuth;
    FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseFirestore.getInstance();

        recyclerViewImoveis = findViewById(R.id.recyclerViewImoveis);
        recyclerViewImoveis.setLayoutManager(new LinearLayoutManager(this));

        listaImoveis = obterImoveis();

        imovelAdapter = new ImovelAdapter(listaImoveis);
        recyclerViewImoveis.setAdapter(imovelAdapter);


        mAuth = FirebaseAuth.getInstance();

        nameTV = findViewById(R.id.NameTextView);

        SharedPreferences sharedPreferences = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        String nome = sharedPreferences.getString("nome", "");

        nameTV.setText(nome.split(" ")[0] + "!");

        FloatingActionButton btnProfile = findViewById(R.id.profileButton);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id", "");
                editor.putString("nome", "");
                editor.putString("email", "");
                editor.putString("telefone", "");
                editor.apply();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        Button anunciarButton = findViewById(R.id.anunciarButton);
        anunciarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AnunciarActivity.class));
            }
        });
    }

    private List<Imovel> obterImoveis() {
        final List<Imovel> listaImoveis = new ArrayList<>();

        CollectionReference imoveisRef = database.collection("imoveis");

        imoveisRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        String titulo = document.getString("titulo");
                        String descricao = document.getString("descricao");
                        Float valor = Float.parseFloat(document.get("preco").toString());
                        String tipo = document.getString("tipo");
                        Imovel imovel = new Imovel(
                               titulo,
                               descricao,
                               valor,
                               tipo,
                                "teste",
                                "teste"
                        );
                        listaImoveis.add(imovel);
                    }
                    imovelAdapter.notifyDataSetChanged();
                }
            }
        });
        return listaImoveis;
    }
}