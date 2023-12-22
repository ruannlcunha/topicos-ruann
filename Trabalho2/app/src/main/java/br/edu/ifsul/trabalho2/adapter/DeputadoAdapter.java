package br.edu.ifsul.trabalho2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifsul.trabalho2.R;
import br.edu.ifsul.trabalho2.domain.Deputado;


public class DeputadoAdapter extends RecyclerView.Adapter<DeputadoAdapter.DeputadoViewHolder> {

    private final List<Deputado> deputados;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Deputado deputado);
    }

    public DeputadoAdapter(List<Deputado> deputados, OnItemClickListener listener) {
        this.deputados = deputados;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeputadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View deputadoView = inflater.inflate(R.layout.card_deputado, parent, false);
        return new DeputadoViewHolder(deputadoView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeputadoViewHolder holder, int position) {
        Deputado deputado = deputados.get(position);
        holder.bind(deputado, listener);
    }

    @Override
    public int getItemCount() {
        return deputados.size();
    }

    static class DeputadoViewHolder extends RecyclerView.ViewHolder {

        private final ImageView fotoImageView;
        private final TextView nomeTextView;
        private final TextView partidoUfTextView;

        DeputadoViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoImageView = itemView.findViewById(R.id.fotoImageView);
            nomeTextView = itemView.findViewById(R.id.nomeTextView);
            partidoUfTextView = itemView.findViewById(R.id.partidoUfTextView);
        }

        void bind(final Deputado deputado, final OnItemClickListener listener) {
            nomeTextView.setText(deputado.getNome());
            partidoUfTextView.setText(deputado.getSiglaPartido() + " - " + deputado.getSiglaUf());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(deputado);
                }
            });
        }
    }
}
