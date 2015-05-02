<?php

$commands = $_GET['cmd'];
$response = new stdClass();

switch ($commands) {
	case createGameSession:
		$response->status = 1;
		$response->message = "Spieler erfolgreich erstellt";
		$response->response = new stdClass();
		$response->response->player = new stdClass();
		$response->response->player->id = 3;
		$response->response->player->name = "Mario";
		break;
}
header('Content-Type: application/json');‏  
echo json_encode($response);

?>