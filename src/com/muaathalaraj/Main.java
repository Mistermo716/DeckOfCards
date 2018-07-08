package com.muaathalaraj;
import java.util.Scanner;
import java.util.*;

// Assignment 3
// Students:
// Muaath Alaraj
// Christopher Caldwell
// Doan Dinh
// Corey Johnson
// Date: 7/10/2018

// Description:
//

public class Main
{
    public static void main(String[] args)
    {

        //Phase 4 Test Run

        //Create new deck with 2 decks total
        Deck newDeck = new Deck(2);

        //deal each card.. right now all cards are in order.
        for(int i = 0; i < newDeck.getNumPacks() * 52; i++) {
            System.out.println(i + ": " + newDeck.dealCard().toString());
        }
        //reinitialize the deck which is now been completely used.
        newDeck.init(2);
        //shuffle the deck
        newDeck.shuffle();
        //now the deck is shuffled and should output in a different order.
        for(int i = 0; i < newDeck.getNumPacks() * 52; i++) {
            System.out.println(i + ": " + newDeck.dealCard().toString());
        }
    }
}

class Card
{
    // Suit Enumeration
    public enum Suit
    {
        spades, hearts, diamonds, clubs
    }
    //added to set char value
    public static char[] cardCharValues = {'A', '2','3', '4', '5', '6', '7',
          '8', '9', 'T', 'J', 'Q', 'K'};

    // Data section
    private boolean errorFlag;
    public boolean getErrorFlag()
    {
        return this.errorFlag;
    }

    private char value;
    public char getValue()
    {
        return value;
    }
    private void setValue(char value)
    {
        this.value = value;
    }

    private Suit suit;
    public Suit getSuit()
    {
        return suit;
    }
    private void setSuit(Suit suit)
    {
        this.suit = suit;
    }

    public boolean set(char value, Suit suit)
    {
        if (isValid(value, suit))
        {
            setValue(value);
            setSuit(suit);
            errorFlag = false;
        }
        else
        {
            errorFlag = true;
        }

        return errorFlag;
    }

    // Constructors
    public Card()
    {
        set(getDefaultValue(), getDefaultSuit());
    }

    public Card(char value, Suit suit)
    {
        set(value, suit);
    }

    public Card(Card card)
    {
        set(card.getValue(), card.getSuit());
    }

    // Private Helper Methods

    //Check if valid card (does not examine suit)
    private boolean isValid(char value, Suit suit)
    {
        // Define all valid values.
        char[] validValues = getValidValues();

        // Iterate through all valid values testing if in set.
        // Return true if valid value in valid char array.
        for (int valId = 0; valId < validValues.length; valId++)
            if (validValues[valId] == value)
                return true;

        // return false if valid value not found.
        return false;
    }

    public static char[] getValidValues()
    {
        return new char[]{'A', '2','3', '4', '5', '6', '7',
              '8', '9', 'T', 'J', 'Q', 'K'};
    }

    public static Suit[] getValidSuits()
    {
        return new Suit[] {Suit.spades, Suit.hearts,
              Suit.diamonds, Suit.clubs};
    }

    public static char getDefaultValue()
    {
        return 'A';
    }

    public static Suit getDefaultSuit()
    {
        return Suit.spades;
    }

    public static Card getBadCard()
    {
        return new Card(' ', getDefaultSuit());
    }

    // Output
    public String toString()
    {
        if (!errorFlag)
            return value + " of " + suit.name();
        else
            return "** illegal **";
    }

    // Equals
    // Returns true if member values are identical to parameter member values.
    public boolean equals(Card card)
    {
        if (card == null)
            return false;
        else if (this.getValue() == card.getValue() &&
              this.getSuit() == card.getSuit())
            return true;

        return false;
    }

}

class Hand
{
    public final int MAX_CARDS = 50;
    private Card[] myCards;
    private int numCards;

    // default constructor
    public Hand()
    {
        numCards = 0;
        myCards = new Card[MAX_CARDS];
    }

    // remove all cards
    public void resetHand()
    {
        numCards = 0;
        myCards = new Card[MAX_CARDS];
    }

    // Adds a card to the hand - accepts a Card
    public boolean takeCard(Card card)
    {
        if(numCards >= MAX_CARDS || card.getErrorFlag())
        {
            return false;
        }
        myCards[numCards] = new Card(card.getValue(), card.getSuit());
        numCards++;
        return true;
    }

