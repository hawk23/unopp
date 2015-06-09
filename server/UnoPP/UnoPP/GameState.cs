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
        public int direction;          //  -1: counter clockwise, 1: clockwise
        public int currentColor;       //  1: red, 2: yellow, 3: green, 4: blue, 5: black
        public Deck Deck;

        public GameState()
        {

        }

        public GameState(int currentPlayer, int nrPlayers, Player[] players, Deck Deck)
        {
            this.currentPlayer = currentPlayer;
            this.Deck = Deck;
            this.topCard = Deck.playedStack[0].id;

            // will be changed if a +2 card is played
            this.drawCounter = 0;

            // change parameters as top card requires
            this.currentColor = CheckColor(this.topCard);
            this.direction = CheckDirection(this.topCard);
            this.drawCounter = CheckDrawCounter(this.topCard);
            this.nextPlayer = CheckNextPlayer(this.direction, this.currentPlayer, players);
        }

        /// <summary>
        /// checks which color is assigned to the given id
        /// || 1: red, 2: yellow, 3: green, 4: blue, 5: black (special)
        /// </summary>
        public int CheckColor(int card)
        {
            // ASSIGN COLOR
            // red
            if ((0 <= card) && (card <= 24))
            {
                return 1;
            }
            // yellow
            else if ((25 <= card) && (card <= 49))
            {
                return 2;
            }
            // green
            else if ((50 <= card) && (card <= 74))
            {
                return 3;
            }
            // blue
            else if ((75 <= card) && (card <= 99))
            {
                return 4;
            }
            // random color is choosen
            else if ((100 <= card) && (card <= 103))
            {
                Random random = new Random();
                return random.Next(1, 5);
            }
            // error occurred: +4 is not possible as starting card
            else
            {
                return -1;
            }
        }

        /// <summary>
        /// checks if given id references to one of the 8 reverse cards
        /// -1: counter clockwise, 1: clockwise
        /// </summary>
        public int CheckDirection(int card)
        {
            // ASSIGN DIRECTION
            // direction is counter clockwise
            if (card == 21 || card == 22            // red reverse
                || card == 46 || card == 47         // yellow reverse
                || card == 71 || card == 72         // green reverse
                || card == 96 || card == 97)        // blue reverese
            {
                return -1;
            }
            // direction is clockwise
            else
            {
                return 1;
            }
        }

        /// <summary>
        /// checks if given id references to one of the 8 +2 cards
        /// adds up drawCounter + 2 if given
        /// </summary>
        public int CheckDrawCounter(int card)
        {
            // ASSIGN DRAWCOUNTER
            // check if a +2 card was drawn
            if (card == 23 || card == 24            // red +2
                || card == 48 || card == 49         // yellow +2
                || card == 73 || card == 74         // green +2
                || card == 98 || card == 99)        // blue +2
            {
                return this.drawCounter + 2;
            }
            else
            {
                return 0;
            }
        }

        /// <summary>
        /// returns id of next player
        /// </summary>
        public int CheckNextPlayer(int direction, int currentPlayer, Player[] players)
        {
            for (int i = 0; i < Player.GetActivePlayers(players); i++)
            {
                if (players[i].id == currentPlayer)
                {
                    if (direction == 1)
                    {
                        // checks if next player is not null or out of bounds
                        return players[(i + 1) % Player.GetActivePlayers(players)].id;
                    }
                    else if (direction == -1)
                    {
                        if(i < 0)
                        {
                            return players[Player.GetActivePlayers(players) - 1].id;
                        }
                        else
                        {
                            return players[i].id;
                        }
                    }
                    // error occurred:
                    else
                    {
                        return -1;
                    }
                }
            }
            
            return this.currentPlayer;
        }
    }
}
