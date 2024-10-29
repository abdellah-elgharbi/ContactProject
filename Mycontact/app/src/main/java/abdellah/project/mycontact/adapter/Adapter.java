package abdellah.project.mycontact.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import abdellah.project.mycontact.R;
import abdellah.project.mycontact.beans.Contact;

public class Adapter extends BaseAdapter {
    private List<Contact> contacts;            // Liste filtrée
    private List<Contact> originalContacts;    // Liste complète (non filtrée)
    private LayoutInflater inflater;
    private List<Integer> images = new ArrayList<>();

    public Adapter(List<Contact> contacts, Activity activity) {
        this.contacts = new ArrayList<>(contacts);  // Initialiser avec une copie des contacts
        this.originalContacts = new ArrayList<>(contacts);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Ajouter les ressources d'images disponibles ici
        images.add(R.drawable.image1);
        images.add(R.drawable.image3);
        images.add(R.drawable.image4);
        images.add(R.drawable.image5);
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact, null);
        }

        TextView nameView = convertView.findViewById(R.id.contact_name);
        TextView phoneView = convertView.findViewById(R.id.contact_phone);
        ImageView image = convertView.findViewById(R.id.image);

        Contact contact = contacts.get(position);
        nameView.setText(contact.getNom());
        phoneView.setText(contact.getNumber());

        int randomImage = getRandomElement(images);
        image.setImageResource(randomImage);

        // Ajouter un listener pour modifier le contact
        convertView.setOnClickListener(v -> showEditDialog(contact));

        return convertView;
    }

    // Afficher un AlertDialog pour modifier le contact
    private void showEditDialog(Contact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
        builder.setTitle("Modifier le contact");

        // Créer un layout pour l'alerte
        View dialogView = inflater.inflate(R.layout.dialog_edit_contact, null);
        builder.setView(dialogView);

        EditText nameInput = dialogView.findViewById(R.id.edit_contact_name);
        EditText phoneInput = dialogView.findViewById(R.id.edit_contact_phone);

        // Remplir les champs avec les données actuelles
        nameInput.setText(contact.getNom());
        phoneInput.setText(contact.getNumber());

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Mettre à jour les informations du contact
                contact.setNom(nameInput.getText().toString());
                contact.setNumber(phoneInput.getText().toString());
                notifyDataSetChanged();  // Notifier l'adaptateur pour rafraîchir la liste
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();  // Fermer le dialog sans faire de modifications
            }
        });

        builder.show();  // Afficher le dialog
    }

    public static <T> T getRandomElement(List<T> list) {
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }

    public void filter(String query) {
        query = query.toLowerCase(Locale.ROOT);
        contacts.clear();

        if (query.isEmpty()) {
            contacts.addAll(originalContacts);
        } else {
            for (Contact contact : originalContacts) {
                if (contact.getNom().toLowerCase(Locale.ROOT).contains(query) ||
                        contact.getNumber().contains(query)) {
                    contacts.add(contact);
                }
            }
        }

        notifyDataSetChanged();
    }
}
