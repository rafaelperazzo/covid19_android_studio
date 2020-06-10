package pet.yoko.apps.covid;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

//https://developer.android.com/training/testing/ui-testing/espresso-testing#java
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

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
    public void testaBotaoMapaBairros() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.btnBairro)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.txtTabelaCidade)).perform(click());
        onView(withId(R.id.txtTabelaConfirmados)).perform(click());
        onView(withId(R.id.txtTabelaAnalise)).perform(click());
        onView(withId(R.id.txtTabelaObitos)).perform(click());
        onView(withId(R.id.txtTabelaIncidencia)).perform(click());
        pressBack();
    }

}
