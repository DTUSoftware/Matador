package dk.dtu.matador.managers;

import dk.dtu.matador.Game;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class language manager with handles whether you want the language in Danish or English.
 * <p>
 * Note: originally made in cdio2, though improved
 */
public class LanguageManager {
    private Locale locale = null;
    private ResourceBundle messages = null;

    private static LanguageManager languageManager;

    private LanguageManager() {
        locale = Locale.getDefault();

        if (locale.getLanguage().equals(new Locale("en").getLanguage())) {
            locale = new Locale("en", "US");
        } else if (locale.getLanguage().equals(new Locale("da").getLanguage())) {
            locale = new Locale("da", "DK");
        } else {
            locale = new Locale("en", "US");
        }

        // TODO: remove following line after adding English
        locale = new Locale("da", "DK");

        // initialize
        messages = getMessages();
    }

    public static LanguageManager getInstance() {
        if (languageManager == null) {
            languageManager = new LanguageManager();
        }

        return languageManager;
    }

    /**
     * Function that takes either language of country, and by either language
     * or country decide the language of the game
     *
     * @param language the language you want
     * @param country  the country you want
     */
    public void setLocale(String language, String country) {
        locale = new Locale(language, country);
        messages = getMessages();
    }

    public void setLocale(String language) {
        if (language.contains("_")) {
            setLocale(language.split("_")[0], language.split("_")[1]);
        } else {
            locale = new Locale(language);
            messages = getMessages();
        }
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        messages = getMessages();
    }

    public String getLocale() {
        return locale.getDisplayLanguage();
    }

    private Locale[] getLocales() {
        ArrayList<Locale> locales = new ArrayList<>();

        URL url;
        for (Locale locale : Locale.getAvailableLocales()) {
            url = Game.class.getClassLoader().getResource("GameMessages_" + locale.toString() + ".properties");
//            url = Resources.getResource("GameMessages_"+locale.toString()+".properties");
            if (url != null) {
                locales.add(locale);
            }
        }
//        locales.add(new Locale("en", "US"));

        return locales.toArray(new Locale[0]);
    }

    public HashMap<String, Locale> getLocalesMap() {
        HashMap<String, Locale> locales = new HashMap<>();

        for (Locale locale : getLocales()) {
            locales.put(locale.getDisplayLanguage(), locale);
        }

        return locales;
    }

    /**
     * Function that returns the messages
     *
     * @return all the messages in the locale
     */
    private ResourceBundle getMessages() {
        return ResourceBundle.getBundle("GameMessages", locale);
    }

    /**
     * A function that takes a key for a specific message and returns
     * that message
     *
     * @param messageKey key for a specific message
     * @return returns the specific message
     */
    public String getString(String messageKey) {
        String message = "";
        try {
            message = messages.getString(messageKey);
        } catch (Exception e) {
            // Don't print the stacktrace when we run tests
            if (!messageKey.equals("non_existent_test_string")) {
                e.printStackTrace();
            }
            message = "Could not read message '" + messageKey + "' from locale " + locale.getLanguage();
        }
        // System.out.println(getLocale() + " - " + messageKey +": " + message);
        return message;
    }
}
