package pet.yoko.apps.covid;

import android.content.Context;
import android.graphics.Color;
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
        public TextView obitos;
        public TextView incidencia;
        public TextView emRecuperacao;
        public TextView situacao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cidade = (TextView)itemView.findViewById(R.id.txtCidade);
            confirmados = (TextView)itemView.findViewById(R.id.txtConfirmados);
            obitos = (TextView)itemView.findViewById(R.id.txtObitos);
            incidencia = (TextView)itemView.findViewById(R.id.txtIncidencia);
            emRecuperacao = (TextView)itemView.findViewById(R.id.txtEmRec);
            situacao = (TextView)itemView.findViewById(R.id.txtSituacaoCidade);
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
        TextView obitos = holder.obitos;
        TextView incidencia = holder.incidencia;
        TextView emRecuperacao = holder.emRecuperacao;
        TextView situacao = holder.situacao;
        cidade.setText(item.cidade);
        if (item.confirmados!=-1) {
            confirmados.setText(String.valueOf(item.confirmados));
            obitos.setText(String.valueOf(item.obitos));
            incidencia.setText(String.valueOf(item.incidencia));
            emRecuperacao.setText(String.valueOf(item.emRecuperacao));
            situacao.setText(item.getSituacao());
            if (item.obitos>0) {
                obitos.setTextColor(Color.RED);
            }
            else {
                obitos.setTextColor(Color.BLACK);
            }
            if (item.situacao.equals("ALTO")) {
                situacao.setBackgroundColor(Color.RED);;
            }
            else if (item.situacao.equals("ALERTA")) {
                situacao.setBackgroundColor(Color.parseColor("#FF3333"));
            }
            else if (item.situacao.equals("ATENÇÃO")) {
                situacao.setBackgroundColor(Color.parseColor("#FF8000"));
            }
            else if (item.situacao.equals("CONTROLADA")) {
                situacao.setBackgroundColor(Color.YELLOW);
            }
            else {
                situacao.setBackgroundColor(Color.parseColor("#336600"));
                situacao.setTextColor(Color.WHITE);
            }

        }
        else {
            confirmados.setText("");
            obitos.setText("");
            incidencia.setText("");
            emRecuperacao.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
