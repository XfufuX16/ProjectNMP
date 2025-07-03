package com.example.healingyuk.ui.drawer

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.healingyuk.data.api.ApiService
import com.example.healingyuk.databinding.ActivityChangePasswordBinding
import com.example.healingyuk.util.SessionManager

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // ✅ Setup Toolbar dengan tombol kembali
        supportActionBar?.apply {
            title = "Ganti Password"
            setDisplayHomeAsUpEnabled(true)
        }

        binding.btnChange.setOnClickListener {
            val oldPass = binding.etOldPass.text.toString().trim()
            val newPass = binding.etNewPass.text.toString().trim()
            val repeatPass = binding.etRepeatPass.text.toString().trim()

            // Validasi input
            if (oldPass.isEmpty() || newPass.isEmpty() || repeatPass.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPass != repeatPass) {
                Toast.makeText(this, "Password baru tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kirim request ubah password
            val request = object : StringRequest(
                Method.POST,
                ApiService.CHANGE_PASSWORD,
                {
                    Toast.makeText(this, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                    finish()
                },
                { error ->
                    Toast.makeText(this, "Gagal ubah password: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> = hashMapOf(
                    "user_id" to session.getUserId().toString(),
                    "old_password" to oldPass,
                    "new_password" to newPass,
                    "repeat_password" to repeatPass
                )
            }

            Volley.newRequestQueue(this).add(request)
        }
    }

    // ✅ Aksi tombol back di toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
