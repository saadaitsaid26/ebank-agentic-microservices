
package net.saad.ebankbot.controllers;

import net.saad.ebankbot.agents.EbankAIAgent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EbankBotRestController {
    private final EbankAIAgent ebankAIAgent;

    public EbankBotRestController(EbankAIAgent ebankAIAgent) {
        this.ebankAIAgent = ebankAIAgent;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam(name = "query", defaultValue = "Bonjour") String query,
            @RequestParam(name = "conversationId", defaultValue = "user-1")
            String conversationId) {
        try {
            String response = ebankAIAgent.chat(query, conversationId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Throwable root = e;
            while (root.getCause() != null && root.getCause() != root) {
                root = root.getCause();
            }
            String errorMsg = "Erreur: " + e.getMessage() + (root != e ? " | Cause: " + root.getMessage() : "");
            return ResponseEntity.status(500).body(errorMsg);
        }
    }

    @GetMapping("/test")
    public String test() {
        return "Le contrôleur fonctionne !";
    }


}
