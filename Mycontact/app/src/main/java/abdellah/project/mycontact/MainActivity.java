package abdellah.project.mycontact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import abdellah.project.mycontact.adapter.Adapter;
import abdellah.project.mycontact.beans.Contact;

public class MainActivity extends AppCompatActivity {
    private static final int CONTACT_PERMISSION_REQUEST_CODE = 1;
    private static final int ADD_CONTACT_REQUEST_CODE = 2;

    private ListView contactListView;
    private SearchView searchView;
    private List<Contact> contactList = new ArrayList<>();
    private Adapter adapter;
    private Button addContactButton;

    private ContentObserver contactObserver;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactListView = findViewById(R.id.list);
        searchView = findViewById(R.id.search_view);


        // Vérifier la permission d'accès aux contacts
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    CONTACT_PERMISSION_REQUEST_CODE);
        } else {
            fetchContacts();
        }

        // Configurer l'observer pour surveiller les contacts
        initContactObserver();

        // Ajouter un listener au bouton pour ajouter un contact


        // Configurer la recherche
        setupSearchView();
    }

    // Configurer le ContentObserver pour surveiller les modifications de contacts
    private void initContactObserver() {
        contactObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                fetchContacts(); // Rafraîchir la liste
            }
        };

        getContentResolver().registerContentObserver(
                ContactsContract.Contacts.CONTENT_URI,
                true,
                contactObserver
        );
    }

    // Lancer l'activité d'ajout de contact
    private void addContact() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        startActivityForResult(intent, ADD_CONTACT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CONTACT_REQUEST_CODE) {
            fetchContacts();
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Contact ajouté", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ajout annulé", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Récupérer la liste des contacts
    private void fetchContacts() {
        contactList.clear(); // Éviter les doublons

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phoneNumber = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                contactList.add(new Contact(name, phoneNumber));
            }
            cursor.close();
        }

        adapter = new Adapter(contactList, this);
        contactListView.setAdapter(adapter);
    }


    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACT_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchContacts();
            } else {
                Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (contactObserver != null) {
            getContentResolver().unregisterContentObserver(contactObserver);
        }
    }
}
