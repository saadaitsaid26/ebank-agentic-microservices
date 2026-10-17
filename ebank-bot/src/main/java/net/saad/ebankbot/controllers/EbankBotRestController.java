package net.saad.ebankbot.controllers;

import net.saad.ebankbot.agents.EbankAIAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EbankBotRestController {
    private EbankAIAgent ebankAIAgent;
    public EbankBotRestController(EbankAIAgent ebankAIAgent){
        this.ebankAIAgent=ebankAIAgent;
    }

    @GetMapping("/chat")
    public String chat(
            @RequestParam(name = "query", defaultValue = "Bonjour") String query,
            @RequestParam(name = "conversationId", defaultValue = "user-1")
            String conversationId) {

        return ebankAIAgent.chat(query, conversationId);
    }

    @GetMapping("/test")
    public String test() {
        return "Le contrôleur fonctionne !";
    }


}
