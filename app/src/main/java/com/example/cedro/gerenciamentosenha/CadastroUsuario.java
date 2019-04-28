package com.example.cedro.gerenciamentosenha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cedro.gerenciamentosenha.interfaceService.AddUserService;
import com.example.cedro.gerenciamentosenha.model.RequestAddUser;
import com.example.cedro.gerenciamentosenha.model.ResponseAddUser;
import com.example.cedro.gerenciamentosenha.model.Usuario;
import com.example.cedro.gerenciamentosenha.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroUsuario extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnCadastrarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        UIContent();
    }

    private void UIContent() {
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnCadastrarUsuario = findViewById(R.id.btnCadastrarUsuario);
        final Utils teste = new Utils();
        btnCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNome.length() < 3) {
                    Toast.makeText(CadastroUsuario.this, "Nome do usuário menor do que 3!", Toast.LENGTH_SHORT).show();
                    edtNome.setFocusableInTouchMode(true);
                    edtNome.requestFocus();
                    edtNome.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                    return;
                } else if (!Utils.isEmailValid(edtEmail.getText().toString())) {
                    Toast.makeText(CadastroUsuario.this, "Email inválido!", Toast.LENGTH_SHORT).show();
                    edtEmail.setFocusableInTouchMode(true);
                    edtEmail.requestFocus();
                    edtEmail.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                    return;
                } else if (!teste.isPasswordValid(edtSenha.getText().toString())) {
                    Toast.makeText(CadastroUsuario.this, "Senha inválida!", Toast.LENGTH_SHORT).show();
                    edtSenha.setFocusableInTouchMode(true);
                    edtSenha.requestFocus();
                    edtSenha.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                    return;
                }
                Usuario usuario = new Usuario();
                usuario.setNome(edtNome.getText().toString());
                usuario.setEmail(edtEmail.getText().toString());
                usuario.setSenha(edtSenha.getText().toString());
                //new TryLogin().onPostExecute("Samara");
                //new EnviaUsuarioTask(CadastroUsuario.this, usuario).execute();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(AddUserService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                AddUserService service = retrofit.create(AddUserService.class);

                RequestAddUser requestAddUser = new RequestAddUser();

                requestAddUser.setName(edtNome.getText().toString());
                requestAddUser.setEmail(edtEmail.getText().toString());
                requestAddUser.setPassword(edtSenha.getText().toString());

                Call<ResponseAddUser> responseAddUserCall = service.addUser(requestAddUser);

                responseAddUserCall.enqueue(new Callback<ResponseAddUser>() {

                    @Override
                    public void onResponse(Call<ResponseAddUser> call, Response<ResponseAddUser> response) {
                        if (!response.isSuccess()) {
                            Toast.makeText(CadastroUsuario.this, "Erro: " + response.code() + response.message().toString(), Toast.LENGTH_SHORT).show();
                            if (response.code() == 409) {
                                Toast.makeText(CadastroUsuario.this, "Usuário já existente!", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (response.code() == 400) {
                                Toast.makeText(CadastroUsuario.this, "Dados incorretos!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            ResponseAddUser responseAddUser = response.body();
                            String type = responseAddUser.type;
                            Toast.makeText(CadastroUsuario.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                            chamaNovaAcitivity();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseAddUser> call, Throwable t) {
                        Toast.makeText(CadastroUsuario.this, "Falhou: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }


        });
    }

    private void chamaNovaAcitivity() {
        Intent intent = new Intent(CadastroUsuario.this, ActivityListaItens.class);
        startActivity(intent);
        finish();
    }
}
