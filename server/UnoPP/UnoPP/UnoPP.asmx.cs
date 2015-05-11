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

        static readonly object objLock = new object();
        static Random rnd = new Random();

        private static Deck InitStackOfCards(int mode)
        {
            List<Card> stackOfCards = null;
            int nrCards;

            switch(mode)
            {
                case 0: 
                    {
                        nrCards = 108;
                        stackOfCards = new List<Card>(nrCards);
                        
                        for(int i = 0; i < nrCards; i++)
                        {
                            stackOfCards.Add(new Card(i, -1));
                        }

                    } break;
                case 1: // TODO: cheat mode 1
                    {   
                        nrCards = 110;
                        stackOfCards = new List<Card>(nrCards);

                        for(int i = 0; i < nrCards; i++)
                        {
                            stackOfCards.Add(new Card(i, -1));
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
        public GameResult CreateGame(string gameName, int hostID, double latitude, double longitude, int maxPlayers)
        {
            // create a new game session
            GameSession gameSession = null;

            // check if player who is host exists
            if (Player.GetPlayerByID(hostID) != null)
            {
                // check if value of maxPlayers in valid
                if (maxPlayers <= 1 || maxPlayers >= 9)
                {
                    return new GameResult(new Result(false, "Maximale Spieleranzahl muss größer als 1 und kleiner als 9 sein!"), null);
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
            GameSession gameSession = GetGameSession(gameID);
            
            // there was no game found
            if (gameSession == null)
            {
                return GameSessionNotFoundResult(gameID);
            }

            lock(objLock)
            {
                // try to find a free seat in the current game session
                for (int i = 0; i < gameSession.maxPlayers; i++)
                {
                    // check if player is already in game session
                    if (gameSession.Players[i] != null)
                    {
                        if (gameSession.Players[i].id == playerID)
                        {
                            return new Result(false, "Spieler mit ID: " + playerID + " bereits im Spiel!");
                        }
                    }
                    // free seat found
                    if (gameSession.Players[i] == null)
                    {
                        // successfully joined game session
                        if(Player.GetPlayerByID(playerID) != null)
                        {
                            gameSession.Players[i] = Player.GetPlayerByID(playerID);
                            return new Result(true, "Erfolgreich beigetreten!");
                        }
                        // player was not found
                        else
                        {
                            return new Result(false, "Spieler mit ID: " + playerID + " nicht gefunden!");
                        }
                    }
                }

                // no free seat found
                return new Result(false, "Spiel bereits voll!");
            }
        } 
        
        [WebMethod]
        public GameResult GetGame(int gameID)
        {
            GameSession gameSession = GetGameSession(gameID);
            Result Result = null;

            // no game session with given id found
            if(gameSession == null)
            {
                Result = GameSessionNotFoundResult(gameID);
            }
                
            return new GameResult(Result, gameSession);
        }
        
        //[WebMethod]
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
        public Result StartGame(int gameID, int hostID)
        {      
            GameSession gameSession = GetGameSession(gameID);
            int nrStartCards = 7;

            // no game sessioin with given id found
            if (gameSession == null)
            {
                return GameSessionNotFoundResult(gameID);
            }
            
            // missing permission
            if (gameSession.host != hostID)
            {
                return new Result(false, "Nur Host (ID: " + gameSession.host + ") kann Spiel starten!");
            }

            // find number of active players in session
            int nrActivePlayers;

            for (nrActivePlayers = 0; nrActivePlayers < gameSession.maxPlayers; nrActivePlayers++)
            {
                if (gameSession.Players[nrActivePlayers] == null)
                {
                    break;
                }
            }

            if (nrActivePlayers <= 1)
            {
               return new Result(false, "Nicht genügend Spieler vorhanden!");
            }

            // create new game round
            GameState gameState = new GameState(hostID, nrActivePlayers, InitStackOfCards(gameSession.mode));
            GameRound gameRound = new GameRound(gameState);
            gameSession.GameRounds.Add(gameRound);
            
            // assign 7 cards to each active player
            for (int j = 0; j < nrStartCards; j++)
            {
                for (int i = 0; i < nrActivePlayers; i++)
                {
                    Card card = Deck.DrawCard(gameState.Deck);
                    card.owner = gameSession.Players[i].id;
                    gameSession.Players[i].Cards.Add(card);
                }
            }
            
            return new Result(true, "Spiel erfolgreich gestartet!");
        }

        [WebMethod]
        public Result DestroyGame(int gameID, int hostID)
        {
            for(int i = 0; i < UnoPP.gameSessions.Count; i++)
            {
                if (UnoPP.gameSessions[i].id == gameID)
                {
                    if (UnoPP.gameSessions[i].host == hostID)
                    {
                        UnoPP.gameSessions.RemoveAt(i);
                        return new Result(true, "Spiel mit ID: " + gameID + " erfolgreich entfernt!");
                    }
                    else
                    {
                        return new Result(false, "Spieler mit ID: " + hostID + " hat keine Berechtigungen um Spiel zu starten!");
                    }
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

        // [WebMethod]
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