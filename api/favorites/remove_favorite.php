<?php
require_once '../config/db.php';

// Set header untuk JSON
header('Content-Type: application/json');

// Ambil data dari body request
$data = json_decode(file_get_contents("php://input"), true);
$user_id = $data['user_id'] ?? null;
$place_id = $data['place_id'] ?? null;

// Validasi input
if (!$user_id || !$place_id || !is_numeric($user_id) || !is_numeric($place_id)) {
    echo json_encode([
        "status" => false,
        "message" => "Data tidak lengkap atau tidak valid"
    ]);
    exit;
}

try {
    // Cek apakah data favorit ada
    $check = $pdo->prepare("SELECT id FROM favorites WHERE user_id = ? AND place_id = ?");
    $check->execute([$user_id, $place_id]);

    if (!$check->fetch()) {
        echo json_encode([
            "status" => false,
            "message" => "Data favorit tidak ditemukan"
        ]);
        exit;
    }

    // Hapus favorit
    $stmt = $pdo->prepare("DELETE FROM favorites WHERE user_id = ? AND place_id = ?");
    $stmt->execute([$user_id, $place_id]);

    echo json_encode([
        "status" => true,
        "message" => "Favorit berhasil dihapus"
    ]);
} catch (PDOException $e) {
    echo json_encode([
        "status" => false,
        "message" => "Terjadi kesalahan server",
        "error" => $e->getMessage() // boleh dihapus di production
    ]);
}
?>
