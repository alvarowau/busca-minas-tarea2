package org.alvarowau.game;

import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.alvarowau.R;
import org.alvarowau.model.Character;
import org.alvarowau.model.Configuration;
import org.alvarowau.ui.ConfigurationDialog;
import org.alvarowau.ui.InstructionsDialog;
import org.alvarowau.ui.CharacterDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    // Main variables used in the activity
    Board board;
    LinearLayout mainLayout;
    MenuItem characterMenuItem;
    Button tileButton;
    TableLayout boardLayout;
    Drawable characterImage;

    boolean isPlaying = false;
    int foundHypotenochas = 0;
    private InstructionsDialog instructionsDialog;
    private CharacterDialog characterDialog;
    private ConfigurationDialog configurationDialog;
    List<Configuration> availableConfigurations;
    Configuration currentConfiguration;

    List<Character> availableCharacters;
    Character currentCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.tablero);
        instructionsDialog = new InstructionsDialog(this);
        String[] configurationNames = getResources().getStringArray(R.array.config_nombres);
        int[] cellsPerConfiguration = getResources().getIntArray(R.array.config_casillas);
        int[] hypotenochasPerConfiguration = getResources().getIntArray(R.array.config_hipos);

        availableConfigurations = new ArrayList<>();
        for (int i = 0; i < configurationNames.length; i++) {
            availableConfigurations.add(
                    new Configuration(configurationNames[i], cellsPerConfiguration[i], hypotenochasPerConfiguration[i])
            );
        }
        currentConfiguration = availableConfigurations.get(0);
        configurationDialog = new ConfigurationDialog(this, availableConfigurations, currentConfiguration);
        String[] characterNames = getResources().getStringArray(R.array.personaje_nombre);
        TypedArray characterImages = getResources().obtainTypedArray(R.array.personajes_imagenes);

        availableCharacters = new ArrayList<>();
        for (int i = 0; i < characterNames.length; i++) {
            int imageId = characterImages.getResourceId(i, -1);
            availableCharacters.add(new Character(characterNames[i], imageId));
        }
        characterImages.recycle();

        currentCharacter = availableCharacters.get(0);
        characterDialog = new CharacterDialog(this, availableCharacters, currentCharacter);

        populateButtons(currentConfiguration.getCells());
        initGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_actionbar, menu);

        // Asignación explícita de íconos a los elementos del menú
        menu.findItem(R.id.menu_instrucciones).setIcon(R.drawable.ic_instrucciones);
        menu.findItem(R.id.menu_nuevo).setIcon(R.drawable.ic_nuevo_juego);
        menu.findItem(R.id.menu_config).setIcon(R.drawable.ic_config);
        menu.findItem(R.id.menu_personaje).setIcon(R.drawable.homer); // Aquí asignamos el ícono del personaje

        characterMenuItem = menu.findItem(R.id.menu_personaje);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.menu_instrucciones) {
            instructionsDialog.show();
        }
        if (itemId == R.id.menu_config) {
            configurationDialog.show();
            startNewGame();
        }
        if (itemId == R.id.menu_nuevo) {
            startNewGame();
        }
        if (itemId == R.id.menu_personaje) {
            characterDialog.show(characterMenuItem);
            startNewGame();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startNewGame() {
        isPlaying = true;

        currentConfiguration = configurationDialog.getSelected();
        currentCharacter = characterDialog.getSelected();

        populateButtons(currentConfiguration.getCells());
        characterImage = getResources().getDrawable(currentCharacter.getImage());
        board = new Board(currentConfiguration);
        board.play();
    }

    @Override
    public void onClick(View view) {
        // Get the coordinates from the button's text
        String[] coordinates = ((Button) view).getText().toString().split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);

        int result = board.checkCell(x, y);
        Button button = (Button) view;
        button.setPadding(0, 0, 0, 0);

        switch (result) {
            case -1:
                button.setText("X");
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                button.setTextColor(Color.BLACK);
                button.setBackground(characterImage);
                button.setScaleY(-1);
                endGameWithLoss(view);
                break;

            case 0:
                button.setBackgroundColor(Color.GRAY);
                clearAdjacentCells(view, x, y);
                break;

            default:
                button.setText(String.valueOf(result));
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                button.setTextColor(Color.WHITE);
                button.setBackgroundColor(Color.GRAY);
                view.setEnabled(false);
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        String[] coordinates = ((Button) view).getText().toString().split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);

        int result = board.checkCell(x, y);
        Button button = (Button) view;
        button.setPadding(0, 0, 0, 0);

        if (result == -1) {
            button.setBackground(characterImage);
            foundHypotenochas++;
            if (foundHypotenochas == currentConfiguration.getHypotenochas()) {
                endGameWithWin(view);
            }
        } else {
            button.setText(String.valueOf(result));
            button.setTextSize(20);
            button.setTextColor(Color.WHITE);
            button.setBackgroundColor(Color.GRAY);
            view.setEnabled(false);
            endGameWithLoss(view);
        }

        return true;
    }
    private void clearAdjacentCells(View view, int x, int y) {
        for (int xt = -1; xt <= 1; xt++) {
            for (int yt = -1; yt <= 1; yt++) {
                if (xt != yt) {
                    if (board.checkCell(x + xt, y + yt) == 0 && !board.isPressed(x + xt, y + yt)) {
                        Button b = (Button) getButton(x + xt, y + yt);
                        b.setBackgroundColor(Color.GRAY);
                        b.setClickable(false);
                        board.setPressed(x + xt, y + yt);
                        String[] coordinates = b.getText().toString().split(",");
                        clearAdjacentCells(b, Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
                    }
                }
            }
        }
    }

    private View getButton(int x, int y) {
        for (int i = 0; i < boardLayout.getChildCount(); i++) {
            TableRow tr = (TableRow) boardLayout.getChildAt(i);
            for (int j = 0; j < tr.getChildCount(); j++) {
                Button b = (Button) tr.getChildAt(j);
                if (b.getText().toString().equals(x + "," + y)) {
                    return b;
                }
            }
        }
        return null;
    }

    private void populateButtons(int buttons) {
        boardLayout = new TableLayout(this);
        boardLayout.setStretchAllColumns(true);
        boardLayout.setShrinkAllColumns(true);
        boardLayout.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        boardLayout.setWeightSum(buttons);

        for (int i = 0; i < buttons; i++) {
            TableRow tr = new TableRow(this);
            tr.setGravity(Gravity.CENTER);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));

            for (int j = 0; j < buttons; j++) {
                Button tileButton = new Button(this);
                tileButton.setWidth(20);
                tileButton.setHeight(20);
                tileButton.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                tileButton.setId(View.generateViewId());
                tileButton.setText(i + "," + j);
                tileButton.setTextSize(0);
                tileButton.setOnClickListener(this);
                tileButton.setOnLongClickListener(this);
                tr.addView(tileButton);
            }
            boardLayout.addView(tr);
        }

        mainLayout.removeAllViews();
        mainLayout.addView(boardLayout);

        if (!isPlaying) disableBoard(boardLayout);
    }

    private void disableBoard(View view) {
        TableLayout tl = (TableLayout) view;
        for (int i = 0; i < tl.getChildCount(); i++) {
            TableRow tr = (TableRow) tl.getChildAt(i);
            for (int j = 0; j < tr.getChildCount(); j++) {
                Button b = (Button) tr.getChildAt(j);
                b.setEnabled(false);
            }
        }
    }

    private void endGameWithWin(View view) {
        TableLayout tl = (TableLayout) view.getParent().getParent();
        isPlaying = false;
        foundHypotenochas = 0;
        Toast.makeText(MainActivity.this, "¡HAS GANADO!", Toast.LENGTH_SHORT).show();
        disableBoard(tl);
        showRestartDialog("¡Has ganado! ¿Quieres iniciar un nuevo juego?");
    }

    private void endGameWithLoss(View view) {
        TableLayout tl = (TableLayout) view.getParent().getParent();
        isPlaying = false;
        foundHypotenochas = 0;
        Toast.makeText(MainActivity.this, "¡HAS PERDIDO!", Toast.LENGTH_SHORT).show();
        disableBoard(tl);
        showRestartDialog("¡Has perdido! ¿Quieres iniciar un nuevo juego?");
    }

    private void showRestartDialog(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(mensaje)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restartGame();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void restartGame() {
        Toast.makeText(MainActivity.this, "Reiniciando el juego...", Toast.LENGTH_SHORT).show();
        startNewGame();
    }

    private void initGame(){
        Toast.makeText(MainActivity.this, "Iniciando el juego...", Toast.LENGTH_SHORT).show();
        startNewGame();
    }

}
