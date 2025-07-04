<?php
require_once '../config/db.php';

header("Content-Type: application/json");

// Ambil data dari POST
$name = $_POST['name'] ?? '';
$email = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';
$repeat_password = $_POST['confirm_password'] ?? ''; 

// Validasi input kosong
if (empty($name) || empty($email) || empty($password) || empty($repeat_password)) {
    echo json_encode(["status" => false, "message" => "Field tidak boleh kosong"]);
    exit;
}

// Validasi password match
if ($password !== $repeat_password) {
    echo json_encode(["status" => false, "message" => "Password tidak sama"]);
    exit;
}

// Cek email sudah terdaftar
$stmt = $pdo->prepare("SELECT id FROM users WHERE email = ?");
$stmt->execute([$email]);
if ($stmt->fetch()) {
    echo json_encode(["status" => false, "message" => "Email sudah terdaftar"]);
    exit;
}

// Simpan user baru
$hashedPassword = password_hash($password, PASSWORD_BCRYPT);
$stmt = $pdo->prepare("INSERT INTO users (name, email, password) VALUES (?, ?, ?)");
$success = $stmt->execute([$name, $email, $hashedPassword]);

if ($success) {
    echo json_encode(["status" => true, "message" => "Registrasi berhasil"]);
} else {
    echo json_encode(["status" => false, "message" => "Gagal registrasi"]);
}
