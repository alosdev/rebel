<?php
header("Content-Type: text/html; charset=utf-8");
$con = mysql_connect("localhost","d01760a4","paypal2013");
if (!$con)
 {
 die('Could not connect: ' . mysql_error());
}

mysql_select_db("d01760a4", $con);
mysql_query("set names 'utf8'");

//$uuid = $_POST['uuid'];
//$shortname = $_POST['shortname'];
$nickname = $_POST['nickname'];
$password = $_POST['password'];
$demoname = $_POST['demoname'];
$hashtag = $_POST['hashtag'];
$latitude = $_POST['demolat'];
$longitude = $_POST['demolng'];
$start = $_POST['start'];
$end = $_POST['end'];

$states=mysql_query("INSERT INTO `demos` (`id`, `nickname`, `password`, `demoname`, `hashtag`, `latitude`, `longitude`, `start`, `end`) VALUES ('', '$nickname', '$password', '$demoname', '$hashtag', '$latitude', '$longitude', '$start', '$end')");

if (!mysql_query($sql,$con))
{
die('Error: ' . mysql_error());
}

echo "Demo successfully added to our database.";

mysql_close($con);
?>