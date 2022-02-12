package ru.loolzaaa.telegram.servicebot.core.bot;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.loolzaaa.telegram.servicebot.core.commands.StartCommand;
import ru.loolzaaa.telegram.servicebot.core.commands.currencies.CurrencyRatesCommand;
import ru.loolzaaa.telegram.servicebot.core.commands.currencies.RatesInlineMenuCommand;
import ru.loolzaaa.telegram.servicebot.core.service.RussianPostTrackingService;

import java.util.List;

public class ServiceLongPollingBot extends TelegramLongPollingCommandBot {

    private String nameVariable;
    private String tokenVariable;

    public ServiceLongPollingBot(String nameVariable, String tokenVariable) {
        this.nameVariable = nameVariable;
        this.tokenVariable = tokenVariable;
        register(new StartCommand("start", "Старт"));
        register(new RatesInlineMenuCommand("rates", "Меню курсов валют"));
        register(new CurrencyRatesCommand("rate", "Курс валют"));
    }

    public ServiceLongPollingBot() {
        this(null, null);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            processCallbackQueryUpdate(update);
            return;
        }

        String msgText = update.getMessage().getText().toUpperCase();

        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(String.format("Accepted: %s. Tracking...", msgText));

        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        try {
            String answer = RussianPostTrackingService.track(msgText);
            message.setText(answer);
        } catch (Exception e) {
            e.printStackTrace();
            message.setText("Ошибка");
        }

        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void processCallbackQueryUpdate(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        MessageEntity messageEntity = new MessageEntity("bot_command", 0, callbackQuery.getData().length());
        messageEntity.setText(callbackQuery.getData());

        Message message = callbackQuery.getMessage();
        message.setText(callbackQuery.getData());
        message.setEntities(List.of(messageEntity));

        update.setMessage(message);
        update.setCallbackQuery(null);
        try {
            execute(new AnswerCallbackQuery(callbackQuery.getId()));
            onUpdateReceived(update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processInvalidCommandUpdate(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText("Неизвестная команда");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return System.getenv(tokenVariable == null ? "bot_token" : tokenVariable);
    }

    @Override
    public String getBotUsername() {
        return System.getenv(nameVariable == null ? "bot_name" : nameVariable);
    }
}
