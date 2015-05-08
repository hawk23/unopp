using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class Deck
    {
        public Stack<Card> drawStack;
        public Stack<Card> playedStack;

        public Deck(Stack<Card> stack)
        {
            this.drawStack = Shuffle(stack);
            this.playedStack = new Stack<Card>();
            this.playedStack.Push(DrawCard(this));
        }

        public Stack<Card> Shuffle(Stack<Card> stack)
        {
            // TODO
            return stack;
        }

        public static Card DrawCard(Deck Deck)
        {
            return Deck.drawStack.Pop();
        }

        public static void PlayCard(Deck Deck, Card card)
        {
            Deck.playedStack.Push(card);
        }
    }
}