using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Net;
using System.Security.Principal;
using System.Text;

namespace UnoPP
{
    /// <summary>
    /// Zusammenfassungsbeschreibung für UnoPP
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // Wenn der Aufruf dieses Webdiensts aus einem Skript mithilfe von ASP.NET AJAX zulässig sein soll, heben Sie die Kommentarmarkierung für die folgende Zeile auf. 
    // [System.Web.Script.Services.ScriptService]
    public class UnoPP : System.Web.Services.WebService
    {
        private static List<GameSession> gameSessions = new List<GameSession>();
        private static readonly object objLock = new object();

        private static List<Card> startDeckMode0 = InitStackOfCards(0);
        private static List<Card> startDeckMode1 = InitStackOfCards(1);
        private static int nrStartCards = 7;

        private static Deck getNewDeck(int mode)
        {
            if (mode == 0)
            {
                return new Deck(startDeckMode0);
            }
            else
            {
                return new Deck(startDeckMode1);
            }
        }
        
        private static List<Card> InitStackOfCards(int mode)
        {
            List<Card> stackOfCards = null;
            int nrCards;

            switch (mode)
            {
                case 0:
                    {
                        nrCards = 108;
                        stackOfCards = new List<Card>(nrCards);

                        for (int i = 0; i < nrCards; i++)
                        {
                            stackOfCards.Add(new Card());
                        }

                    } break;
                case 1: // TODO: cheat mode 1
                    {
                        nrCards = 110;
                        stackOfCards = new List<Card>(nrCards);

                        for (int i = 0; i < nrCards; i++)
                        {
                            stackOfCards.Add(new Card());
                        }
                    } break;
                default:
                    {

                    } break;
            }

            return stackOfCards;
        }

        private void AssignCards(GameSession gameSession, int nrActivePlayers)
        {
            GameState gameState = gameSession.GameRounds.Last().GameState;

            // assign cards to each active player and sort deck
            for (int i = 0; i < nrActivePlayers; i++)
            {
                for (int j = 0; j < nrStartCards; j++)
                {
                    Card card = Deck.DrawCard(gameState.Deck);
                    gameSession.Players[i].Cards.Add(card);
                }

                gameSession.Players[i].SortCards();
            }
        }
        
        private void ResetHandCards(GameSession gameSession)
        {
            int nrActivePlayers = Player.GetActivePlayers(gameSession.Players);

            for (int i = 0; i < nrActivePlayers; i++)
            {
                gameSession.Players[i].Cards = new List<Card>();
            }
        }

        private static double DistanceTo(double lat1, double lon1, double lat2, double lon2, char unit = 'K')
        {
            double rlat1 = Math.PI * lat1 / 180;
            double rlat2 = Math.PI * lat2 / 180;
            double theta = lon1 - lon2;
            double rtheta = Math.PI * theta / 180;
            double dist =
                Math.Sin(rlat1) * Math.Sin(rlat2) + Math.Cos(rlat1) *
                Math.Cos(rlat2) * Math.Cos(rtheta);
            dist = Math.Acos(dist);
            dist = dist * 180 / Math.PI;
            dist = dist * 60 * 1.1515;

            switch (unit)
            {
                case 'K': //Kilometers -> default
                    return dist * 1.609344;
                case 'N': //Nautical Miles 
                    return dist * 0.8684;
                case 'M': //Miles
                    return dist;
            }

            return dist;
        }

        public Result CheckParameters(int gameID, int playerID, bool checkHost)
        {
            GameSession gameSession = GetGameSession(gameID);

            // no game sessioin with given id found
            // if gameID -1 ignore t
            if (gameID != -1 && gameSession == null)
            {
                return GameSessionNotFound(gameID);
            }

            Player player = Player.GetPlayerByID(playerID);

            // no player with given id found
            if (player == null)
            {
                return PlayerNotFound(playerID);
            }

            // missing permissions
            if (checkHost && gameSession.hostID != playerID)
            {
                return MissingPermissions(gameSession.hostID);
            }

            return new Result(true, "");
        }

