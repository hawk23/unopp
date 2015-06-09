using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class GameRound
    {
        private static int nextID = 0;
        private static readonly object objLock1 = new object();
        private static readonly object objLock2 = new object();


        public int id;
        public bool finished;
        public GameState GameState;
        public List<Update> Updates;

        public GameRound()
        {

        }

        public GameRound(GameState gameState, bool state)
        {
            lock (objLock1)
            {
                this.id = nextID++;
            }

            this.finished = state;
            this.GameState = gameState;
            this.Updates = new List<Update>();
        }

        public void SetUpdate(Update update)
        {
            lock (objLock2)
            {
                this.Updates.Add(update);
            }
        }

        public bool CheckUpdate(int updateID)
        {
            foreach (Update update in this.Updates)
            {
                if (update.updateID == updateID)
                {
                    return false;
                }
            }

            return true;
        }
    }
}


