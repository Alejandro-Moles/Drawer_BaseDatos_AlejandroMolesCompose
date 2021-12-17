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
 
 
$Nombre = $_GET["Nombre"];
$Tlf = $_GET["Tlf"];
 
$sql = "INSERT INTO ContactosFavoritos (Nombre, Tlf) VALUES ('$Nombre', '$Tlf')";
if (mysqli_query($conn, $sql)) {
      echo "New record created successfully";
} else {
      echo "Error: " . $sql . "<br>" . mysqli_error($conn);
}
mysqli_close($conn);
?>