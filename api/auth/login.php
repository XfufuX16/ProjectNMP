<?php
require_once '../config/db.php';

header("Content-Type: application/json");

// Ambil data dari POST (bukan JSON)
$email = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';

// Validasi
if (empty($email) || empty($password)) {
    echo json_encode(["status" => false, "message" => "Email dan password wajib diisi"]);
    exit;
}

// Cek user
$stmt = $pdo->prepare("SELECT * FROM users WHERE email = ?");
$stmt->execute([$email]);
$user = $stmt->fetch(PDO::FETCH_ASSOC);

if ($user && password_verify($password, $user['password'])) {
    $token = bin2hex(random_bytes(32));
    $expiry = date('Y-m-d H:i:s', strtotime('+7 days'));

    $stmt = $pdo->prepare("INSERT INTO user_tokens (user_id, token, expiry) VALUES (?, ?, ?)");
    $stmt->execute([$user['id'], $token, $expiry]);

    echo json_encode([
        "status" => true,
        "message" => "Login berhasil",
        "user" => [
            "id" => $user['id'],
            "name" => $user['name'],
            "email" => $user['email'],
            "created_at" => $user['created_at']
        ],
        "token" => $token
    ]);
} else {
    echo json_encode(["status" => false, "message" => "Email atau password salah"]);
}
