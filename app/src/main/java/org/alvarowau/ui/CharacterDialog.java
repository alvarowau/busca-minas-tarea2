package org.alvarowau.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.alvarowau.R;
import org.alvarowau.model.Character;
import org.alvarowau.adapters.ImageTextAdapter;

import java.util.List;

public class CharacterDialog {
    private final Context context;
    private final List<Character> characters;
    private Character selectedCharacter;
    private String[] characterNames;
    private int[] images;

    public CharacterDialog(Context context, List<Character> characters, Character initialCharacter) {
        this.context = context;
        this.characters = characters;
        this.selectedCharacter = initialCharacter;
    }

    public void show(MenuItem characterItem) {
        characterNames = new String[characters.size()];
        images = new int[characters.size()];

        for (int i = 0; i < characters.size(); i++) {
            Character character = characters.get(i);
            characterNames[i] = character.getName();
            images[i] = character.getImage();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.character_title);

        // Inflate and set the custom spinner
        Spinner spinner = getSpinner();
        builder.setView(spinner);

        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeIcon(characterItem, selectedCharacter.getImage());
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private Spinner getSpinner() {
        // Create the custom Spinner and adapter
        Spinner spinner = new Spinner(context);
        spinner.setAdapter(new ImageTextAdapter(context, characterNames, images));

        // Set the selected character index
        int selectedIndex = 0;
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).equals(selectedCharacter)) {
                selectedIndex = i;
                break;
            }
        }
        spinner.setSelection(selectedIndex);

        // Handle selection events
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCharacter = characters.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return spinner;
    }

    public Character getSelected() {
        return selectedCharacter;
    }

    public static void changeIcon(MenuItem item, int selectedIcon) {
        item.setIcon(selectedIcon);
    }
}
