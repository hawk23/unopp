using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class Player
    {
        private static int nextID = 0;
        private static List<Player> players = new List<Player>();
        private static readonly object objLock = new object();

        public int id;
        public string name;
        public bool uno = false;
        public Location Location;
        public List<Card> Cards;

        public Player()
        {

        }

        public Player(string name)
        {
            this.Cards = new List<Card>();
            this.name = name;

            lock (objLock)
            {
                this.id = nextID++;
                Player.players.Add(this);
            }           
        }

        public Player(string name, double latitude,  double longitude)
        {
            this.Location = new Location(latitude, longitude);
            this.Cards = new List<Card>();

            new Player(name);
        }

        public static bool RemovePlayer(int playerID)
        {
            lock (objLock)
            {
                for(int i = 0; i < Player.players.Count(); i++)
                {
                    if (Player.players[i].id == playerID)
                    {
                        Player.players.RemoveAt(i);
                        return true;
                    }
                }
            }

            return false;
        }

        public static Player GetPlayerByID(int playerID)
        {
            foreach(Player player in Player.players)
            {
                if(player.id == playerID)
                {
                    return player;
                }
            }

            return null;
        }

        public static int GetActivePlayers(Player[] players)
        {
            int i = 0;

            for (; i < players.Count(); i++)
            {
                if (players[i] == null)
                {
                    return i;
                }
            }

            return i;
        }

        public void SortCards()
        {
            bool swapped = true;
            int j = 0;
            Card tmp;
            
            while (swapped)
            {
                swapped = false;
                j++;

                for (int i = 0; i < this.Cards.Count - j; i++)
                {
                    if (this.Cards[i].id > this.Cards[i + 1].id)
                    {
                        tmp = this.Cards[i];
                        this.Cards[i] = this.Cards[i + 1];
                        this.Cards[i + 1] = tmp;
                        swapped = true;
                    }
                }
            }
        }


    }
}