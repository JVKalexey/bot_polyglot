package ru.org.botpolyglot.translate;

import java.io.IOException;

public interface TranslationService {
    String translateText(String targetLanguageCode, String textToTranslate) throws IOException;
}
