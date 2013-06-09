<?php
 
require 'Slim/Slim.php';
\Slim\Slim::registerAutoloader();
 
$app = new \Slim\Slim();
 
//$app->get('/demos', 'getBoxes');
$app->get('/demo', 'getAllDemos');
$app->get('/demo/:id',  'getDemoInfo');
$app->get('/test', 'getTest');
$app->get('/json', 'getJson');
 
$app->run();

/* 
function getBoxes() {
    $sql = "SELECT * FROM demos";
    try {
        $db = getConnection();
        $stmt = $db->query($sql);
        $wines = $stmt->fetchAll(PDO::FETCH_OBJ);
        $db = null;
        echo '{"demonstrations": ' . json_encode($wines) . '}';
    } catch(PDOException $e) {
        echo '{"error":{"text":'. $e->getMessage() .'}}';
    }
}
*/

function getAllDemos() {
include '../test.php';
/*
    $sql = "SELECT * FROM demos";
    try {
        $db = getConnection();
        $stmt = $db->query($sql);
        $wines = $stmt->fetchAll(PDO::FETCH_OBJ);
        $db = null;
        echo '{"demonstrations": ' . json_encode($wines) . '}';
    } catch(PDOException $e) {
        echo '{"error":{"text":'. $e->getMessage() .'}}';
    }*/
}

function getDemoInfo($id) {
//include '../test.php';
$sql = "SELECT * FROM demos WHERE id=:id";
    try {
        $db = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam("id", $id);
        $stmt->execute();
        $demoinfo = $stmt->fetchObject();
        $db = null;
        echo json_encode($demoinfo);
    } catch(PDOException $e) {
        echo '{"error":{"text":'. $e->getMessage() .'}}';
    }
}

function getTest() {
    $sql = "SELECT  `id` ,  `demoname` AS title ,  `hashtag` ,  `latitude` ,  `longitude` FROM  `demos` ";
    try {
        $db = getConnection();
        $stmt = $db->query($sql);
        $wines = $stmt->fetchAll(PDO::FETCH_OBJ);
        $db = null;
		//print_r($wines);
		//echo '<br/>';
        echo '{"demonstrations": ' . json_encode($wines) . '}';
    } catch(PDOException $e) {
        echo '{"error":{"text":'. $e->getMessage() .'}}';
    }
}

function getJson() {
    $sql = "SELECT  `id` ,  `demoname` AS title ,  `hashtag` ,  `latitude` ,  `longitude` FROM  `demos` ";
    
	
	try {
        $db = getConnection();
        $stmt = $db->query($sql);
		$result = $stmt;
		
				if(mysql_num_rows($result)){
				echo '{"testData":[';

				$first = true;
				$row=mysql_fetch_assoc($result);
				while($row=mysql_fetch_row($result)){
				//  cast results to specific data types

				if($first) {
					$first = false;
				} else {
					echo ',';
				}
				echo json_encode($row);
				}
				echo ']}';
				} else {
			echo '[]';
			}
		/*
        $wines = $stmt->fetchAll(PDO::FETCH_OBJ);
        $db = null;
		//print_r($wines);
		//echo '<br/>';
        echo '{"demonstrations": ' . json_encode($wines) . '}';
		*/
    } catch(PDOException $e) {
        echo '{"error":{"text":'. $e->getMessage() .'}}';
    }
}
/*
$result = mysql_query("SELECT * from listinfo") or die('Could not query');

if(mysql_num_rows($result)){
    echo '{"testData":[';

    $first = true;
    $row=mysql_fetch_assoc($result);
    while($row=mysql_fetch_row($result)){
        //  cast results to specific data types

        if($first) {
            $first = false;
        } else {
            echo ',';
        }
        echo json_encode($row);
    }
    echo ']}';
} else {
    echo '[]';
}
 */
function getWine($id) {
    $sql = "SELECT * FROM wine WHERE id=:id";
    try {
        $db = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam("id", $id);
        $stmt->execute();
        $wine = $stmt->fetchObject();
        $db = null;
        echo json_encode($wine);
    } catch(PDOException $e) {
        echo '{"error":{"text":'. $e->getMessage() .'}}';
    }
}
 
function addWine() {
    $request = Slim::getInstance()->request();
    $wine = json_decode($request->getBody());
    $sql = "INSERT INTO wine (name, grapes, country, region, year, description) VALUES (:name, :grapes, :country, :region, :year, :description)";
    try {
        $db = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam("name", $wine->name);
        $stmt->bindParam("grapes", $wine->grapes);
        $stmt->bindParam("country", $wine->country);
        $stmt->bindParam("region", $wine->region);
        $stmt->bindParam("year", $wine->year);
        $stmt->bindParam("description", $wine->description);
        $stmt->execute();
        $wine->id = $db->lastInsertId();
        $db = null;
        echo json_encode($wine);
    } catch(PDOException $e) {
        echo '{"error":{"text":'. $e->getMessage() .'}}';
    }
}
 
function updateWine($id) {
    $request = Slim::getInstance()->request();
    $body = $request->getBody();
    $wine = json_decode($body);
    $sql = "UPDATE wine SET name=:name, grapes=:grapes, country=:country, region=:region, year=:year, description=:description WHERE id=:id";
    try {
        $db = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam("name", $wine->name);
        $stmt->bindParam("grapes", $wine->grapes);
        $stmt->bindParam("country", $wine->country);
        $stmt->bindParam("region", $wine->region);
        $stmt->bindParam("year", $wine->year);
        $stmt->bindParam("description", $wine->description);
        $stmt->bindParam("id", $id);
        $stmt->execute();
        $db = null;
        echo json_encode($wine);
    } catch(PDOException $e) {
        echo '{"error":{"text":'. $e->getMessage() .'}}';
    }
}
 
function deleteWine($id) {
    $sql = "DELETE FROM wine WHERE id=:id";
    try {
        $db = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam("id", $id);
        $stmt->execute();
        $db = null;
    } catch(PDOException $e) {
        echo '{"error":{"text":'. $e->getMessage() .'}}';
    }
}
 
function findByName($query) {
    $sql = "SELECT * FROM wine WHERE UPPER(name) LIKE :query ORDER BY name";
    try {
        $db = getConnection();
        $stmt = $db->prepare($sql);
        $query = "%".$query."%";
        $stmt->bindParam("query", $query);
        $stmt->execute();
        $wines = $stmt->fetchAll(PDO::FETCH_OBJ);
        $db = null;
        echo '{"wine": ' . json_encode($wines) . '}';
    } catch(PDOException $e) {
        echo '{"error":{"text":'. $e->getMessage() .'}}';
    }
}
 
function getConnection() {
    $dbhost="localhost";
    $dbuser="d01760a4";
    $dbpass="paypal2013";
    $dbname="d01760a4";
    $dbh = new PDO("mysql:host=$dbhost;dbname=$dbname", $dbuser, $dbpass);
    $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    return $dbh;
}
 
?>