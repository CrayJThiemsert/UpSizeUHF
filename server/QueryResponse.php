<?php
require_once("dbcontroller.php");
/* 
A domain Class to demonstrate RESTful web services
*/
Class QueryResponse {
	private $results = array();
	/*
		you should hookup the DAO here
	*/
	public function getAllCostume(){
		$query = "SELECT * FROM tblcsvimport ORDER BY actor ASC";
		$dbcontroller = new DBController();
		$this->results = $dbcontroller->executeSelectQuery($query);
		return $this->results;
	}	
	
	public function getAllActor(){
		$query = "SELECT DISTINCT actor FROM tblcsvimport ORDER BY actor ASC";
		$dbcontroller = new DBController();
		$this->results = $dbcontroller->executeSelectQuery($query);
		return $this->results;
	}
}
?>