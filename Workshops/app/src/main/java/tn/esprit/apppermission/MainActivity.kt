package tn.esprit.apppermission

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE = 113

    // L'importation du l'image
    private lateinit var profilePic: ImageView
    private lateinit var selectedImageUri: Uri
    private val startForResultOpenGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data!!.data!!
                profilePic!!.setImageURI(selectedImageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profilePic = findViewById(R.id.addImage)
        profilePic!!.setOnClickListener {
            checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
        }
    }

    // Fonction checkPermission

    private fun checkPermission(permission: String, requestCode: Int) {

        // Vérifier si la permission est déjà acceptée
        if (ContextCompat.checkSelfPermission(this@MainActivity, permission) == PackageManager.PERMISSION_DENIED) {

            // Demander l’autorisation
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this@MainActivity, "Permission Granted Already", Toast.LENGTH_LONG).show()
        }
    }

    // Récupérer la réponse de l’utilisateur

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                openGallery()
                Toast.makeText(this@MainActivity, "Storage Permission Granted", Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(this@MainActivity, "Storage Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startForResultOpenGallery.launch(intent)
    }
}