package dk.dtu.matador;

import dk.dtu.matador.managers.LanguageManager;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class TestLanguageManager {
    LanguageManager lm = LanguageManager.getInstance();

    @Test
    public void testLanguageManager() {
        /*
        lm.setLocale("en");
        assertEquals(lm.getString("test_string"), "This is a test string for the English language!");
        lm.setLocale("en_US");
        assertEquals(lm.getString("test_string"), "This is a test string for the English language!");
        lm.setLocale("en", "US");
        assertEquals(lm.getString("test_string"), "This is a test string for the English language!");
        lm.setLocale(Locale.ENGLISH);
        assertEquals(lm.getString("test_string"), "This is a test string for the English language!");
         */
        lm.setLocale("da");
        assertEquals(lm.getString("test_string"), "Denne tekststreng er en test for det danske sprog!");
        lm.setLocale("da_DK");
        assertEquals(lm.getString("test_string"), "Denne tekststreng er en test for det danske sprog!");
        lm.setLocale("da", "DK");
        assertEquals(lm.getString("test_string"), "Denne tekststreng er en test for det danske sprog!");
        assertEquals(lm.getString("non_existent_test_string"), "Could not read message 'non_existent_test_string' from locale da");
    }
}
