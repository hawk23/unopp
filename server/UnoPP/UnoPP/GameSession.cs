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
        public bool started;                // game state
        public string name;
        public int host;                    // host id
        public int mode = 0;
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
            
            this.host = playerID;
            this.started = false;
            this.Location = new Location(latitude, longitude);
            this.maxPlayers = maxPlayers;
            this.name = gameName;
            this.Players = new Player[maxPlayers];
            this.GameRounds = new List<GameRound>();
        }

        public GameSession(string gameName, int playerID, double latitude, double longitude, int maxPlayers, int mode)
        {
            new GameSession(gameName, playerID, latitude, longitude, maxPlayers);
            this.mode = mode;
        }
    }
}