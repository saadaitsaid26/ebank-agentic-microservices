package net.saad.ebankbot.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EbankBotRestController {
    private ChatClient chatClient;
    public EbankBotRestController(ChatClient.Builder chatClient, ChatMemory chatMemory){
        this.chatClient= chatClient
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "query", defaultValue = "Bonjour") String query){
        return chatClient.prompt(query).call().content();
    }

    @GetMapping("/test")
    public String test() {
        return "Le contrôleur fonctionne !";
    }


}