    // return and remove the top card - returns a Card
    public Card playCard()
    {
        numCards--;
        Card temp = myCards[numCards];
        myCards[numCards] = null;
        return temp;
    }

    // returns a string consisting of entire hand
    public String toString()
    {
        String stringOfCards = "";
        for(int i = 0 ; i < numCards; i++)
        {
            stringOfCards += myCards[i].toString();
            if(i < numCards - 1)
            {
                stringOfCards += ", ";
            }
            if(stringOfCards.length() % 80 > 65)
            {
                stringOfCards += "\n";
            }
        }
        return stringOfCards;
    }

    // returns the number of cards held in the hand - returns an int
    public int getNumCards()
    {
        return numCards;
    }

    // returns the specified card - accepts int - returns Card
    public Card inspectCard(int k)
    {
        if(k >= numCards)
        {
            return new Card('e', Card.Suit.hearts);
        }
        return myCards[k];
    }
}

class Deck
{
    public static final int MAX_CARDS = 6*52;
    public static Card [] masterPack = new Card[52];

    private Card[] cards;
    private int topCard;
    private int numPacks;

    public Deck(int numPacks){

        //while numPacks is larger than amount we will ask user to enter
        //less than 6 packs.
        while(52*numPacks > MAX_CARDS){
            Scanner input = new Scanner(System.in);
            System.out.println("please enter a number of decks 6 or less");
            numPacks = input.nextInt();
        }

        allocateMasterPack();

        init(numPacks);

    }

    //will initalize or reintialize a deck to default
    public void init(int numPacks){
        this.cards = new Card[52 * numPacks];
        this.numPacks = numPacks;
        this.topCard = 0;

        for(int i = 0; i < cards.length; i++){

            //mod 52 to adjust for multiple decks.
            //each deck contains 52 cards and 52%52 will start new deck.
            cards[i] = new Card(masterPack[i%52].getValue(),
                  masterPack[i%52].getSuit());

            //topCard is the top of the deck
            //Could really use a stack here.
            //every time a card is added to the deck its added to the top.
            topCard++;
        }

    }

    //will deal the top card
    public Card dealCard(){
        if(topCard == 0){
            return null;
        }

        //must deal the topCard - 1 out of the deck.
        Card card = new Card(cards[topCard-1].getValue(),
              cards[topCard-1].getSuit());

        cards[topCard-1] = null;
        topCard--;
        return card;
    }

    //will shuffle cards very well.
    public void shuffle(){
        for(int i = 0; i < cards.length; i++){

            //to randomly generate a set of numbers but not duplicate those numbers
            Random rand = new Random();
            int randIndex = rand.nextInt(52* this.numPacks);

            Card temporary;

            //shuffling the cards by placing card at current index to new index.
            temporary = cards[i];
            cards[i] = cards[randIndex];
            cards[randIndex] = temporary;

        }
    }

    //will create master pack
    private static void allocateMasterPack(){
        if(masterPack[51] != null){
            return;
        }

        //must initialize to suit variable to avoid problems
        Card.Suit cardSuit = Card.Suit.hearts;

        //will loop through masterPack length
        for(int i = 0; i < masterPack.length; i++){
            if(i < 13){
                cardSuit = Card.Suit.hearts;
            }
            if(i >= 13 && i < 26){
                cardSuit = Card.Suit.diamonds;
            }
            if(i >= 26 && i < 39){
                cardSuit = Card.Suit.clubs;
            }
            else if (i>= 39 && i < 52 ){
                cardSuit = Card.Suit.spades;
            }

            //at each iteration masterPack array index will be set to card values.
            masterPack[i] = new Card(Card.cardCharValues[i % 13], cardSuit);
        }
    }

    //will return the number of packs.
    public int getNumPacks() {
        return this.numPacks;
    }


    //will inspect card at an index without removing it
    public Card inspectCard(int k){

        //if k is within the length of the deck return the card
        if(k < this.cards.length){
            return cards[k];
        }

        //otherwise flag error.
        //returns bad card to flag error.
        Card newCard = new Card('m', Card.Suit.hearts);
        return newCard;
    }

}