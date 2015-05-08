using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System;
using System.Collections.Generic;
using System.Linq;
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

        private static int mode = 0;
        private static int nrCards = 108;

        static readonly object objLock = new object();
        static Random rnd = new Random();

        private static Deck InitStackOfCards(int mode)
        {
            Stack<Card> stackOfCards = null;
            int nrCards;

            switch(mode)
            {
                case 0: 
                    {
                        nrCards = 108;
                        stackOfCards = new Stack<Card>(nrCards);
                        
                        for(int i = 0; i < nrCards; i++)
                        {
                            stackOfCards.Push(new Card(i, -1));
                        }

                    } break;
                case 1: // TODO: cheat mode 1
                    {   
                        nrCards = 110;
                        stackOfCards = new Stack<Card>(nrCards);

                        for(int i = 0; i < nrCards; i++)
                        {
                            stackOfCards.Push(new Card(i, -1));
                        }
                    } break;
                default:
                    {

                    } break;
            }

            return new Deck(stackOfCards);
        }

        static private double DistanceTo(double lat1, double lon1, double lat2, double lon2, char unit = 'K')
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

        private string[] RandomizeCards(string[] arr)
        {
            List<KeyValuePair<int, string>> list = new List<KeyValuePair<int, string>>();
            // Add all strings from array
            // Add new random int each time
            foreach (string s in arr)
            {
                list.Add(new KeyValuePair<int, string>(rnd.Next(), s));
            }
            
            // umsortieren
            var sorted = from item in list
                         orderby item.Key
                         select item;

            // Neues Array erstellen
            string[] result = new string[arr.Length];

            // Werte in das Array kopieren
            int index = 0;
            foreach (KeyValuePair<int, string> pair in sorted)
            {
                result[index] = pair.Value;
                index++;
            }

            // Array zurückgeben
            return result;
        }
        
        private Result GameSessionNotFoundResult(int gameSessionID)
        {
            return new Result(false, "Kein Spiel mit ID: "+ gameSessionID + " gefunden!");
        }

        [WebMethod]
        public GameSession[] ListGames(double latitude, double longitude, double maxDistance)
        {
            if (latitude == 0 && longitude == 0)
                return UnoPP.gameSessions.ToArray();

            List<GameSession> resultList = new List<GameSession>();
            
            // try to find games which are in range and add to a list
            foreach (GameSession gameSession in UnoPP.gameSessions)
            {
                // calculate distance
                if (DistanceTo(gameSession.Location.latitude, gameSession.Location.longitude, latitude, longitude, 'K') <= maxDistance)
                    resultList.Add(gameSession);
            }

            return resultList.ToArray();
        }

        [WebMethod]
        public Player CreatePlayer(string name)
        {
            Player player = new Player(name);
            return player;
        }

        [WebMethod]
        public GameSession CreateGame(string gameName, int playerID, double latitude, double longitude, int maxPlayers)
        {
            // create a new game session
            GameSession gameSession = new GameSession(gameName, playerID, latitude, longitude, maxPlayers);
            
            // add game session to list
            UnoPP.gameSessions.Add(gameSession);

            // host joins to his own game
            JoinGame(gameSession.id, playerID);

            return gameSession;
        }

        [WebMethod]
        public Result JoinGame(int gameSessionID, int playerID)
        {
            GameSession gameSession = GetGameSession(gameSessionID);
            
            // there was no game found
            if (gameSession == null)
            {
                return GameSessionNotFoundResult(gameSessionID);
            }

            lock(objLock)
            {
                // try to find a free seat in the current game session
                for (int i = 0; i < gameSession.maxPlayers; i++)
                {
                    // free seat found
                    if (gameSession.Players[i] == null)
                    {
                        // successfully joined game session
                        if((gameSession.Players[i] = Player.getPlayerByID(playerID)) != null)
                        {
                            return new Result(true, "Erfolgreich beigetreten!");
                        }
                        // player was not found
                        else
                        {
                            return new Result(false, "Spieler mit ID: " + playerID + "nicht gefunden!");
                        }
                    }
                }

                // no free seat found
                return new Result(false, "Spiel bereits voll!");
            }
        } 
        
        [WebMethod]
        public GetGameResponse GetGame(int gameSessionID)
        {
            GameSession gameSession = GetGameSession(gameSessionID);
            Result result = null;

            // no game session with given id found
            if(gameSession == null)
            {
                result = GameSessionNotFoundResult(gameSessionID);
            }
                
            return new GetGameResponse(result, gameSession);
        }
        
        [WebMethod]
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
           
        
        [WebMethod]
        public Result StartGame(int gameID, int playerID)
        {      
            GameSession gameSession = GetGameSession(gameID);
            int nrStartCards = 7;

            // no game sessioin with given id found
            if (gameSession == null)
            {
                return GameSessionNotFoundResult(gameID);
            }
            
            // missing permission
            if (gameSession.host != playerID)
            {
                return new Result(false, "Spieler mit ID: " + playerID + " hat keine Berechtigungen um Spiel zu starten!");
            }

            // find number of players in session
            int nrPlayers;

            for (nrPlayers = 0; nrPlayers < gameSession.maxPlayers; nrPlayers++)
            {
                if (gameSession.Players[nrPlayers] == null)
                {
                    break;
                }
            }

            // create new game round
            GameState gameState = new GameState(playerID, nrPlayers, InitStackOfCards(gameSession.mode));
            GameRound gameRound = new GameRound(gameState);
            gameSession.GameRounds.Add(gameRound);
            
            // assign 7 cards to each active player
            for(int j = 0; j < nrStartCards; j++)
            {
                for(int i = 0; i < nrPlayers; i++)
                {
                    gameSession.Players[i].Cards.Add(Deck.DrawCard(gameState.Deck));
                }
            }
            
            return new Result(true, "Spiel erfolgreich gestartet");
        }

        [WebMethod]
        public Result DestroyGame(int gameID)
        {
            for(int i = 0; i < UnoPP.gameSessions.Count; i++)
            {
                if (UnoPP.gameSessions[i].id == gameID)
                {
                    UnoPP.gameSessions.RemoveAt(i);
                    return new Result(true, "Spiel mit ID: " + gameID + " erfolgreich entfernt!");
                }
            }

            return new Result(false, "Spiel mit ID: " + gameID + " nicht gefunden!");
        }

        /*
        [WebMethod]
        public int PlayCard(int gameID, int playerID, Card card, bool uno)
        {
            Game game = GetGame(gameId);
            string searchCard = card + ":P" + playerId;

            // Zuerst alle Zähler auf dem öffentlichen Stapel erhöhen
            for (int k = 0; k < game.cards.Length; k++)
                if (game.cards[k].Contains(":S"))
                {
                    game.cards[k] = game.cards[k].Split(':')[0] + ":S" +
                        Convert.ToInt32(game.cards[k].Split(':')[1].Substring(1)) + 1;
                }

            // Nun die gelegte Karte auf S0 setzen
            for (int k = 0; k < game.cards.Length; k++)
                if (game.cards[k] == searchCard)
                {
                    game.cards[k] = card + ":S0";
                }

            game.uno[playerId] = uno;

            return game.version++;
        }

        
        [WebMethod]
        public int SetActivePlayer(int gameId, int playerId)
        {
            lock (objLock)
            {
                Game game = GetGame(gameId);
                game.activePlayer = playerId;
                return game.version++;
            }
        }
        */

        [WebMethod]
        public Card[] GetCardFromDeck(int gameID, int playerID, int nrCards)
        {
            // TODO: what if gameID or playerID is invalid
            GameSession gameSession = GetGameSession(gameID);
            GameRound gameRound = gameSession.GameRounds.Last();
            GameState gameState = gameRound.GameState;
            Deck deck = gameState.Deck;
            Player player = null;

            for (int i = 0; i < gameSession.Players.Length; i++)
            {
                if((player = gameSession.Players[i]).id == playerID)
                {
                    break;
                }
            }

            Card[] cards = new Card[nrCards];

            for (int i = 0; i < nrCards; i++)
            {
                cards[i] = Deck.DrawCard(deck);
                player.Cards.Add(cards[i]);
            }

            return cards;
        }
    }
}