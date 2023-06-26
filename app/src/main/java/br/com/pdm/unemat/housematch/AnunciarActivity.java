package br.com.pdm.unemat.housematch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AnunciarActivity extends AppCompatActivity {

    FirebaseFirestore database;
    TextInputEditText tituloIDT, valorIDT, descricaoIDT;
    RadioGroup radioGroup;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anunciar);

        FloatingActionButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        database = FirebaseFirestore.getInstance();

        tituloIDT = findViewById(R.id.nomeAnuncioEditText);
        radioGroup = findViewById(R.id.categoriaGroupRadio);
        valorIDT = findViewById(R.id.precoEditText);
        descricaoIDT = findViewById(R.id.descricaoEditText);

        sharedPreferences = getSharedPreferences("Usuario", Context.MODE_PRIVATE);

        Button cadastrarButton = findViewById(R.id.cadastrarButton);
        cadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarImovel();
            }
        });

    }

    private void cadastrarImovel() {
        String titulo = tituloIDT.getText().toString();
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        String valor = valorIDT.getText().toString();
        String descricao = descricaoIDT.getText().toString();

        if (TextUtils.isEmpty(titulo)) {
            tituloIDT.setError("Titulo do Anuncio não pode ser vazio.");
            tituloIDT.requestFocus();
        } else if (selectedRadioButtonId == -1) {
            Toast.makeText(AnunciarActivity.this, "Deve-se selecionar uma categoria de Imovel", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(valor)) {
            valorIDT.setError("Valor não pode ser vazio.");
            valorIDT.requestFocus();
        } else if (TextUtils.isEmpty(descricao)) {
            descricaoIDT.setError("Descrição não pode ser vazia!");
            descricaoIDT.requestFocus();
        } else {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

            CollectionReference imoveis = database.collection("imoveis");
            Map<String, Object> usuarioData = new HashMap<>();
            usuarioData.put("id", sharedPreferences.getString("id", ""));
            usuarioData.put("nome", sharedPreferences.getString("nome", ""));
            usuarioData.put("telefone", sharedPreferences.getString("telefone", ""));

            Map<String, Object> imoveisData = new HashMap<>();
            imoveisData.put("titulo", titulo);
            imoveisData.put("tipo", selectedRadioButton.getText().toString());
            imoveisData.put("descricao", descricao);
            imoveisData.put("preco", Float.parseFloat(valor));
            imoveisData.put("proprietatio", usuarioData);
            imoveisData.put("status", "Á venda");

            imoveis.add(imoveisData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AnunciarActivity.this, "OK!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AnunciarActivity.this, "Deu ruim: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}