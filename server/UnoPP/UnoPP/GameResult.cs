using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UnoPP
{
    public class GetGameResponse
    {
        public Result result;
        public GameSession gameSession;

        public GetGameResponse()
        {

        }

        public GetGameResponse(Result result, GameSession gameSession)
        {
            this.result = result;
            this.gameSession = gameSession;
        }
    }
}