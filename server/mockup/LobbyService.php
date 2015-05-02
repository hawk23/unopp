<?php
error_reporting( E_ALL );
ini_set('display_errors',1);

$commands = $_GET['cmd'];
$response = array();

switch ($commands) {
	case "createGameSession":
		$gameSession = new stdClass();
		$gameSession->status = 1;
		$gameSession->message = "Game Session erfolgreich erstellt";
		$gameSession->response = new stdClass();
		$gameSession->response->player = new stdClass();
		$gameSession->response->player->id = 3;
		$gameSession->response->player->name = "Mario";
		
		$response = array($gameSession);
		break;

	case "updateLocation":
		$location = new stdClass();
		$location->status = 1;
		$location->message = "updated location successfully";
		$location->response = new stdClass();
		
		$response = array($location);
		break;
		
	case "isGameSessionValid":
		$validGameSession1 = new stdClass();
		$validGameSession1->status = 1;
		$validGameSession1->message = "Spiel gültig";
		$validGameSession1->response = new stdClass();
		
		$response = array($validGameSession1);
		break;
		
	case "createPlayer":
		$player = new stdClass();
		$player->status = 1;
		$player->message = "Spieler erfolgreich erstellt";
		$player->response = new stdClass();
		$player->response->player = new stdClass();
		$player->response->player->id = 3;
		$player->response->player->name = "Mario";
		
		$response = array($player);
		break;
		
	case "getAvailableGameSessions":
		$availableGameSession1 = new stdClass();
		$availableGameSession1->id = 1;
		$availableGameSession1->name = "Albertos Game";
		$availableGameSession1->started = false;
		$availableGameSession1->currentPlayers = 2;
		$availableGameSession1->host = new stdClass();
		$availableGameSession1->host->id = 1;
		$availableGameSession1->host->name = "Albert";
		
		$player1 = new stdClass();
		$player1->id = 1;
		$player1->name = "Albert";
		
		$player2 = new stdClass();
		$player2->id = 2;
		$player2->name = "Roland";
		$availableGameSession1->players = array($player1,$player2);	
		
		$availableGameSession2 = new stdClass();
		$availableGameSession2->id = 2;
		$availableGameSession2->name = "Rolands Game";
		$availableGameSession2->currentPlayers = 1;
		$availableGameSession2->host = new stdClass();
		$availableGameSession2->host->id = 1;
		$availableGameSession2->host->name = "Roland";
		
		$player3 = new stdClass();
		$player3->id = 2;
		$player3->name= "Roland";
		$availableGameSession2->players = array($player3);
		
		$response = array($availableGameSession1, $availableGameSession2);
		break;
		
	case "joinGameSessions":
		$joinGameSession1 = new stdClass();
		$joinGameSession1->status = 1;
		$joinGameSession1->message = "Erfolgreich beigetreten";
		$joinGameSession1->response = new stdClass();
		$joinGameSession1->response->gameSession = new stdClass();
		$joinGameSession1->response->gameSession->id = 1;
		$joinGameSession1->response->gameSession->name = "Albertos Game";
		$joinGameSession1->response->gameSession->started = false;
		$joinGameSession1->response->gameSession->currentPlayers = 2;
		$joinGameSession1->response->gameSession->host = new stdClass();
		$joinGameSession1->response->gameSession->host->id= 1;
		$joinGameSession1->response->gameSession->host->name = "Albert";
		$player1 = new stdClass();
		$player1->id = 1;
		$player1->name = "Albert";
		$player2 = new stdClass();
		$player2->id = 2;
		$player2->name = "Roland";
		$player3 = new stdClass();
		$player3->id = 3;
		$player3->name = "Mario";
		$joinGameSession1->players = array($player1, $player2, $player3);
		
		$joinGameSession2 = new stdClass();
		$joinGameSession2->status = 0;
		$joinGameSession2->message = "Spiel voll";
		$joinGameSession2->response = new stdClass();
		
		$response = array($joinGameSession1, $joinGameSession2);
		break;
		
	case "leaveGameSession":
		$leaveGameSession = new stdClass();
		$leaveGameSession->status = 1;
		$leaveGameSession->message = "Erfolgreich verlassen";
		$leaveGameSession->response = new stdClass();
		
		$response = array($leaveGameSession);
		break;
}

// header('Content-Type: application/json');‏  
echo json_encode ($response);
?>