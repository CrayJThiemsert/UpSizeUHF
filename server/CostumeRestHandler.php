<?php
require_once("SimpleRest.php");
require_once("QueryResponse.php");
		
class CostumeRestHandler extends SimpleRest {

	function getAllCostumes() {	

		$costume = new QueryResponse();
		$rawData = $costume->getAllCostume();

		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('error' => 'No costumes found!');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';//$_POST['HTTP_ACCEPT'];
		$this ->setHttpHeaders($requestContentType, $statusCode);
		
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			echo $response;
		}
	}
	
	function getAllActors() {	

		$actors = new QueryResponse();
		$rawData = $actors->getAllActor();

		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('error' => 'No costumes found!');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';//$_POST['HTTP_ACCEPT'];
		$this ->setHttpHeaders($requestContentType, $statusCode);
		
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			echo $response;
		}
	}
	
	public function encodeJson($responseData) {
		$jsonResponse = json_encode($responseData);
		return $jsonResponse;		
	}
}
?>