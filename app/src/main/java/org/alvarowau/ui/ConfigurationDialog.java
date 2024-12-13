package org.alvarowau.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import org.alvarowau.model.Configuration;
import org.alvarowau.R;

import java.util.List;

public class ConfigurationDialog {

    private final Context context;
    private final List<Configuration> configurations;
    private Configuration selectedConfiguration;

    public ConfigurationDialog(Context context, List<Configuration> configurations, Configuration initialConfiguration) {
        this.context = context;
        this.configurations = configurations;
        this.selectedConfiguration = initialConfiguration;
    }

    public void show() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialogo_configuracion, null);

        ListView configurationList = dialogView.findViewById(R.id.lista_configuraciones);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_list_item_single_choice,
                getConfigurationNames()
        );

        configurationList.setAdapter(adapter);
        configurationList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        configurationList.setItemChecked(configurations.indexOf(selectedConfiguration), true);

        // Create the dialog with the custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.selecciona_configuracion)
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the selected configuration
                        int selectedPosition = configurationList.getCheckedItemPosition();
                        selectedConfiguration = configurations.get(selectedPosition);
                        dialog.dismiss(); // Close the dialog after confirmation
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    public Configuration getSelected() {
        return selectedConfiguration;
    }

    private String[] getConfigurationNames() {
        String[] names = new String[configurations.size()];
        for (int i = 0; i < configurations.size(); i++) {
            names[i] = configurations.get(i).getName();
        }
        return names;
    }
}
