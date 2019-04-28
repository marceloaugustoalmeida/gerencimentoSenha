package com.example.cedro.gerenciamentosenha;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cedro.gerenciamentosenha.DAO.SitesDAO;
import com.example.cedro.gerenciamentosenha.model.Site;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

class ListaSitesAdapter extends RecyclerView.Adapter {

    private List<Site> listaSites;
    private Context context;
    private ActivityListaItens activityListaItens;

    public ListaSitesAdapter(List<Site> listaSites, Context context) {
        this.listaSites = listaSites;
        this.context = context;
        activityListaItens = new ActivityListaItens();

    }

//    @Override
//    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView);
//        onStop();
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_item, parent, false);

        ListaSitesHolder holder = new ListaSitesHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        ListaSitesHolder holder = (ListaSitesHolder) viewHolder;

        final Site site = listaSites.get(position);

        holder.emailUsuario.setText(site.getUsuario().getEmail());
        holder.urlSite.setText(site.getUrl());
        holder.senhaUsuario.setText(site.getUsuario().getSenha());
        holder.urlSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SitesDAO siteDAO = new SitesDAO(context);
                Site site = siteDAO.buscaAlunoId(String.valueOf(listaSites.get(position).getId()));
                //Site site = (Site) listaAlunos.getItemAtPosition(position);

                Intent intent = new Intent(context, ActivityNovoSite.class);
                intent.putExtra("site", site);
                context.startActivity(intent);
            }
        });

        holder.urlSite.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SitesDAO siteDAO = new SitesDAO(context);
                Site site = siteDAO.buscaAlunoId(String.valueOf(listaSites.get(position).getId()));
                siteDAO.deleta(site);
                Toast.makeText(context, "Item deletado!", Toast.LENGTH_SHORT).show();
                siteDAO.close();
                listaSites.remove(position);
                notifyItemRemoved(position);
                EventBus.getDefault().post(true);

                return true;
            }
        });

        ((ListaSitesHolder) viewHolder).emailUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((ListaSitesHolder) viewHolder).senhaUsuario.getVisibility() == View.GONE) {
                    ((ListaSitesHolder) viewHolder).senhaUsuario.setVisibility(View.VISIBLE);
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(((ListaSitesHolder) viewHolder).senhaUsuario.getText().toString());
                    Toast.makeText(context, "Senha copiada para área de transferência", Toast.LENGTH_SHORT).show();
                } else if (((ListaSitesHolder) viewHolder).senhaUsuario.getVisibility() == View.VISIBLE) {
                    ((ListaSitesHolder) viewHolder).senhaUsuario.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaSites.size();
    }

    protected void onStop() {
        //super.onStop();
        activityListaItens.Unregister();
    }
}

class ListaSitesHolder extends RecyclerView.ViewHolder {

    final ImageView iconeSite;
    final TextView urlSite;
    final TextView emailUsuario;
    final TextView senhaUsuario;
    final LinearLayout llInformation;

    public ListaSitesHolder(View view) {
        super(view);
        iconeSite = (ImageView) view.findViewById(R.id.iconeSite);
        urlSite = (TextView) view.findViewById(R.id.urlSite);
        emailUsuario = (TextView) view.findViewById(R.id.emailUsuario);
        senhaUsuario = (TextView) view.findViewById(R.id.senhaUsuario);
        llInformation = (LinearLayout) view.findViewById(R.id.ll_information);
    }

}
