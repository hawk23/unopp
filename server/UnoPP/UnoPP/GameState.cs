using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class GameState
    {
        public int topCard;
        public int drawCounter;
        public int currentPlayer;
        public int nextPlayer;
        public int direction;          //  1: clockwise, 2: counterclocwise
        public int currentColor;       //  1: red, 2: yellow, 3: green, 4: blue, 5: black
        public Deck Deck;

        public GameState()
        {

        }

        public GameState(int currentPlayer, int nrPlayers, Deck Deck)
        {
            this.currentPlayer = currentPlayer;
            this.Deck = Deck;
            this.topCard = Deck.playedStack[0].id;
            this.drawCounter = 1;

            this.currentColor = CheckColor(this.topCard);
            this.direction = CheckDirection(this.topCard);

            // TODO check if last player --> next player is first player
            if(direction == 1)
            {
                this.nextPlayer = this.currentPlayer + 1;
            }
            else if(direction == 2)
            {
                this.nextPlayer = this.currentPlayer - 1;
            }

            this.Deck = Deck;
        }

        /// <summary>
        /// checks which color is assigned to the given id
        /// || 1: red, 2: yellow, 3: green, 4: blue, 5: black (special)
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public int CheckColor(int id)
        {
            // red
            if ((0 <= this.topCard) && (this.topCard >= 24))
            {
                return 1;
            }
            // yellow
            else if ((25 <= this.topCard) && (this.topCard >= 49))
            {
                return 2;
            }
            // green
            else if ((50 <= this.topCard) && (this.topCard >= 74))
            {
                return 3;
            }
            // blue
            else if ((75 <= this.topCard) && (this.topCard >= 99))
            {
                return 4;
            }
            // special card: 100 - 107
            else
            {
                return 5;
            }
        }

        /// <summary>
        /// checks given id references to one of the 8 reverse cards
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public int CheckDirection(int id)
        {
            // counter clockwise
            if (this.topCard == 21 || this.topCard == 22            // red reverse
                || this.topCard == 46 || this.topCard == 47         // yellow reverse
                || this.topCard == 71 || this.topCard == 72         // green reverse
                || this.topCard == 96 || this.topCard == 97)        // blue reverese
            {
                return 2;
            }
            // clockwise
            else
            {
                return 1;
            }
        }
    }
}
