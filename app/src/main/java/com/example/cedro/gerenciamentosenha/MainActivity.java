package com.example.cedro.gerenciamentosenha;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cedro.gerenciamentosenha.interfaceService.LoginService;
import com.example.cedro.gerenciamentosenha.model.RequestLogin;
import com.example.cedro.gerenciamentosenha.model.ResponseLogin;
import com.example.cedro.gerenciamentosenha.model.Site;
import com.example.cedro.gerenciamentosenha.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText edtEmail = findViewById(R.id.edtEmail);
        final EditText edtSenha = findViewById(R.id.edtSenha);
        Button btnLogin = findViewById(R.id.btnLogin);

        final Usuario usuario = new Usuario();
        usuario.setEmail(edtEmail.getText().toString());
        usuario.setSenha(edtSenha.getText().toString());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(LoginService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                LoginService service = retrofit.create(LoginService.class);

                RequestLogin requestLogin = new RequestLogin();

                requestLogin.email = edtEmail.getText().toString();
                requestLogin.password = edtSenha.getText().toString();

                Call<ResponseLogin> requestCatalog = service.login(requestLogin);

                requestCatalog.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        if (!response.isSuccess()) {
                            Toast.makeText(MainActivity.this, "Erro: " + response.code(), Toast.LENGTH_SHORT).show();

                        } else {
                            ResponseLogin catalog = response.body();
                            Site site = new Site();
                            site.setToken(catalog.token);
                            Usuario usuario = new Usuario();
                            usuario.setEmail(edtEmail.getText().toString());
                            usuario.setSenha(edtSenha.getText().toString());
                            site.setUsuario(usuario);

                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.usuario), Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.token), site.getToken());
                            editor.putString(getString(R.string.usuario), site.getUsuario().getEmail());
                            editor.putString(getString(R.string.senha), site.getUsuario().getSenha());
                            editor.apply();
                            Intent intent = new Intent(MainActivity.this, ActivityListaItens.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Falhou: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //new RealizaLoginTask(MainActivity.this, usuario).execute();

            }
        });
    }
}
