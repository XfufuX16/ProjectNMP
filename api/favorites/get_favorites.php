<?php
require_once '../config/db.php';

// Set header untuk response JSON
header('Content-Type: application/json');

// Ambil user_id dari GET
$user_id = $_GET['user_id'] ?? null;

// Validasi input
if (!$user_id || !is_numeric($user_id)) {
    echo json_encode([
        "status" => false,
        "message" => "User ID tidak valid"
    ]);
    exit;
}

try {
    // Query: Ambil semua place yang sudah difavoritkan oleh user
    $stmt = $pdo->prepare("
        SELECT p.id, p.name, p.short_description, p.category, p.image_url, p.description, p.user_id, p.created_at
        FROM favorites f
        INNER JOIN places p ON f.place_id = p.id
        WHERE f.user_id = ?
        ORDER BY f.id DESC
    ");
    $stmt->execute([$user_id]);
    $favorites = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode([
        "status" => true,
        "data" => $favorites
    ]);
} catch (Exception $e) {
    echo json_encode([
        "status" => false,
        "message" => "Terjadi kesalahan pada server",
        "error" => $e->getMessage()
    ]);
}
?>
