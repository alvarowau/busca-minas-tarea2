package org.alvarowau.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.alvarowau.R;

public class InstructionsDialog {

    private final Context context;

    public InstructionsDialog(Context context) {
        this.context = context;
    }

    public void show() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialogo_instrucciones, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.instructions_title);

        builder.setView(dialogView);

        builder.setNeutralButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView instructionsText = dialogView.findViewById(R.id.instrucciones_text);
        instructionsText.setText(R.string.instructions_content);
        instructionsText.setTextColor(context.getResources().getColor(R.color.text_color_instructions));
        instructionsText.setTextSize(16);
    }
}
