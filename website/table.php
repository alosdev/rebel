
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>rebek</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Create a demonstration - share and keep informed about it.">
    <meta name="author" content="">
	<script src="js/jquery.js"></script>
	<script src="js/jquery.dataTables.js"></script>
	<script src="js/bhb2013-2.js"></script>
	
	<script type="text/javascript" charset="utf-8">
			$(document).ready(function() {
				$('#example').dataTable();
			} );
		</script>
	
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>	
	
    <!-- Le styles -->
    <link href="css/bootstrap.css" rel="stylesheet">
	<link href="css/bootstrap-responsive.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 20px;
        padding-bottom: 40px;
      }

      /* Custom container */
      .container-narrow {
        margin: 0 auto;
        max-width: 700px;
      }
      .container-narrow > hr {
        margin: 30px 0;
      }

      /* Main marketing message and sign up button */
      .jumbotron {
        margin: 60px 0;
        text-align: center;
      }
      .jumbotron h1 {
        font-size: 72px;
        line-height: 1;
      }
      .jumbotron .btn {
        font-size: 21px;
        padding: 14px 24px;
      }

      /* Supporting marketing content */
      .marketing {
        margin: 60px 0;
      }
      .marketing p + h4 {
        margin-top: 28px;
      }
	  
	  #map {
		width: 100%;
		height: 400px;}
    </style>
    
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="img/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="img/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="img/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="img/apple-touch-icon-57-precomposed.png">
    <link rel="shortcut icon" href="img/favicon.png">
	</head>

  <body>

    <div class="container-narrow">
	
        <h4 class="muted">Rebel | Register, share and follow ongoing demos!</h4>
		
 
      <hr>

      <div class="jumbotron">
	  
		<a class="btn btn-large btn-success" href="index.html">Create Demo</a>  
		<a class="btn btn-large btn-success" href="table.php">Show Demos</a>  
		<!--<a class="btn btn-large btn-success" href="index.html">Donate</a>-->
		
		<br/><br/>
		
		<table cellpadding="0" cellspacing="0" border="0" class="display" id="example" width="100%">
		<thead>
		<tr>
			<th>Demonstration</th>
			<th>Hashtag</th>
			<th>Balance</th>
		</tr>
		</thead>
		<tbody>
		
		<?php
//header("Content-Type: text/html; charset=utf-8");
$con = mysql_connect("localhost","d01760a4","paypal2013");
if (!$con)
 {
 die('Could not connect: ' . mysql_error());
}

mysql_select_db("d01760a4", $con);
//mysql_query("set names 'utf8'");

$states=mysql_query("SELECT id, demoname, hashtag, balance FROM  `demos` ");

while($state=mysql_fetch_array($states)) {
echo "<tr class='odd gradeX'>";
$url = 'http://rebel.polizei-news.com/api/demo/';
$url = $url . $state['id'];
echo "<td>".$state['demoname']."</td><td>".$state['hashtag']."</td><td>".$state['balance']."</td><td><a href=".$url.">Details</a></td>";
echo "</tr>";
}
?> 
</tbody>
<tfoot>
		<tr>
			<th>Demonstration</th>
			<th>Hashtag</th>
			<th>Balance</th>
		</tr>
	</tfoot>
</table>

	  </div>

      <hr>
<!--
      <div class="row-fluid marketing">
        <div class="span6">
          <h4>Subheading</h4>
          <p>Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum.</p>

          <h4>Subheading</h4>
          <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Cras mattis consectetur purus sit amet fermentum.</p>

          <h4>Subheading</h4>
          <p>Maecenas sed diam eget risus varius blandit sit amet non magna.</p>
        </div>

        <div class="span6">
          <h4>Subheading</h4>
          <p>Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum.</p>

          <h4>Subheading</h4>
          <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Cras mattis consectetur purus sit amet fermentum.</p>

          <h4>Subheading</h4>
          <p>Maecenas sed diam eget risus varius blandit sit amet non magna.</p>
        </div>
      </div>

      <hr>
-->
      <div class="footer">
        <p>REBEL - We support your demonstration - Worldwide</p>
      </div>

    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    
    <script src="js/bootstrap-transition.js"></script>
    <script src="js/bootstrap-alert.js"></script>
    <script src="js/bootstrap-modal.js"></script>
    <script src="js/bootstrap-dropdown.js"></script>
    <script src="js/bootstrap-scrollspy.js"></script>
    <script src="js/bootstrap-tab.js"></script>
    <script src="js/bootstrap-tooltip.js"></script>
    <script src="js/bootstrap-popover.js"></script>
    <script src="js/bootstrap-button.js"></script>
    <script src="js/bootstrap-collapse.js"></script>
    <script src="js/bootstrap-carousel.js"></script>
    <script src="js/bootstrap-typeahead.js"></script>
	
  </body>
</html>
