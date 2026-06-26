package com.companion.domain.card.impl;

import com.companion.domain.card.Card;
import com.companion.domain.card.CardData;
import com.companion.domain.context.Context;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Map;

@Component
public class GitCard implements Card {

    @Override
    public String getId() { return "GitCard"; }

    @Override
    public String getTitle() { return "Git"; }

    @Override
    public int getPriority() { return 2; }

    @Override
    public Duration getDuration() { return Duration.ofSeconds(15); }

    @Override
    public boolean canDisplay(Context context) {
        return context.hasActiveProject() || context.activeBranch() != null;
    }

    @Override
    public CardData generate(Context context) {
        String branch  = context.activeBranch() != null ? context.activeBranch() : runGit("rev-parse", "--abbrev-ref", "HEAD");
        int ahead      = parseIntSafe(runGit("rev-list", "--count", "@{u}..HEAD"));
        int changed    = parseIntSafe(runGit("diff", "--name-only", "--cached", "--diff-filter=ACM").lines().count() + "");

        return new CardData(getId(), getTitle(), getPriority(),
                (int) getDuration().toSeconds(),
                Map.of(
                        "branch",  branch != null ? branch : "unknown",
                        "ahead",   ahead,
                        "changed", changed
                ));
    }

    private String runGit(String... args) {
        try {
            String[] cmd = new String[args.length + 1];
            cmd[0] = "git";
            System.arraycopy(args, 0, cmd, 1, args.length);
            Process proc = new ProcessBuilder(cmd)
                    .redirectErrorStream(true)
                    .start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                return reader.readLine();
            }
        } catch (Exception ex) {
            return null;
        }
    }

    private int parseIntSafe(String value) {
        if (value == null) return 0;
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return 0; }
    }
}
