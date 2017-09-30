<!DOCTYPE html>
<html>
<head>
	<title>Dank Project</title>
</head>

<body>
<form name="form" action ="" method ="post">
	Tweet <input type="text" name="tweet" id="tweet">
	<button>send</button>
	</form>
</body>
</html>

<?php
$inputText="";
if (isset($_POST['tweet'])) {
    $inputText = $_POST['tweet'];
}

$myfile = fopen("C:/xampp/cgi-bin/newfile.txt", "w") or die ("Unable to open file!");

fwrite($myfile,$inputText);
fclose($myfile);
echo "still working....";
$python = shell_exec('python C:\xampp\cgi-bin\Twitter_neo.py');
echo "done";
?>