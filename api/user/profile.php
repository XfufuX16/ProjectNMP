<?php
require_once '../config/db.php';

// Pakai $_POST karena Android (Volley) kirim form-urlencoded
$user_id = $_POST['user_id'] ?? null;

if (!$user_id) {
    echo json_encode(["status" => false, "message" => "User ID tidak ditemukan"]);
    exit;
}

// Ambil nama, email, dan tanggal dibuat
$stmt = $pdo->prepare("SELECT name, email, created_at FROM users WHERE id = ?");
$stmt->execute([$user_id]);
$user = $stmt->fetch(PDO::FETCH_ASSOC);

if ($user) {
    // Hitung total favorit user
    $stmt2 = $pdo->prepare("SELECT COUNT(*) as total FROM favorites WHERE user_id = ?");
    $stmt2->execute([$user_id]);
    $count = $stmt2->fetch(PDO::FETCH_ASSOC);

    $user["total_favorites"] = $count["total"] ?? 0;

    echo json_encode([
        "status" => true,
        "data" => $user
    ]);
} else {
    echo json_encode(["status" => false, "message" => "User tidak ditemukan"]);
}
?>
