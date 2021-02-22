package pet.yoko.apps.covid.db;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public interface TaskGetInternacoesEvolucaoResponse {

    void getInternacoesFinish(ArrayList<Entry> response, ArrayList<String> labels);

}
