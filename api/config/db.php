<?php
$host = 'localhost';
$dbname = 'healingyuk_db';
$username = 'root';
$password = ''; 

try {
    $pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8mb4", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (Exception $e) {
    die(json_encode(["status" => false, "message" => "Connection failed: " . $e->getMessage()]));
}
?>
