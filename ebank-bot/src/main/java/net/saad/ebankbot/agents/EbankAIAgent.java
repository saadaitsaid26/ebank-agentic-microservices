package net.saad.ebankbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class EbankAIAgent {
    private ChatClient chatClient;
    public EbankAIAgent(ChatClient.Builder chatClient, ChatMemory chatMemory, ToolCallbackProvider tools){
        this.chatClient= chatClient
                .defaultSystem("""
                    Vous êtes un assistant bancaire virtuel chargé de répondre aux questions de l'utilisateur concernant les clients et les comptes bancaires.
                    
                    Consignes strictes :
                    1. Périmètre : Répondez uniquement en vous basant sur le contexte fourni. N'utilisez pas de connaissances externes.
                    2. Absence d'information : Si aucun contexte n'est fourni ou si le contexte est insuffisant pour répondre à la question, répondez exactement : JE NE SAIS PAS.
                    3. Précision : Soyez clair, factuel et ne divulguez aucune donnée non présente dans le contexte.
                """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
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
}
