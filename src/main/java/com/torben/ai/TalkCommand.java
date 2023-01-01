package com.torben.ai;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class TalkCommand implements CommandExecutor, Listener {

    private Main main;

    public TalkCommand(Main main) {
        this.main = main;
    }

    private OpenAiService service = new OpenAiService("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", 0);
    private HashMap<UUID, StringBuilder> conversations = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (conversations.containsKey(player.getUniqueId())) {
                conversations.remove(player.getUniqueId());
                player.sendMessage(ChatColor.RED + "Your Conversation with Chat GPT has ended.");
            } else {
                conversations.put(player.getUniqueId(), new StringBuilder("The following is a conversation with an AI assistant. The assistant is helpful, creative, clever, and very friendly.\n" +
                        "\n" +
                        "Human: Hello!\n" +
                        "AI: "));
                player.sendMessage(ChatColor.GREEN + "You Have Started a conversation with ChatGPT, Say Hi.");
            }
        }

        return false;
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (conversations.containsKey(player.getUniqueId())) {
            e.setCancelled(true);

            player.sendMessage("You: " + e.getMessage());

            Bukkit.getScheduler().runTaskAsynchronously(main, () -> player.sendMessage("AI: " + getResponce(player.getUniqueId(), e.getMessage())));
        }
    }

    private String getResponce(UUID uuid, String message){
        conversations.get(uuid).append("\nHuman:").append(message).append("\nAI:");
        CompletionRequest request = CompletionRequest.builder()
                .prompt(conversations.get(uuid).toString())
                .model("text-davinci-003")
                .temperature(0.9D)
                .maxTokens(150)
                .topP(1.0)
                .frequencyPenalty(0D)
                .presencePenalty(0.6D)
                .stop(Arrays.asList("Human:", "AI:"))
                .build();
        return service.createCompletion(request).getChoices().get(0).getText();
    }
}

