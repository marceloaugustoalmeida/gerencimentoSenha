package com.example.cedro.gerenciamentosenha.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.cedro.gerenciamentosenha.model.Site;
import com.example.cedro.gerenciamentosenha.model.Usuario;
import com.example.cedro.gerenciamentosenha.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class SitesDAO extends SQLiteOpenHelper {

    private static String nomeBD = "Sites";
    private Context context;

    public SitesDAO(Context context) {
        super(context, nomeBD, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE Sites (id INTEGER PRIMARY KEY, nome TEXT, url TEXT NOT NULL, senha TEXT NOT NULL, email TEXT NOT NULL, token TEXT NOT NULL, dono TEXT NOT NULL, senhaDono TEXT NOT NULL);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                //String sql = "ALTER TABLE Sites ADD COLUMN caminhoFoto TEXT";
                //db.execSQL(sql);
        }
    }

    public void insere(Site site) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosAluno(site);
        db.insertOrThrow("Sites", null, dados);


    }

    @NonNull
    private ContentValues pegaDadosAluno(Site site) {
        ContentValues dados = new ContentValues();
        dados.put("nome", site.getUsuario().getNome());
        dados.put("url", site.getUrl());
        dados.put("email", site.getUsuario().getEmail());
        dados.put("senha", site.getUsuario().getSenha());
        dados.put("dono", Utils.recuperaDadosUsuario(context).getUsuario().getEmail());
        dados.put("senhaDono", Utils.recuperaDadosUsuario(context).getUsuario().getSenha());
        dados.put("token", Utils.recuperaDadosUsuario(context).getToken());
        return dados;
    }

    public List<Site> buscaSites(String email, String password) {
        //String sql = "SELECT * FROM Sites WHERE dono = " + email;//'igor@email.com'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from Sites where " + "dono=? and senhaDono=?", new String[]{email, password});

        List<Site> sites = new ArrayList<Site>();
        while (c.moveToNext()) {
            Site site = new Site();
            Usuario usuario = new Usuario();
            site.setId(c.getLong(c.getColumnIndex("id")));
            site.setUrl(c.getString(c.getColumnIndex("url")));
            usuario.setNome(c.getString(c.getColumnIndex("nome")));
            usuario.setSenha(c.getString(c.getColumnIndex("senha")));
            usuario.setEmail(c.getString(c.getColumnIndex("email")));
            site.setUsuario(usuario);
            //site.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

            sites.add(site);
        }
        c.close();
        return sites;
    }

    public void deleta(Site site) {
        SQLiteDatabase db = getWritableDatabase();

        String[] params = {site.getId().toString()};
        db.delete("Sites", "id = ?", params);

    }

    public void altera(Site site) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosAluno(site);


        String[] params = {site.getId().toString()};
        db.update("Sites", dados, "id = ?", params);
    }

    public boolean ehAluno(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Sites WHERE email = ?", new String[]{email});
        int resultado = c.getCount();
        c.close();
        return resultado > 0;
    }

    public Site buscaAlunoId(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Sites WHERE id = ?", new String[]{id});

        Site site = new Site();
        while (c.moveToNext()) {
            Usuario usuario = new Usuario();
            site.setId(c.getLong(c.getColumnIndex("id")));
            site.setUrl(c.getString(c.getColumnIndex("url")));
            usuario.setNome(c.getString(c.getColumnIndex("nome")));
            usuario.setSenha(c.getString(c.getColumnIndex("senha")));
            usuario.setEmail(c.getString(c.getColumnIndex("email")));
            site.setUsuario(usuario);
            //site.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

            //sites.add(site);
        }
        c.close();
        return site;
    }
}
