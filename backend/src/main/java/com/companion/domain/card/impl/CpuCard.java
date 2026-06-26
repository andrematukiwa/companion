package com.companion.domain.card.impl;

import com.companion.domain.card.Card;
import com.companion.domain.card.CardData;
import com.companion.domain.context.Context;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.time.Duration;
import java.util.Map;

@Component
public class CpuCard implements Card {

    private static final long MB = 1024 * 1024;

    @Override
    public String getId() { return "CpuCard"; }

    @Override
    public String getTitle() { return "System"; }

    @Override
    public int getPriority() { return 2; }

    @Override
    public Duration getDuration() { return Duration.ofSeconds(20); }

    @Override
    public boolean canDisplay(Context context) { return true; }

    @Override
    public CardData generate(Context context) {
        double cpuLoad = fetchCpuLoad();
        MemoryInfo memory = fetchMemory();

        return new CardData(getId(), getTitle(), getPriority(),
                (int) getDuration().toSeconds(),
                Map.of(
                        "cpuPercent",  Math.round(cpuLoad * 100.0),
                        "ramUsedMb",   memory.usedMb(),
                        "ramTotalMb",  memory.totalMb()
                ));
    }

    private double fetchCpuLoad() {
        var os = ManagementFactory.getOperatingSystemMXBean();
        if (os instanceof com.sun.management.OperatingSystemMXBean ext) {
            double load = ext.getCpuLoad();
            return load < 0 ? 0 : load;
        }
        return 0;
    }

    private MemoryInfo fetchMemory() {
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        long used  = mem.getHeapMemoryUsage().getUsed() / MB;
        long total = mem.getHeapMemoryUsage().getMax() / MB;
        return new MemoryInfo(used, total);
    }

    private record MemoryInfo(long usedMb, long totalMb) {}
}
