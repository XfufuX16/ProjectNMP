<?php
require_once '../config/db.php';

// Set response header
header('Content-Type: application/json');

// Ambil ID dari query string
$id = $_GET['id'] ?? null;

// Validasi ID
if (!$id || !is_numeric($id)) {
    echo json_encode([
        "status" => false,
        "message" => "ID tempat tidak valid atau tidak ditemukan"
    ]);
    exit;
}

try {
    // Query data tempat berdasarkan ID
    $stmt = $pdo->prepare("SELECT * FROM places WHERE id = ?");
    $stmt->execute([$id]);
    $place = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($place) {
        echo json_encode([
            "status" => true,
            "data" => $place
        ]);
    } else {
        echo json_encode([
            "status" => false,
            "message" => "Tempat tidak ditemukan"
        ]);
    }

} catch (PDOException $e) {
    echo json_encode([
        "status" => false,
        "message" => "Terjadi kesalahan saat mengambil data",
        "error" => $e->getMessage()
    ]);
}
?>
