<?php
require_once '../config/db.php';
header('Content-Type: application/json');

// Ambil data dari form POST (karena Volley pakai x-www-form-urlencoded)
$user_id = $_POST['user_id'] ?? null;
$place_id = $_POST['place_id'] ?? null;

// Validasi input
if (!$user_id || !$place_id || !is_numeric($user_id) || !is_numeric($place_id)) {
    echo json_encode([
        "status" => false,
        "message" => "Data tidak lengkap atau tidak valid"
    ]);
    exit;
}

try {
    // Cek apakah favorit sudah ada
    $check = $pdo->prepare("SELECT id FROM favorites WHERE user_id = ? AND place_id = ?");
    $check->execute([$user_id, $place_id]);

    if ($check->fetch()) {
        echo json_encode([
            "status" => false,
            "message" => "Tempat ini sudah ada di favorit"
        ]);
        exit;
    }

    // Tambahkan data ke tabel favorites
    $stmt = $pdo->prepare("INSERT INTO favorites (user_id, place_id) VALUES (?, ?)");
    $success = $stmt->execute([$user_id, $place_id]);

    if ($success) {
        echo json_encode([
            "status" => true,
            "message" => "Favorit berhasil ditambahkan"
        ]);
    } else {
        echo json_encode([
            "status" => false,
            "message" => "Gagal menambahkan favorit"
        ]);
    }

} catch (PDOException $e) {
    echo json_encode([
        "status" => false,
        "message" => "Kesalahan server",
        "error" => $e->getMessage()
    ]);
}
