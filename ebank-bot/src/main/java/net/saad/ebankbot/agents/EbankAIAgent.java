package net.saad.ebankbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class EbankAIAgent {
    private final ChatClient chatClient;

    public EbankAIAgent(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, ToolCallbackProvider tools) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                    Vous êtes un assistant bancaire virtuel intelligent.
                    Vous disposez d'outils (MCP) pour accéder en temps réel aux données bancaires.

                    Consignes :
                    1. Utilisez systématiquement les outils disponibles pour répondre aux questions sur les clients et les comptes bancaires.
                    2. Répondez de manière polie, claire et concise en français.
                    3. Si une information reste introuvable même après utilisation des outils, indiquez-le clairement.
                    4. Ne refusez jamais d'utiliser les outils — ils sont votre principale source d'information.
                """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultTools(tools)
                .build();
    }

    public String chat(String query, String conversationId) {
        return chatClient.prompt()
                .user(query)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    // Nouvelle méthode à ajouter pour les objets Prompt (texte + images)
    public String chat(Prompt prompt, String conversationId) {
        return chatClient.prompt(prompt)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    public Flux<String> chatStream(Prompt prompt, String conversationId) {
        return chatClient.prompt(prompt)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }


}
