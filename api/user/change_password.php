<?php
require_once '../config/db.php';

// Set response header
header('Content-Type: application/json');

// Ambil data dari POST (karena dikirim via application/x-www-form-urlencoded)
$user_id = $_POST['user_id'] ?? null;
$old_password = $_POST['old_password'] ?? '';
$new_password = $_POST['new_password'] ?? '';
$repeat_password = $_POST['repeat_password'] ?? '';

// Validasi input
if (!$user_id || !$old_password || !$new_password || !$repeat_password) {
    echo json_encode([
        "status" => false,
        "message" => "Semua field harus diisi"
    ]);
    exit;
}

// Validasi kecocokan password baru
if ($new_password !== $repeat_password) {
    echo json_encode([
        "status" => false,
        "message" => "Password baru dan konfirmasi tidak cocok"
    ]);
    exit;
}

// Pastikan user_id numerik
if (!is_numeric($user_id)) {
    echo json_encode([
        "status" => false,
        "message" => "ID user tidak valid"
    ]);
    exit;
}

try {
    // Ambil password lama dari DB
    $stmt = $pdo->prepare("SELECT password FROM users WHERE id = ?");
    $stmt->execute([$user_id]);
    $user = $stmt->fetch(PDO::FETCH_ASSOC);

    if (!$user) {
        echo json_encode([
            "status" => false,
            "message" => "User tidak ditemukan"
        ]);
        exit;
    }

    // Verifikasi password lama
    if (!password_verify($old_password, $user['password'])) {
        echo json_encode([
            "status" => false,
            "message" => "Password lama salah"
        ]);
        exit;
    }

    // Update password baru
    $hashed = password_hash($new_password, PASSWORD_BCRYPT);
    $update = $pdo->prepare("UPDATE users SET password = ? WHERE id = ?");
    $update->execute([$hashed, $user_id]);

    echo json_encode([
        "status" => true,
        "message" => "Password berhasil diubah"
    ]);
} catch (PDOException $e) {
    echo json_encode([
        "status" => false,
        "message" => "Terjadi kesalahan saat mengubah password",
        "error" => $e->getMessage()
    ]);
}
?>
