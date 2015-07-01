using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class GameSession
    {
        private static int nextID = 0;
        private static readonly object objLock = new object();

        public int id;
        public int maxPlayers;
        public bool started;                    // game state
        public string name;
        public int hostID;                      // host id
        public int mode = 0;                    // cheating
        public List<GameRound> GameRounds;
        public Location Location;
        public Player[] Players;
    
        public GameSession()
        {

        }

        public GameSession(string gameName, int playerID, double latitude, double longitude, int maxPlayers)
        {
            lock(objLock)
            {
                this.id = nextID++;
            }
            
            this.hostID = playerID;
            this.started = false;
            this.Location = new Location(latitude, longitude);
            this.maxPlayers = maxPlayers;
            this.name = gameName;
            this.Players = new Player[maxPlayers];
            this.GameRounds = new List<GameRound>();
        }

        public GameSession(string gameName, int playerID, double latitude, double longitude, int maxPlayers, int mode) : this(gameName, playerID, latitude, longitude, maxPlayers)
        {
            this.mode = mode;
        }

        /*
        private string SetName(string name, int count)
        {
            bool changeName = false;

            for (int i = 0; i < Player.players.Count; i++)
            {
                // name is already taken
                if ((0 == String.Compare(Player.players[i].name, name) && count == 0) || (0 == String.Compare(Player.players[i].name + " (" + count + ")", name)))
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
        */
    }
}