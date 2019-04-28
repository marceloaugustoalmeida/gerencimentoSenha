package com.example.cedro.gerenciamentosenha;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cedro.gerenciamentosenha.DAO.SitesDAO;
import com.example.cedro.gerenciamentosenha.model.Site;
import com.example.cedro.gerenciamentosenha.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ActivityListaItens extends AppCompatActivity {

    private List<Site> listaSites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_itens);
        UIContent();
        register();

        //Verificando se o aparelho está no V21 ou não
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initLFloatingButtons();
        } else {
            //implementações abaixo
        }

    }

    private void UIContent() {
        RecyclerView recyclerView = findViewById(R.id.listaSalvas);

        carregaLista();

        recyclerView.setAdapter(new ListaSitesAdapter(listaSites, this));

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layout);

        //Toast.makeText(this, Utils.recuperaToken(this), Toast.LENGTH_SHORT).show();

    }

    public void carregaLista() {
        SitesDAO dao = new SitesDAO(this);
        listaSites = dao.buscaSites(Utils.recuperaDadosUsuario(this).getUsuario().getEmail(), Utils.recuperaDadosUsuario(this).getUsuario().getSenha());
        dao.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIContent();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initLFloatingButtons() {

        final int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());

        final ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, size, size);
            }
        };

        ImageButton floatingButtonAddSite = ((ImageButton) findViewById(R.id.floatingButtonAddSite));
        floatingButtonAddSite.setOutlineProvider(viewOutlineProvider);
        floatingButtonAddSite.setClipToOutline(true);
        floatingButtonAddSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityListaItens.this, ActivityNovoSite.class);
                startActivity(intent);
            }
        });

        ImageButton floatingButtonAddUser = ((ImageButton) findViewById(R.id.floatingButtonAddUser));
        floatingButtonAddUser.setOutlineProvider(viewOutlineProvider);
        floatingButtonAddUser.setClipToOutline(true);
        floatingButtonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityListaItens.this, CadastroUsuario.class);
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Boolean event) {
        //Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
    };

    public void Unregister() {
        EventBus.getDefault().unregister(this);
    }
    public void register() {
        EventBus.getDefault().register(this);
    }
}
