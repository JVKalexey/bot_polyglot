package ru.org.botpolyglot;

public enum Languages {
    ENGLISH("en", "English"),
    SPANISH("es", "Spanish"),
    PORTUGUESE("pt", "Portuguese"),
    CHINESE("zh", "Chinese"),
    FRENCH("fr", "French"),
    CZECH("cs", "Czech");

    private String langCode;
    private String langName;

    Languages(String langCode, String langName) {
        this.langCode = langCode;
        this.langName = langName;
    }

    public String getLangCode() {
        return langCode;
    }

    public String getLangName() {
        return langName;
    }

    public static Languages getByCode(String langCode) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].langCode.equals(langCode)) {
                return values()[i];
            }
        }
        return ENGLISH;
    }

}
