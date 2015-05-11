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

            lock (objLock)
            {
                this.id = nextID++;
                this.name = SetName(name, 0);
                Player.players.Add(this);
            }           
        }

        public Player(string name, double latitude,  double longitude)
        {
            this.Location = new Location(latitude, longitude);
            this.Cards = new List<Card>();

            new Player(name);
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

        private string SetName(string name, int count)
        {
            bool changeName = false;

            for (int i = 0; i < Player.players.Count; i++)
            {
                // name is already taken
                if((0 == String.Compare(Player.players[i].name, name) && count == 0) || (0 == String.Compare(Player.players[i].name + " (" + count + ")", name)))
                {
                    count++;
                    changeName = true;
                    break;
                }
            }   
            
            if (changeName)
            {
                return SetName(name, count);
            }
            if (count >= 1)
            {
                return name + " (" + count + ")";
            }
            else
            {
                return name;
            }
        }
    }
}