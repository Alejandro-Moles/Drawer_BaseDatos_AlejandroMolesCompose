<?php

$server = "localhost";
$user = "root";
$pass = "clave";
$bd = "BDAlejandroMoles";

//Creamos la conexión
$conexion = mysqli_connect($server, $user, $pass,$bd)
or die("Ha sucedido un error inexperado en la conexion de la base de datos");

//generamos la consulta
$sql = "SELECT * FROM ContactosFavoritos";
mysqli_set_charset($conexion, "utf8"); //formato de datos utf8

if(!$result = mysqli_query($conexion, $sql)) die();

$Contactos = array(); //creamos un array

while($row = mysqli_fetch_array($result))
{
    $Nombre=$row['Nombre'];
    $Tlf=$row['Tlf'];
    
    $Contactos[] = array('Nombre'=> $Nombre, 'Tlf'=> $Tlf);

}

//desconectamos la base de datos
$close = mysqli_close($conexion)
or die("Ha sucedido un error inexperado en la desconexion de la base de datos");


//Creamos el JSON
$json_string = json_encode($Contactos);

echo $json_string;

