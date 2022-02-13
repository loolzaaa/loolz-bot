package ru.loolzaaa.telegram.servicebot.impl.binance;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.loolzaaa.telegram.servicebot.core.bot.config.BotConfiguration;
import ru.loolzaaa.telegram.servicebot.core.command.CommonCommand;

import java.util.List;

public class RatesInlineMenuCommand extends CommonCommand {

    public RatesInlineMenuCommand(String commandIdentifier, String description, BotConfiguration configuration) {
        super(commandIdentifier, description, configuration);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        InlineKeyboardButton.InlineKeyboardButtonBuilder keyboardButtonBuilder = InlineKeyboardButton.builder();
        InlineKeyboardButton USDRUBButton = keyboardButtonBuilder
                .text("USD/RUB")
                .callbackData("/rate usd rub")
                .build();
        InlineKeyboardButton ETHUSDButton = keyboardButtonBuilder
                .text("ETH/USD")
                .callbackData("/rate eth usd")
                .build();

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardMarkupBuilder = InlineKeyboardMarkup.builder();
        ReplyKeyboard replyKeyboard = keyboardMarkupBuilder.keyboardRow(List.of(USDRUBButton, ETHUSDButton)).build();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setReplyMarkup(replyKeyboard);
        message.setText("Выберите один из вариантов:");

        sendAnswer(absSender, message);
    }
}