<?php
require_once("CostumeRestHandler.php");
		
$view = "";
if(isset($_GET["view"]))
	$view = $_GET["view"];
/*
controls the RESTful services
URL mapping
*/
switch($view){

	case "all":
		// to handle REST Url /costume/list/
		$costumeRestHandler = new CostumeRestHandler();
		$costumeRestHandler->getAllCostumes();
		break;
		
	case "actors":
		// to handle REST Url /costume/actors/
		$costumeRestHandler = new CostumeRestHandler();
		$costumeRestHandler->getAllActors();
		break;

	case "" :
		//404 - not found;
		break;
}
?>
