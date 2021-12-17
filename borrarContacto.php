<?php
$servername = "localhost";
$database = "BDAlejandroMoles";
$username = "root";
$password = "clave";
// Create connection
$conn = mysqli_connect($servername, $username, $password, $database);
// Check connection
if (!$conn) {
      die("Connection failed: " . mysqli_connect_error());
}
 
echo "Connected successfully";
 
$Tlf = $_GET["Tlf"];
 
$sql = "DELETE FROM ContactosFavoritos WHERE  Tlf = '$Tlf'";
if (mysqli_query($conn, $sql)) {
      echo "delete was successfully";
} else {
      echo "Error: " . $sql . "<br>" . mysqli_error($conn);
}
mysqli_close($conn);
?>