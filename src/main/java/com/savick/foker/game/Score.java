package com.savick.foker.game;

import java.util.Iterator;
import java.util.List;

/**
 * @author Visith Dayananda.
 */

public class Score {
    //the value of a respective card is given exact value as the card rank.
    //the value of the risk when exchanging card is the value of the card rank which is being replaced
    //the initial cards are given a value of additional 10 points

    public int getHandScore(List<PlayerCard> oldHand, List<PlayerCard> newHand) {
        Integer totalOfInitialHand = getInitialHandTotal(oldHand);
        Integer totalOfReplacedHand = getReplaceHandTotal(newHand);
        Integer riskValue = getRiskValue(oldHand, newHand);
        Integer totalScore = getTotalScore(totalOfInitialHand, totalOfReplacedHand, riskValue);
        return totalScore;
    }

    public Integer getInitialHandTotal(List<PlayerCard> oldHand) {
        Integer beforeTotal = 0;

        if (!oldHand.isEmpty()) {
            for (Iterator<PlayerCard> it = oldHand.iterator(); it.hasNext(); ) {
                PlayerCard initialHand = it.next();

                Integer cardRank = initialHand.getRank();
                switch (cardRank) {
                    case 1:
                        if (!initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 14;
                        } else {
                            beforeTotal = beforeTotal + 14 + 10;
                        }
                        break;
                    case 2:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 2 + 10;
                        } else {
                            beforeTotal = beforeTotal + 2;
                        }
                        break;
                    case 3:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 3 + 10;
                        } else {
                            beforeTotal = beforeTotal + 3;
                        }
                        break;
                    case 4:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 4 + 10;
                        } else {
                            beforeTotal = beforeTotal + 4;
                        }
                        break;
                    case 5:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 5 + 10;
                        } else {
                            beforeTotal = beforeTotal + 14;
                        }
                        break;
                    case 6:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 6 + 10;
                        } else {
                            beforeTotal = beforeTotal + 6;
                        }
                        break;
                    case 7:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 7 + 10;
                        } else {
                            beforeTotal = beforeTotal + 7;
                        }
                        break;
                    case 8:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 8 + 10;
                        } else {
                            beforeTotal = beforeTotal + 8;
                        }
                        break;
                    case 9:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 9 + 10;
                        } else {
                            beforeTotal = beforeTotal + 14;
                        }
                        break;
                    case 10:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 10 + 10;
                        } else {
                            beforeTotal = beforeTotal + 10;
                        }
                        break;
                    case 11:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 11 + 10;
                        } else {
                            beforeTotal = beforeTotal + 11;
                        }
                        break;
                    case 12:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 12 + 10;
                        } else {
                            beforeTotal = beforeTotal + 12;
                        }
                        break;
                    case 13:
                        if (initialHand.isInitial()) {
                            beforeTotal = beforeTotal + 13 + 10;
                        } else {
                            beforeTotal = beforeTotal + 34;
                        }
                        break;
                }
            }
        }

        return beforeTotal;
    }

    public Integer getReplaceHandTotal(List<PlayerCard> newHand) {
        Integer AfterTotal = 0;
        if (!newHand.isEmpty()) {
            for (PlayerCard initialHand : newHand) {
                Integer cardRank = initialHand.getRank();
                switch (cardRank) {
                    case 1:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 14 + 10;
                        } else {
                            AfterTotal = AfterTotal + 14;
                        }
                        break;
                    case 2:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 2 + 10;
                        } else {
                            AfterTotal = AfterTotal + 2;
                        }
                        break;
                    case 3:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 3 + 10;
                        } else {
                            AfterTotal = AfterTotal + 3;
                        }
                        break;
                    case 4:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 4 + 10;
                        } else {
                            AfterTotal = AfterTotal + 4;
                        }
                        break;
                    case 5:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 5 + 10;
                        } else {
                            AfterTotal = AfterTotal + 14;
                        }
                        break;
                    case 6:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 6 + 10;
                        } else {
                            AfterTotal = AfterTotal + 6;
                        }
                        break;
                    case 7:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 7 + 10;
                        } else {
                            AfterTotal = AfterTotal + 7;
                        }
                        break;
                    case 8:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 8 + 10;
                        } else {
                            AfterTotal = AfterTotal + 8;
                        }
                        break;
                    case 9:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 9 + 10;
                        } else {
                            AfterTotal = AfterTotal + 14;
                        }
                        break;
                    case 10:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 10 + 10;
                        } else {
                            AfterTotal = AfterTotal + 10;
                        }
                        break;
                    case 11:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 11 + 10;
                        } else {
                            AfterTotal = AfterTotal + 11;
                        }
                        break;
                    case 12:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 12 + 10;
                        } else {
                            AfterTotal = AfterTotal + 12;
                        }
                        break;
                    case 13:
                        if (initialHand.isInitial()) {
                            AfterTotal = AfterTotal + 13 + 10;
                        } else {
                            AfterTotal = AfterTotal + 13;
                        }
                        break;
                }
            }
        }

        return AfterTotal;
    }

    private Integer getRiskValue(List<PlayerCard> oldHandForRisk, List<PlayerCard> newHandForRisk) {
        Integer calculatedRiskValue = 0;

        for (int i = 0; i < 5; i++) {
            PlayerCard oldHandCard = oldHandForRisk.get(i);
            PlayerCard newHandCard = newHandForRisk.get(i);

            if (oldHandCard.getRank() != newHandCard.getRank() || !oldHandCard.getSuit().equalsIgnoreCase(newHandCard.getSuit())) {
                if (oldHandCard.getRank() == 1) {
                    calculatedRiskValue = calculatedRiskValue + 14;
                } else {
                    calculatedRiskValue = calculatedRiskValue + oldHandCard.getRank();
                }
            } else if (i == 4) {
                calculatedRiskValue = 10;
            }
        }

        return calculatedRiskValue;
    }

    private Integer getTotalScore(Integer totalOfInitialHand, Integer totalOfReplacedHand, Integer riskValue) {
        Integer calculatedTotalScore;
        Integer scoreDifference = totalOfReplacedHand - totalOfInitialHand;
        Integer tempValue = 1;

        if (scoreDifference < 0) {
            tempValue = -1;
        }

        calculatedTotalScore = scoreDifference + (tempValue * riskValue);

        return calculatedTotalScore;
    }
}
