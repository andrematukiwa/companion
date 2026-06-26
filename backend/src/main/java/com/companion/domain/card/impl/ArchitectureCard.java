package com.companion.domain.card.impl;

import com.companion.domain.card.Card;
import com.companion.domain.card.CardData;
import com.companion.domain.context.Context;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class ArchitectureCard implements Card {

    private record Tip(String title, String body) {}

    private static final List<Tip> TIPS = List.of(
        new Tip("Single Responsibility", "Uma classe deve ter apenas um motivo para mudar. Se você consegue descrever o que ela faz usando 'e', ela tem responsabilidades demais."),
        new Tip("Dependency Inversion", "Dependa de abstrações, não de implementações. Módulos de alto nível não devem conhecer detalhes de baixo nível."),
        new Tip("Tell, Don't Ask", "Em vez de perguntar o estado de um objeto e decidir o que fazer, diga ao objeto o que fazer. Mantenha a lógica onde está o dado."),
        new Tip("Ports & Adapters", "Separe o núcleo da aplicação de infraestrutura. O domínio não sabe se fala com um banco, uma API ou um arquivo."),
        new Tip("Fail Fast", "Valide entradas na borda do sistema. Quanto mais cedo um erro é detectado, mais barato ele é para corrigir."),
        new Tip("Strangler Fig", "Para migrar um sistema legado, envolva-o gradualmente com o novo. Nunca reescreva tudo de uma vez."),
        new Tip("CQRS", "Separar leituras de escritas permite otimizar cada lado independentemente. Mas adicione complexidade só se o problema justificar."),
        new Tip("Idempotência", "Operações idempotentes podem ser repetidas com segurança. Essencial para retries em sistemas distribuídos."),
        new Tip("Lei de Demeter", "Um objeto deve falar apenas com seus vizinhos imediatos. Encadeamentos longos criam acoplamento frágil."),
        new Tip("Coesão Alta", "O código que muda junto deve ficar junto. Coesão alta significa que cada módulo tem um propósito claro e focado."),
        new Tip("Open/Closed", "Aberto para extensão, fechado para modificação. Adicione comportamento sem alterar o que já funciona."),
        new Tip("Event Sourcing", "Guarde eventos, não estado. O estado atual é sempre a projeção de todos os eventos desde o início."),
        new Tip("Bounded Context", "Cada contexto tem seu próprio modelo e linguagem. Não force um único modelo para todo o sistema."),
        new Tip("Circuit Breaker", "Quando um serviço externo falha repetidamente, pare de chamar por um tempo. Proteja o sistema de falhas em cascata."),
        new Tip("Composition over Inheritance", "Prefira compor comportamentos a herdar implementações. Herança cria acoplamento vertical difícil de quebrar.")
    );

    @Override
    public String getId() { return "ArchitectureCard"; }

    @Override
    public String getTitle() { return "Arquitetura"; }

    @Override
    public int getPriority() { return 4; }

    @Override
    public Duration getDuration() { return Duration.ofSeconds(30); }

    @Override
    public boolean canDisplay(Context context) { return true; }

    @Override
    public CardData generate(Context context) {
        int index = (int) (LocalDate.now().toEpochDay() % TIPS.size());
        Tip tip = TIPS.get(index);

        return new CardData(getId(), getTitle(), getPriority(),
                (int) getDuration().toSeconds(),
                Map.of(
                        "tipTitle", tip.title(),
                        "tipBody",  tip.body()
                ));
    }
}
