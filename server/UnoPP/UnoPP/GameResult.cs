using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class GameResult
    {
        public Result Result;
        public GameSession GameSession;

        public GameResult()
        {

        }

        public GameResult(Result result, GameSession gameSession)
        {
            this.Result = result;
            this.GameSession = gameSession;
        }
    }
}