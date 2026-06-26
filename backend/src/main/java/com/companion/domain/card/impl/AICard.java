package com.companion.domain.card.impl;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.companion.domain.card.Card;
import com.companion.domain.card.CardData;
import com.companion.domain.context.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AICard implements Card {

    private record CachedResponse(String topic, String insight, Instant cachedAt) {}

    private final AnthropicClient client;
    private final long cacheMinutes;
    private final AtomicReference<CachedResponse> cache = new AtomicReference<>();
    private final AtomicReference<Boolean> fetching = new AtomicReference<>(false);

    public AICard(
            @Value("${companion.ai.api-key:}") String apiKey,
            @Value("${companion.ai.cache-minutes:10}") long cacheMinutes) {
        this.cacheMinutes = cacheMinutes;
        this.client = AnthropicOkHttpClient.builder()
                .apiKey(apiKey == null || apiKey.isBlank() ? "placeholder" : apiKey)
                .build();
    }

    @Override
    public String getId() { return "AICard"; }

    @Override
    public String getTitle() { return "Insight do Dia"; }

    @Override
    public int getPriority() { return 3; }

    @Override
    public Duration getDuration() { return Duration.ofSeconds(25); }

    @Override
    public boolean canDisplay(Context context) { return true; }

    @Override
    public CardData generate(Context context) {
        CachedResponse cached = cache.get();

        if (cached != null && Instant.now().isBefore(cached.cachedAt().plusSeconds(cacheMinutes * 60))) {
            return buildCardData(cached);
        }

        if (fetching.compareAndSet(false, true)) {
            String project = context.activeProject() != null ? context.activeProject() : "desenvolvimento de software";
            CompletableFuture.runAsync(() -> fetchFromClaude(project));
        }

        if (cached != null) {
            return buildCardData(cached);
        }

        return new CardData(getId(), getTitle(), getPriority(), (int) getDuration().toSeconds(),
                Map.of("topic", "Carregando...", "insight", "Buscando insight via IA..."));
    }

    private void fetchFromClaude(String project) {
        try {
            Message message = client.messages().create(
                    MessageCreateParams.builder()
                            .model(Model.of("claude-opus-4-8"))
                            .maxTokens(200)
                            .addUserMessage(
                                    "Você é um assistente para um desenvolvedor. " +
                                    "Gere um insight técnico conciso sobre " + project + ". " +
                                    "Responda SOMENTE em JSON com este formato exato: " +
                                    "{\"topic\": \"<tema em 3-5 palavras>\", \"insight\": \"<dica prática em 1-2 frases>\"} " +
                                    "Sem markdown, sem explicação, apenas o JSON."
                            )
                            .build()
            );

            String text = message.content().get(0).text().map(t -> t.text()).orElse("");
            String cleaned = text.trim().replaceAll("^```json|```$", "").trim();

            String topic = extractJson(cleaned, "topic");
            String insight = extractJson(cleaned, "insight");

            if (!topic.isEmpty() && !insight.isEmpty()) {
                cache.set(new CachedResponse(topic, insight, Instant.now()));
            }
        } catch (Exception e) {
            cache.set(new CachedResponse("Dica Técnica",
                    "Escreva testes antes de refatorar — eles documentam o comportamento esperado e protegem contra regressões.",
                    Instant.now()));
        } finally {
            fetching.set(false);
        }
    }

    private String extractJson(String json, String key) {
        try {
            String marker = "\"" + key + "\"";
            int idx = json.indexOf(marker);
            if (idx < 0) return "";
            int colon = json.indexOf(":", idx);
            int start = json.indexOf("\"", colon + 1);
            int end = json.indexOf("\"", start + 1);
            return json.substring(start + 1, end);
        } catch (Exception e) {
            return "";
        }
    }

    private CardData buildCardData(CachedResponse r) {
        return new CardData(getId(), getTitle(), getPriority(), (int) getDuration().toSeconds(),
                Map.of("topic", r.topic(), "insight", r.insight()));
    }
}
