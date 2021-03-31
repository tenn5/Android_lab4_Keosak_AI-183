package fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ua.opu.contactlist.Contact;
import ua.opu.contactlist.DataViewModel;
import ua.opu.contactlist.R;

public class AddContactFragment extends Fragment {

    private DataViewModel viewModel;
    private Uri uri;
    private ImageView mContactImage;
    private static final int IMAGE_CAPTURE_REQUEST_CODE = 7777;

    public AddContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_contact, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Меняем ImageView с изображением контакта
            mContactImage.setImageBitmap(imageBitmap);

            String filename = "contact_" + System.currentTimeMillis() + ".png";

            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(outputFile);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                // Сохраняем путь к файлу в формате Uri

                uri = Uri.fromFile(outputFile);

                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        mContactImage = view.findViewById(R.id.profile_image);

        EditText nameEditText = view.findViewById(R.id.name_et);
        EditText EmailEditText = view.findViewById(R.id.email_et);
        EditText PhoneEditText = view.findViewById(R.id.phone_et);

        Button addContactButton = view.findViewById(R.id.button_add);
        addContactButton.setOnClickListener(v -> {
            if (uri == null) {
                Toast.makeText(requireContext(), "Contact image not set!", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = nameEditText.getText().toString();
            String email = EmailEditText.getText().toString();
            String phone = PhoneEditText.getText().toString();

            Contact contact = new Contact(name, email, phone, uri);
            viewModel.addContact(contact);

            getActivity().onBackPressed();
        });

        Button takePhotoButton = view.findViewById(R.id.button_camera);
        takePhotoButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(requireContext(), "Error while trying to open camera app", Toast.LENGTH_SHORT).show();
            }
        });

        Button cancelButton = view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(v -> getActivity().onBackPressed());
    }
}