<?php
require_once '../config/db.php';

$headers = apache_request_headers();
$token = $headers['Authorization'] ?? '';

if (!$token) {
    echo json_encode(["status" => false, "message" => "Token tidak tersedia"]);
    exit;
}

$stmt = $pdo->prepare("SELECT user_id FROM user_tokens WHERE token = ? AND expiry > NOW()");
$stmt->execute([$token]);

$user = $stmt->fetch(PDO::FETCH_ASSOC);

if ($user) {
    echo json_encode(["status" => true, "user_id" => $user['user_id']]);
} else {
    echo json_encode(["status" => false, "message" => "Token tidak valid atau kadaluarsa"]);
}
?>
