using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class Deck
    {
        public List<Card> drawStack;
        public List<Card> playedStack;

        public Deck()
        {

        }

        public Deck(List<Card> stack)
        {
            this.drawStack = Shuffle(stack);
            this.playedStack = new List<Card>();
            this.playedStack.Insert(0, DrawCard(this));
        }

        public List<Card> Shuffle(List<Card> stack)
        {
            // TODO
            return stack;
        }

        public static Card DrawCard(Deck deck)
        {
            Card card = deck.drawStack.Last();

            deck.drawStack.RemoveAt(deck.drawStack.Count-1);
            
            return card;
        }

        public static void PlayCard(Deck deck, Card card)
        {
            deck.playedStack.Insert(0, card);
        }
    }
}