        public GameSession GetGameSession(int gameSessionID)
        {
            foreach (GameSession session in UnoPP.gameSessions)
            {
                if (session.id == gameSessionID)
                {
                    return session;
                }
            }

            return null;
        }

        private Result GameSessionNotFound(int gameSessionID)
        {
            return new Result(false, "Kein Spiel mit ID: " + gameSessionID + " gefunden!");
        }

        private Result GameSessionNotStarted(int gameSessionID)
        {
            return new Result(false, "Spiel mit ID: " + gameSessionID + " wurde noch nicht gestartet!");
        }

        private Result PlayerNotFound(int playerID)
        {
            return new Result(false, "Spieler mit ID: " + playerID + " konnte nicht gefunden werden!");
        }

        private Result MissingPermissions(int hostID)
        {
            return new Result(false, "Nur Host (ID: " + hostID + ") hat die notwendigen Berechtigungen!");
        }
        
        private Result GameNotStarted(int gameID)
        {
            return new Result(false, "Spiel mit ID: " + gameID + " muss zuerst gestartet werden!");

        }

        /********************************************
         **             WEB METHODS                **
         ********************************************/
        [WebMethod]
        public Player CreatePlayer(string name)
        {
            Player player = new Player(name);
            return player;
        }

        [WebMethod]
        public GameResult CreateGame(string gameName, int hostID, double latitude, double longitude, int maxPlayers)
        {
            // create a new game session
            GameSession gameSession = null;

            // check if player who is host exists
            if (Player.GetPlayerByID(hostID) != null)
            {
                // check if value of maxPlayers in valid
                if (maxPlayers <= 1 || maxPlayers >= 7)
                {
                    return new GameResult(new Result(false, "Maximale Spieleranzahl muss größer als 1 und kleiner als 7 sein!"), null);
                }

                // add game session to list
                gameSession = new GameSession(gameName, hostID, latitude, longitude, maxPlayers);
                UnoPP.gameSessions.Add(gameSession);
            }
            else
            {
                return new GameResult(new Result(false, "Spiel konnte nicht erstellt werden, da Host nicht gefunden wurde!"), null);
            }

            // host joins to his own game
            Result result = JoinGame(gameSession.id, hostID);

            return new GameResult(result, gameSession);
        }

        [WebMethod]
        public Result JoinGame(int gameID, int playerID)
        {
            Result result = CheckParameters(gameID, playerID, false);

            if (!result.status)
            {
                return result;
            }

            GameSession gameSession = GetGameSession(gameID);
            int nrActivePlayers = Player.GetActivePlayers(gameSession.Players);

            lock (objLock)
            {
                // check if player is already in game session
                for (int i = 0; i < nrActivePlayers; i++)
                {
                    if (gameSession.Players[i].id == playerID)
                    {
                        return new Result(false, "Spieler mit ID: " + playerID + " bereits im Spiel!");
                    }
                }

                // try to find a free seat
                for (int i = nrActivePlayers; i < gameSession.maxPlayers; i++)
                {
                    // redundant check
                    if (gameSession.Players[i] == null)
                    {
                        Player p = null;

                        // successfully joined game session
                        if ((p = Player.GetPlayerByID(playerID)) != null)
                        {
                            // remove players cards
                            p.Cards = new List<Card>();
                            // add player to session
                            gameSession.Players[i] = p;
                            return new Result(true, "Erfolgreich beigetreten!");
                        }
                        // player was not found
                        else
                        {
                            return PlayerNotFound(playerID);
                        }
                    }
                }

                // no free seat found
                return new Result(false, "Spiel bereits voll!");
            }
        }

        [WebMethod]
        public GameSession[] ListGames(double latitude, double longitude, double maxDistance)
        {
            List<GameSession> resultList = new List<GameSession>();

            // try to find games which are in range and add to a list
            foreach (GameSession gameSession in UnoPP.gameSessions)
            {
                if (!gameSession.started)
                {
                    if (latitude == 0 && longitude == 0)
                    {
                        resultList.Add(gameSession);
                    }
                    // calculate distance
                    else if (DistanceTo(gameSession.Location.latitude, gameSession.Location.longitude, latitude, longitude, 'K') <= maxDistance)
                    {
                        resultList.Add(gameSession);
                    }
                }
            }

            return resultList.ToArray();
        }

