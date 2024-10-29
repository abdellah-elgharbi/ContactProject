package abdellah.project.mycontact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import abdellah.project.mycontact.beans.Contact;

public class EditContactActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText phoneEditText;
    private Button saveButton;
    private Contact contact;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        nameEditText = findViewById(R.id.edit_contact_name);
        phoneEditText = findViewById(R.id.edit_contact_phone);
        saveButton = findViewById(R.id.save_button);

        // Récupérer le contact depuis l'intent
        contact = (Contact) getIntent().getSerializableExtra("contact");
        if (contact != null) {
            nameEditText.setText(contact.getNom());
            phoneEditText.setText(contact.getNumber());
        }

        // Enregistrer les modifications
        saveButton.setOnClickListener(v -> {
            String updatedName = nameEditText.getText().toString();
            String updatedPhone = phoneEditText.getText().toString();

            // Mettre à jour le contact
            contact.setNom(updatedName);
            contact.setNumber(updatedPhone);

            // Retourner le résultat à MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("contact", (CharSequence) contact);
            setResult(RESULT_OK, resultIntent);
            finish();  // Fermer l'activité
        });
    }
}
