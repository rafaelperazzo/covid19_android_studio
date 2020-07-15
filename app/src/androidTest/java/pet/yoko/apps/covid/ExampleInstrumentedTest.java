package pet.yoko.apps.covid;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

//https://developer.android.com/training/testing/ui-testing/espresso-testing#java
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    UiDevice aparelho = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testaBotaoMapaCidades() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.btnCidades)).perform(click());
        onView(withText("Por Cidade")).perform(click());
        Thread.sleep(2000);
        pressBack();
        onView(withId(R.id.btnCidades)).perform(click());
        onView(withText("Por Bairro")).perform(click());
        Thread.sleep(2000);
        pressBack();
    }

    @Test
    public void testaBotaoPorSexo() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.btnSexo)).perform(click());
        onView(withText("Óbitos")).perform(click());
        Thread.sleep(2000);
        pressBack();
        onView(withId(R.id.btnSexo)).perform(click());
        onView(withText("Confirmados")).perform(click());
        Thread.sleep(2000);
        pressBack();
    }

    @Test
    public void testaBotaoPorIdade() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.btnIdade)).perform(click());
        onView(withText("Óbitos")).perform(click());
        Thread.sleep(2000);
        pressBack();
        onView(withId(R.id.btnIdade)).perform(click());
        onView(withText("Confirmados")).perform(click());
        Thread.sleep(2000);
        pressBack();
    }

    @Test
    public void testaBotaoTabela() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.btnBairro)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.txtTabelaCidade)).perform(click());
        onView(withId(R.id.txtTabelaConfirmados)).perform(click());
        onView(withId(R.id.txtTabelaObitos)).perform(click());
        onView(withId(R.id.txtTabelaIncidencia)).perform(click());
        onView(withId(R.id.txtTabelaEmRecuperacao)).perform(click());
        onView(withId(R.id.imgTabelaAjuda)).perform(click());
        onView(withId(R.id.imgTabelaAjuda)).perform(click());
        pressBack();
    }

    @Test
    public void testaCompartilhamentoGraficoPorSexo() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.btnSexo)).perform(click());
        onView(withText("Óbitos")).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.imgCompartilharGrafico)).perform(click());
        aparelho.pressBack();
        aparelho.pressBack();
        onView(withId(R.id.btnSexo)).perform(click());
        onView(withText("Confirmados")).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.imgCompartilharGrafico)).perform(click());
        aparelho.pressBack();
    }

    @Test
    public void testaCompartilhamentoGraficoPorIdade() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.btnIdade)).perform(click());
        onView(withText("Óbitos")).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.imgCompartilharGrafico)).perform(click());
        aparelho.pressBack();
        aparelho.pressBack();
        onView(withId(R.id.btnIdade)).perform(click());
        onView(withText("Confirmados")).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.imgCompartilharGrafico)).perform(click());
        aparelho.pressBack();
    }

    @Test
    public void testaCompartilhamentoTabela() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.btnBairro)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.imgShare)).perform(click());
        aparelho.pressBack();
    }

    @Test
    public void testaConfirmadosClick() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.confirmados)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.imgCompartilharGrafico)).perform(click());
        aparelho.pressBack();
    }

    @Test
    public void testaMenuCodigoFonte() throws InterruptedException {
        aparelho.pressMenu();
        Thread.sleep(2000);
        onView(withText("Sobre este app")).perform(click());
        aparelho.pressBack();

    }

    @Test
    public void testaMenuEvolucao() throws InterruptedException {
        aparelho.pressMenu();
        Thread.sleep(2000);
        onView(withText("Evolução temporal dos casos")).perform(click());
        pressBack();
    }

    @Test
    public void testaMenuGraficoResumo() throws InterruptedException {

        aparelho.pressMenu();
        Thread.sleep(2000);
        onView(withText("Gráfico resumo da situação atual")).perform(click());
        pressBack();

    }

    @Test
    public void testaMenuPercentuais() throws InterruptedException {

        aparelho.pressMenu();
        Thread.sleep(2000);
        onView(withText("Alternar Percentuais/Totais")).perform(click());
    }

    @Test
    public void testaMenuCompartilharApp() throws InterruptedException {
        aparelho.pressMenu();
        Thread.sleep(2000);
        onView(withText("Compartilhar este App")).perform(click());
        aparelho.pressBack();
    }

    @Test
    public void testaAjuda() throws InterruptedException {
        onView(withId(R.id.imgAjuda)).perform(click());
        onView(withId(R.id.imgAjuda)).perform(click());
    }

    @Test
    public void testaMudaCidade() throws InterruptedException {
        for (int i=0; i<29; i++) {
            onView(withId(R.id.cmbCidades)).perform(click());
            onData(anything()).atPosition(i).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.confirmados)).perform(click());
            Thread.sleep(2000);
            aparelho.pressBack();
        }
    }

}
