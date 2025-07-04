<?php
require_once '../config/db.php';
header('Content-Type: application/json');

try {
    $stmt = $pdo->query("SELECT id, name, short_description, description, category, image_url, user_id, created_at FROM places ORDER BY created_at DESC");
    $places = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode([
        "status" => true,
        "data" => $places
    ]);
} catch (PDOException $e) {
    echo json_encode([
        "status" => false,
        "message" => "Gagal mengambil data tempat",
        "error" => $e->getMessage()
    ]);
}
?>
