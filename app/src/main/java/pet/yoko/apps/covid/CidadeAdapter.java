package pet.yoko.apps.covid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class CidadeAdapter extends RecyclerView.Adapter <CidadeAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cidade;
        public TextView confirmados;
        public TextView suspeitos;
        public TextView obitos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cidade = (TextView)itemView.findViewById(R.id.txtCidade);
            confirmados = (TextView)itemView.findViewById(R.id.txtConfirmados);
            suspeitos = (TextView)itemView.findViewById(R.id.txtSuspeitos);
            obitos = (TextView)itemView.findViewById(R.id.txtObitos);
        }
    }

    public void setItems(List<CidadeItem> items) {
        this.items = items;
    }

    private List<CidadeItem> items;

    public CidadeAdapter(List<CidadeItem> items) {
        this.items = items;
    }

    public List<CidadeItem> getItems() {
        return items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cidaderowlayout,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CidadeItem item = items.get(position);
        TextView cidade = holder.cidade;
        TextView confirmados = holder.confirmados;
        TextView suspeitos = holder.suspeitos;
        TextView obitos = holder.obitos;
        cidade.setText(item.cidade);
        if (item.confirmados!=-1) {
            confirmados.setText(String.valueOf(item.confirmados));
            suspeitos.setText(String.valueOf(item.suspeitos));
            obitos.setText(String.valueOf(item.obitos));
        }
        else {
            confirmados.setText("");
            suspeitos.setText("");
            obitos.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
