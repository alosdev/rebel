<?php
//$value = json_decode($_POST['json']);
//print_r($value);
print_r($_POST);

$msgs = $_POST;

 foreach($msgs as $msg => $key)
    {
        echo $msg."<br />";
		echo $key."<br />";
    }
/*
$jsonstring = json_decode($_POST);
echo $jsonstring;

echo json_encode($_POST);
echo json_decode($_POST);
*/
/*
foreach($_POST["Level"] as $key => $value) 
{
echo $key . " => " . $value;
}
*/
?>