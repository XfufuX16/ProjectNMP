<?php
require_once '../config/db.php';
header('Content-Type: application/json');

// Ambil data dari POST (form-urlencoded)
$name              = $_POST['name'] ?? '';
$description       = $_POST['description'] ?? '';
$short_description = $_POST['short_description'] ?? '';
$category          = $_POST['category'] ?? '';
$image_url         = $_POST['image_url'] ?? '';
$latitude          = $_POST['latitude'] ?? null;
$longitude         = $_POST['longitude'] ?? null;
$user_id           = $_POST['user_id'] ?? null;

// Validasi field wajib
if (
    !$name || !$description || !$short_description ||
    !$category || !$image_url || !$user_id
) {
    echo json_encode([
        "status" => false,
        "message" => "Semua field wajib diisi"
    ]);
    exit;
}

// Validasi user_id numerik
if (!is_numeric($user_id)) {
    echo json_encode([
        "status" => false,
        "message" => "User ID tidak valid"
    ]);
    exit;
}

try {
    // Simpan ke database
    $stmt = $pdo->prepare("
        INSERT INTO places 
        (name, description, short_description, category, image_url, latitude, longitude, user_id)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    ");
    $success = $stmt->execute([
        $name, $description, $short_description, $category,
        $image_url,
        $latitude ?: null,
        $longitude ?: null,
        $user_id
    ]);

    if ($success) {
        echo json_encode([
            "status" => true,
            "message" => "Tempat berhasil ditambahkan"
        ]);
    } else {
        echo json_encode([
            "status" => false,
            "message" => "Gagal menambahkan tempat"
        ]);
    }

} catch (PDOException $e) {
    echo json_encode([
        "status" => false,
        "message" => "Terjadi kesalahan server",
        "error"   => $e->getMessage()
    ]);
}
?>
