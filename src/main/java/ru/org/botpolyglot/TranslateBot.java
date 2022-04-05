package ru.org.botpolyglot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.org.botpolyglot.translate.TranslationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslateBot extends TelegramLongPollingBot {

    private TranslationService translationService;
    private Map<Long, Languages> map;

    public TranslateBot(TranslationService translationService) {
        this.translationService = translationService;
        this.map = new HashMap<>();
    }

    @Override
    public String getBotUsername() {
        return AppProp.getProperty("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        return AppProp.getProperty("BOT_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasEntities()) {
            for (var command : update.getMessage().getEntities()) {
                String textCmd = command.getText();
                switch (textCmd) {
                    case "/start": sendChoiceLanguage(update);
                }
            }
        }

        else if (update.hasCallbackQuery()) {
            String selectedLanguage = update.getCallbackQuery().getData();
            Languages lang = Languages.getByCode(selectedLanguage);
            map.put(update.getCallbackQuery().getMessage().getChatId(), lang);
            answerCallbackQuery(update.getCallbackQuery().getId(), lang+" set as default language!");
        }

        else if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            String translateStr;
            Languages currentLanguage = map.get(update.getMessage().getChatId());
            if (currentLanguage == null) {
                currentLanguage = Languages.ENGLISH;
            }
            try {
                translateStr = translationService.translateText(currentLanguage.getLangCode(), update.getMessage().getText());
            } catch (IOException e) {
                translateStr = e.getMessage();
            }
            message.setText(translateStr);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    private void answerCallbackQuery(String callbackId, String message) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        answer.setText(message);
        answer.setShowAlert(true);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void sendChoiceLanguage(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(AppProp.getProperty("LANGUAGE_MESSAGE"));

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();

        List<InlineKeyboardButton> buttonRow1 = new ArrayList<>();
        buttonRow1.add(InlineKeyboardButton.builder().text(Languages.ENGLISH.getLangName()).callbackData(Languages.ENGLISH.getLangCode()).build());
        buttonRow1.add(InlineKeyboardButton.builder().text(Languages.SPANISH.getLangName()).callbackData(Languages.SPANISH.getLangCode()).build());
        buttonRow1.add(InlineKeyboardButton.builder().text(Languages.PORTUGUESE.getLangName()).callbackData(Languages.PORTUGUESE.getLangCode()).build());
        buttonRows.add(buttonRow1);

        List<InlineKeyboardButton> buttonRow2 = new ArrayList<>();
        buttonRow2.add(InlineKeyboardButton.builder().text(Languages.CHINESE.getLangName()).callbackData(Languages.CHINESE.getLangCode()).build());
        buttonRow2.add(InlineKeyboardButton.builder().text(Languages.FRENCH.getLangName()).callbackData(Languages.FRENCH.getLangCode()).build());
        buttonRow2.add(InlineKeyboardButton.builder().text(Languages.CZECH.getLangName()).callbackData(Languages.CZECH.getLangCode()).build());
        buttonRows.add(buttonRow2);

        inlineKeyboard.setKeyboard(buttonRows);
        message.setReplyMarkup(inlineKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
