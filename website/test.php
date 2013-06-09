
<?php
mysql_connect("localhost","d01760a4","paypal2013");

$output = array();

$response = array();
mysql_select_db("d01760a4");
$sql=mysql_query("select * from demos");
while($row=mysql_fetch_assoc($sql)){
$route = array();

$id=$row['id']; 
$demoname=$row['demoname']; 
$title=$row['title']; 
$hashtag=$row['hashtag']; 

$lat=$row['latitude']; 
$lng=$row['longitude']; 

$route[] = array('latitude'=> $lat, 'longitude'=> $lng);

$posts[] = array('id'=> $id, 'title'=> $demoname, 'desc'=> $demoname, 'hashtag'=> $hashtag, 'route' => $route);



//$output[]=$row;
}
$response['posts'] = $posts;
$response = $posts;
print(json_encode($response));
mysql_close();
?>
