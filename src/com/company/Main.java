package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static HashSet<Card> createDeck() {
        HashSet<Card> deck = new HashSet<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        return deck;
    }

    public static HashSet<HashSet<Card>> createHands (HashSet<Card> deck) {
        HashSet<HashSet<Card>> hands = new HashSet<>();
        for (Card c1 : deck) {
            HashSet<Card> deck2 = (HashSet<Card>) deck.clone();
            deck2.remove(c1);
            for (Card c2 : deck2) {
                HashSet<Card> deck3 = (HashSet<Card>) deck2.clone();
                deck3.remove(c2);
                for (Card c3 : deck3) {
                    HashSet<Card> deck4 = (HashSet<Card>) deck3.clone();
                    deck4.remove(c3);
                    for (Card c4 : deck4) {
                        HashSet<Card> hand = new HashSet<>();
                        hand.add(c1);
                        hand.add(c2);
                        hand.add(c3);
                        hand.add(c4);
                        hands.add(hand);
                    }
                }
            }
        }
        return hands;
    }

    public static boolean isFlush(HashSet<Card> hand) {
        HashSet<Card.Suit> suits =
                hand.stream()
                        .map(card -> {
                            return card.suit;
                        })
                        .collect(Collectors.toCollection(HashSet<Card.Suit>::new));
        return suits.size() == 1;
    }

    public static boolean isStraight(HashSet<Card> hand) {
        HashSet<Card.Rank> ranks =
                hand.stream()
                        .map(card -> {
                            return card.rank;
                        })
                        .collect(Collectors.toCollection(HashSet<Card.Rank>::new));
        if (ranks.size() == 4) {

            ArrayList<Integer> ranksList = new ArrayList<>();
            ranks.forEach(rank -> ranksList.add(rank.ordinal()));
            Collections.sort(ranksList);

            return ((ranksList.get(ranksList.size() - 1) - ranksList.get(0)) == 3);
        } else {
            return false;
        }
    }

    public static boolean isStraitFlush(HashSet<Card> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    public static boolean isFourOfaKind(HashSet<Card> hand) {
        TreeMap<Integer, Integer> freqMap = createFreqMap(hand);
        return freqMap.size() == 1;
    }

    public static boolean isThreeOfaKind(HashSet<Card> hand) {
        TreeMap<Integer, Integer> freqMap = createFreqMap(hand);
        return freqMap.containsValue(3);
    }

    public static boolean isTwoPair(HashSet<Card> hand) {
        TreeMap<Integer, Integer> freqMap = createFreqMap(hand);
        return ((freqMap.size() == 2) && (freqMap.containsValue(2)));
    }

    //find this alg here http://www.mkyong.com/java/how-to-count-duplicated-items-in-java-list/
    public static TreeMap<Integer, Integer> createFreqMap(HashSet<Card> hand) {
        TreeMap<Integer, Integer> freqMap = new TreeMap<>();
        for (Card c : hand) {
            Integer count = freqMap.get(c.rank.ordinal());
            freqMap.put(c.rank.ordinal(), (count == null) ? 1 : count + 1);
        }
        return freqMap;
    }

    public static Integer countStuff(HashSet<HashSet<Card>> hands, Function<HashSet<Card>, Boolean> method) {
        hands = hands.stream()
                .filter((hand)  ->  {
                    return method.apply(hand);
                })
                .collect(Collectors.toCollection(HashSet<HashSet<Card>>::new));


        return hands.size();
    }

    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();

        HashSet<Card> deck = createDeck();
        HashSet<HashSet<Card>> hands = createHands(deck);
        System.out.println("total hands: " + hands.size());

        System.out.println("Total Flushes: " + countStuff(hands, Main::isFlush));
        System.out.println("Total Straits: " + countStuff(hands, Main::isStraight));
        System.out.println("Total Strait Flushes: " + countStuff(hands, Main::isStraitFlush));
        System.out.println("Total Four of a Kind: " + countStuff(hands, Main::isFourOfaKind));
        System.out.println("Total Three of a Kind: " + countStuff(hands, Main::isThreeOfaKind));
        System.out.println("Total Two Pair: " + countStuff(hands, Main::isTwoPair));

        long endTime = System.currentTimeMillis();

        System.out.printf("Elapsed Time: %d msecs\n", endTime - beginTime);
    }
}