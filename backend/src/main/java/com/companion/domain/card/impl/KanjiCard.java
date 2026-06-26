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
public class KanjiCard implements Card {

    private record Kanji(String character, String reading, String meaning, String example, String exampleReading) {}

    private static final List<Kanji> DECK = List.of(
        new Kanji("日", "にち / ひ", "dia / sol", "今日", "きょう"),
        new Kanji("本", "ほん / もと", "livro / origem", "日本", "にほん"),
        new Kanji("人", "じん / ひと", "pessoa", "日本人", "にほんじん"),
        new Kanji("大", "だい / おお", "grande", "大学", "だいがく"),
        new Kanji("学", "がく / まな", "aprender", "学校", "がっこう"),
        new Kanji("時", "じ / とき", "tempo / hora", "時間", "じかん"),
        new Kanji("間", "かん / あいだ", "intervalo / entre", "時間", "じかん"),
        new Kanji("見", "けん / み", "ver", "見る", "みる"),
        new Kanji("言", "げん / い", "dizer / palavra", "言葉", "ことば"),
        new Kanji("年", "ねん / とし", "ano", "今年", "ことし"),
        new Kanji("気", "き", "energia / espírito", "元気", "げんき"),
        new Kanji("子", "し / こ", "criança / filho", "子供", "こども"),
        new Kanji("上", "じょう / うえ", "cima / superior", "上手", "じょうず"),
        new Kanji("下", "か / した", "baixo / inferior", "地下", "ちか"),
        new Kanji("来", "らい / く", "vir / próximo", "来月", "らいげつ"),
        new Kanji("国", "こく / くに", "país / nação", "外国", "がいこく"),
        new Kanji("食", "しょく / た", "comer / comida", "食事", "しょくじ"),
        new Kanji("行", "こう / い", "ir", "旅行", "りょこう"),
        new Kanji("出", "しゅつ / で", "sair / emitir", "出口", "でぐち"),
        new Kanji("会", "かい / あ", "encontrar / reunião", "会社", "かいしゃ")
    );

    @Override
    public String getId() { return "KanjiCard"; }

    @Override
    public String getTitle() { return "Kanji do Dia"; }

    @Override
    public int getPriority() { return 3; }

    @Override
    public Duration getDuration() { return Duration.ofSeconds(40); }

    @Override
    public boolean canDisplay(Context context) { return true; }

    @Override
    public CardData generate(Context context) {
        int index = (int) (LocalDate.now().toEpochDay() % DECK.size());
        Kanji k = DECK.get(index);

        return new CardData(getId(), getTitle(), getPriority(),
                (int) getDuration().toSeconds(),
                Map.of(
                        "character",       k.character(),
                        "reading",         k.reading(),
                        "meaning",         k.meaning(),
                        "example",         k.example(),
                        "exampleReading",  k.exampleReading()
                ));
    }
}
