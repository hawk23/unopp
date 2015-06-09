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

            bool isPlus4 = false;

            do
            {
                // if first card on played stack is a +4 a new card has to be drawn
                if (isPlus4 = (this.playedStack.First().id <= 107 && this.playedStack.First().id >= 104))
                {
                    // first card in list is shown 
                    this.playedStack.Insert(0, DrawCard(this));
                }
            } while (isPlus4);
        }

        public List<Card> Shuffle(List<Card> stack)
        {
            Random rand = new Random(new System.DateTime().Millisecond);
            Card temp;
            int x, y;

            for (int i = 0; i < (stack.Count * 5); i++)
            {
                x = rand.Next(0, stack.Count);
                y = rand.Next(0, stack.Count);
                temp = stack[x];
                stack[x] = stack[y];
                stack[y] = temp;
            }
            
            return stack;
        }

        public static Card DrawCard(Deck deck)
        {
            Card card = deck.drawStack.Last();

            deck.drawStack.RemoveAt(deck.drawStack.Count - 1);
            
            return card;
        }

        public static void PlayCard(Deck deck, Card card)
        {
            deck.playedStack.Insert(0, card);
        }
    }
}