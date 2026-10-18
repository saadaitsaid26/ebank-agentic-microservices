package net.saad.ebankbot.telegram;

import jakarta.annotation.PostConstruct;
import net.saad.ebankbot.agents.EbankAIAgent;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.token}")
    private String telegramBotToken;

    private final EbankAIAgent aiAgent;

    public TelegramBot(EbankAIAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @PostConstruct
    public void registerTelegramBot() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du bot Telegram", e);
        }
    }

    @Override
    public void onUpdateReceived(Update telegraRequest) {
        try {
            if (!telegraRequest.hasMessage()) return;

            String messageText = telegraRequest.getMessage().getText();
            Long chatId = telegraRequest.getMessage().getChatId();
            String conversationId = String.valueOf(chatId);

            List<PhotoSize> photos = telegraRequest.getMessage().getPhoto();
            List<Media> mediaList = new ArrayList<>();
            String caption = null;

            if (photos != null && !photos.isEmpty()) {
                caption = telegraRequest.getMessage().getCaption();
                if (caption == null) caption = "Que vois-tu sur cette image ?";

                PhotoSize largestPhoto = photos.stream()
                        .max(Comparator.comparing(PhotoSize::getFileSize))
                        .orElse(photos.get(photos.size() - 1));

                String fileId = largestPhoto.getFileId();
                GetFile getFile = new GetFile();
                getFile.setFileId(fileId);

                File file = execute(getFile);

                String filePath = file.getFilePath();
                String textUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + filePath;
                URL fileUrl = new URL(textUrl);

                mediaList.add(new Media(MimeTypeUtils.IMAGE_PNG, new UrlResource(fileUrl)));
            }

            String query = messageText != null ? messageText : caption;
            if (query == null) return;

            // Utilisation du Builder officiel de UserMessage
            UserMessage userMessage = UserMessage.builder()
                    .text(query)
                    .media(mediaList)
                    .build();

            sendTypingQuestion(chatId);

            String answer = aiAgent.chat(new Prompt(userMessage), conversationId);

            sendTextMessage(chatId, answer);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du traitement du message Telegram", e);
        }
    }

    @Override
    public String getBotUsername() {
        return "EBANKAIBot";
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }

    private void sendTextMessage(long chatId, String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        execute(sendMessage);
    }

    private void sendTypingQuestion(long chatId) throws TelegramApiException {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(String.valueOf(chatId));
        sendChatAction.setAction(ActionType.TYPING);
        execute(sendChatAction);
    }
}