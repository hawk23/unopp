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
            this.id = nextID++;
            this.name = name;
        }

        public Player(string name, double latitude,  double longitude)
        {
            this.name = name;
            this.Location = new Location(latitude, longitude);

            lock(objLock)
            {
                this.id = nextID++;
                Player.players.Add(this);
            }
        }

        public static Player getPlayerByID(int playerID)
        {
            try
            {
                return Player.players[playerID];
            }
            catch (ArgumentOutOfRangeException ex)
            {
                Console.WriteLine("Error in getPlayerByID: " + ex.Message);
                return null;
            }
        }
    }
}