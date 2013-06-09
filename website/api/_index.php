<?php
include 'dblogon.php';

require 'Slim/Slim.php';
\Slim\Slim::registerAutoloader();



$app = new \Slim\Slim();

$app->get('/hello/:name', function ($name) {
    echo "Hello, $name";
});

$app->get('/demos/', function {
	$sql = "SELECT * FROM `demos`"; 
	$snacks = mysql_query($sql,$con);
		while($state=mysql_fetch_array($snacks)) {

			//echo "<script type='text/javascript'>";
			echo "ID   ".$state['id'].", title".$state['title'].", hashtag".$state['hashtag']."  YO!";
			//echo "</script>";
		}
});



$app->run();
?>