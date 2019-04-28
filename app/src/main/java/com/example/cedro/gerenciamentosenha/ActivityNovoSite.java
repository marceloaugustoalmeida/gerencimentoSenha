package com.example.cedro.gerenciamentosenha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cedro.gerenciamentosenha.DAO.SitesDAO;
import com.example.cedro.gerenciamentosenha.model.Site;
import com.example.cedro.gerenciamentosenha.model.Usuario;
import com.example.cedro.gerenciamentosenha.util.Utils;

public class ActivityNovoSite extends AppCompatActivity {

    private EditText edtUrl;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnCadastrarSite;

    private Site site;

    Site novoSite;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_site);

        UIContent();

        novoSite = new Site();
        usuario = new Usuario();

        Intent intent = getIntent();
        Site site = (Site) intent.getSerializableExtra("site");
        if (site != null) {
            novoSite.setId(site.getId());
            edtUrl.setText(site.getUrl());
            edtEmail.setText(site.getUsuario().getEmail());
            edtSenha.setText(site.getUsuario().getSenha());
            this.site = site;
            btnCadastrarSite.setText(R.string.change);
        }

    }

    private void UIContent() {
        edtUrl = findViewById(R.id.edtUrl);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnCadastrarSite = findViewById(R.id.btnCadastrarSite);

        btnCadastrarSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Patterns.WEB_URL.matcher(edtUrl.getText().toString()).matches()) {
                    Toast.makeText(ActivityNovoSite.this, "URL inválida!", Toast.LENGTH_SHORT).show();
                    edtUrl.setFocusableInTouchMode(true);
                    edtUrl.requestFocus();
                    edtUrl.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                    return;
                } else if (edtEmail.length() < 4) {
                    Toast.makeText(ActivityNovoSite.this, "Usuário tem que ser maior do que 3 caráteres!", Toast.LENGTH_SHORT).show();
                    edtEmail.setFocusableInTouchMode(true);
                    edtEmail.requestFocus();
                    edtEmail.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                    return;
                } else if (edtSenha.length() < 4) {
                    Toast.makeText(ActivityNovoSite.this, "Senha tem que ser maior do que 3 caráteres!", Toast.LENGTH_SHORT).show();
                    edtSenha.setFocusableInTouchMode(true);
                    edtSenha.requestFocus();
                    edtSenha.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                    return;
                } else {

                    novoSite.setUrl(edtUrl.getText().toString());
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setSenha(edtSenha.getText().toString());
                    novoSite.setUsuario(usuario);
                    //novoSite.setToken(Utils.recuperaDadosUsuario(ActivityNovoSite.this));

                    SitesDAO dao = new SitesDAO(ActivityNovoSite.this);

                    if (novoSite.getId() != null) {
                        dao.altera(novoSite);
                    } else {
                        dao.insere(novoSite);
                    }
                    dao.close();
                    finish();
                }
            }
        });

    }
}