        [WebMethod]
        public GameResult GetGame(int gameID)
        {
            GameSession gameSession = GetGameSession(gameID);
            Result Result = null;

            // no game session with given id found
            if (gameSession == null)
            {
                Result = GameSessionNotFound(gameID);
            }
            else
            {
                Result = new Result(true, "");
            }
            
            return new GameResult(Result, gameSession);
        }

        [WebMethod]
        public Result StartGame(int gameID, int hostID)
        {
            Result result = CheckParameters(gameID, hostID, true);

            if (!result.status)
            {
                return result;
            }

            return StartGameRound(gameID, hostID);
        }

        [WebMethod]
        public Result StartGameRound(int gameID, int hostID)
        {
            Result result = CheckParameters(gameID, hostID, true);

            if (!result.status)
            {
                return result;
            }

            GameSession gameSession = GetGameSession(gameID);

            // check if last game round is finished
            if (gameSession.started && !gameSession.GameRounds.Last().finished)
            {
                return new Result(false, "Spielrunde läuft noch!");
            }

            // find number of active players in session
            int nrActivePlayers = Player.GetActivePlayers(gameSession.Players);

            if (nrActivePlayers <= 1)
            {
                return new Result(false, "Nicht genügend Spieler vorhanden!");
            }

            Deck deck = null;

            if(gameSession.mode == 0)
            {
                deck = getNewDeck(0);
            }
            else
            {
                deck = getNewDeck(1);
            }

            // create new game round
            GameState gameState = new GameState(hostID, nrActivePlayers, gameSession.Players, deck);
            GameRound gameRound = new GameRound(gameState, false);
            gameSession.GameRounds.Add(gameRound);

            AssignCards(gameSession, nrActivePlayers);

            // first game round
            if (!gameSession.started)
            {
                gameSession.started = true;
                return new Result(true, "Spiel erfolgreich gestartet!");
            }
            else
            {
                return new Result(true, "Neue Spielrunde gestartet!");
            }
        }

        [WebMethod]
        public Result LeaveGame(int gameID, int playerID)
        {
            Result result = CheckParameters(gameID, playerID, false);

            if (!result.status)
            {
                return result;
            }

            GameSession gameSession = GetGameSession(gameID);

            // host leaves game -> DestroyGame
            if (gameSession.hostID == playerID)
            {
                // leave every player from game and delete game
                result = DestroyGame(gameID, playerID);

                if (!result.status)
                {
                    return result;
                }
                else
                {
                    return new Result(true, "Host mit ID: " + playerID + " hat das Spiel verlassen!");
                }
            }

            Player player = Player.GetPlayerByID(playerID);
            int nrActivePlayers = Player.GetActivePlayers(gameSession.Players);
            bool removed = false;

            // try to find player in game session
            for (int i = 0; i < nrActivePlayers; i++)
            {
                // move other players index position
                if (removed)
                {
                    gameSession.Players[i - 1] = gameSession.Players[i];
                }
                else if (gameSession.Players[i].id == playerID)
                {
                    gameSession.Players[i] = null;
                    removed = true;
                }
            }

            if (removed)
            {
                return new Result(true, "Spieler mit ID: " + playerID + " hat das Spiel verlassen!");
            }
            else
            {
                return new Result(false, "Spieler mit ID: " + playerID + " wurde im Spiel nicht gefunden!");
            }
        }

