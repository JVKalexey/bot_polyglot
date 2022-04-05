package ru.org.botpolyglot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProp {
    private static final Properties appProp;

    static {
        appProp = new Properties();
        InputStream is = Main.class.getClassLoader().getResourceAsStream("app.properties");
        try {
            appProp.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String name) {
        return appProp.getProperty(name);
    }

    private AppProp() {

    }
}
