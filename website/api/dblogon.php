<?php
$con = mysql_connect("localhost","d01760a4","paypal2013");

if (!$con)
	{
		die('Could not connect: ' . mysql_error());
	}
	
mysql_select_db("d01760a4", $con);
//mysql_query("set names 'utf8'");


?>