        [WebMethod]
        public Result DestroyPlayer(int gameID, int playerID)
        {
            Result result = CheckParameters(gameID, playerID, false);

            if (!result.status)
            {
                return result;
            }

            GameSession gameSession = GetGameSession(gameID);

            // host of game is deleted -> DestroyGame
            if (gameID != -1 && gameSession.hostID == playerID)
            {
                // leave every player from game and delete game
                result = DestroyGame(gameID, playerID);

                if (!result.status)
                {
                    return result;
                }
                else
                {
                    if (Player.RemovePlayer(playerID))
                    {
                        return new Result(true, "Spieler mit ID: " + playerID + " wurde erfolgreich entfernt!");
                    }

                    return new Result(false, "Spieler mit ID: " + playerID + " konnte nicht entfernt werden!");
                }
            }
            else
            {
                if (Player.RemovePlayer(playerID))
                {
                    // player is in game
                    if (result.status)
                    {
                        result = LeaveGame(gameID, playerID);

                        if (!result.status)
                        {
                            return result;
                        }
                    }

                    return new Result(true, "Spieler mit ID: " + playerID + " wurde erfolgreich entfernt!");
                }

                return new Result(false, "Spieler mit ID: " + playerID + " konnte nicht entfernt werden!");
            }
        }

        [WebMethod]
        public Result EndGameRound(int gameID, int hostID)
        {
            Result result = CheckParameters(gameID, hostID, true);

            if (!result.status)
            {
                return result;
            }

            GameSession gameSession = GetGameSession(gameID);

            if (gameSession.started)
            {
                gameSession.GameRounds.Last().finished = true;
                ResetHandCards(gameSession);
                return new Result(true, "Spielrunde mit ID: " + gameSession.GameRounds.Last().id + " erfolgreich beendet");
            }
            else
            {
                return GameSessionNotStarted(gameSession.id);
            }
        }

        [WebMethod]
        public GameUpdate GetUpdate(int gameID, int lastKnownUpdateID)
        {
            GameSession gameSession = GetGameSession(gameID);
            Result result = null;
            List<Update> newUpdates = null;

            if (gameSession == null)
            {
                result = GameSessionNotFound(gameID);
            }
            else
            {
                if (!gameSession.started)
                {
                    result = GameNotStarted(gameID);
                }
                else
                {
                    newUpdates = new List<Update>();

                    foreach (Update update in gameSession.GameRounds.Last().Updates)
                    {
                        if (update.updateID > lastKnownUpdateID)
                        {
                            newUpdates.Add(update);
                        }
                    }

                    // keine Updates vorhanden
                    if (newUpdates.Count == 0)
                    {
                        result = new Result(true, "0");
                    }
                    else
                    {
                        result = new Result(true, "1");
                    }
                }
            }

            return new GameUpdate(result, newUpdates);
        }

        [WebMethod]
        public Result SetUpdate(int gameID, int updateID, string update)
        {
            GameSession gameSession = GetGameSession(gameID);

            if (gameSession == null)
            {
                return GameSessionNotFound(gameID);
            }
            else
            {
                if (!gameSession.started)
                {
                    return GameNotStarted(gameID);
                }
                else
                {
                    // is updateID valid
                    if (gameSession.GameRounds.Last().CheckUpdate(updateID))
                    {
                        gameSession.GameRounds.Last().SetUpdate(new Update(updateID, update));
                        return new Result(true, "Update mit ID: " + updateID + " war erfolgreich!");
                    }
                    else
                    {
                        return new Result(false, "Update mit ID: " + updateID + " bereits vorhanden!");
                    }
                }
            }
        }

        [WebMethod]
        public Result DestroyGame(int gameID, int hostID)
        {
            Result result = CheckParameters(gameID, hostID, true);

            if (!result.status)
            {
                return result;
            }

            lock (objLock)
            {
                for (int i = 0; i < UnoPP.gameSessions.Count; i++)
                {
                    if (UnoPP.gameSessions[i].id == gameID)
                    {
                        // only host can destroy game
                        if (UnoPP.gameSessions[i].hostID == hostID)
                        {
                            UnoPP.gameSessions.RemoveAt(i);
                            return new Result(true, "Spiel mit ID: " + gameID + " erfolgreich entfernt!");
                        }
                    }
                }
            }

            return new Result(false, "Spiel mit ID: " + gameID + " nicht gefunden!");
        }
    }
}