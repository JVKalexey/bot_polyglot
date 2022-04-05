package ru.org.botpolyglot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.org.botpolyglot.translate.TranslationService;
import ru.org.botpolyglot.translate.YandexTranslate;

public class Main {
    public static void main(String[] args) {
        try {
            TranslationService ts = new YandexTranslate();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TranslateBot(ts));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
