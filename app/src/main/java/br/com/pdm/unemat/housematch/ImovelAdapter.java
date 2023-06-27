package br.com.pdm.unemat.housematch;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ImovelAdapter extends RecyclerView.Adapter<ImovelAdapter.ViewHolder> {
    private List<Imovel> imoveis;
    public ImovelAdapter(List<Imovel> imoveis) {
        this.imoveis = imoveis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imovel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Imovel imovel = imoveis.get(position);
        holder.tvTitulo.setText(imovel.getTitulo());
//        holder.tvDescricao.setText(imovel.getDescricao());
//        holder.tvUsuario.setText(imovel.getNomeProprietario());
        holder.tvCategoria.setText(imovel.getTipo());
        holder.tvValor.setText("R$ "+ imovel.getPreco().toString());
    }

    @Override
    public int getItemCount() {
        return imoveis.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;
//        TextView tvDescricao;
//        TextView tvUsuario;
        TextView tvValor;
        TextView tvCategoria;

        Button btnResp;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
//            tvDescricao = itemView.findViewById(R.id.tvDescricao);
            tvValor = itemView.findViewById(R.id.tvValor);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            btnResp = itemView.findViewById(R.id.btnResposta);
            Log.d("TAG", "AAAAAAAAAAAAAAA");
        }
    }
}
