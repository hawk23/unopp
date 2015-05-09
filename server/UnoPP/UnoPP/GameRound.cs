using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class GameRound
    {
        private static int nextID = 0;
        private static readonly object objLock = new object();

        public int id;
        public GameState GameState;

        public GameRound()
        {

        }

        public GameRound(GameState gameState)
        {
            lock (objLock)
            {
                this.id = nextID++;
            }

            this.GameState = gameState;
        }
    }
}


