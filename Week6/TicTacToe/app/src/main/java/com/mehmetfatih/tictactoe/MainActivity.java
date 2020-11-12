package com.mehmetfatih.tictactoe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final String PLAYER_1 = "X";
    static final String PLAYER_2 = "O";
    boolean player1Turn = true;
    boolean isGameEnded = false;
    byte[][] board = new byte[3][3];
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout tableLayout = findViewById(R.id.board);
        for (int i = 0; i < 3; i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button button = (Button) tableRow.getChildAt(j);
                button.setOnClickListener(new CellListener(i, j));
            }
        }

    }

    class CellListener implements View.OnClickListener{

        int row;
        int col;

        public CellListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View view) {

            if (!isValidMove(row, col)){
                Toast.makeText(MainActivity.this, "Cell is already occupied", Toast.LENGTH_SHORT).show();
                return;
            }

            if (player1Turn) {
                ((Button) view).setText(PLAYER_1);
                board[row][col] = 1;
            } else {
                ((Button) view).setText(PLAYER_2);
                board[row][col] = 2;
            }

            if (gameEnded(row, col) == -1) {
                player1Turn = !player1Turn;
            } else if (gameEnded(row, col) == 1) {
                Toast.makeText(MainActivity.this, "Player 1 Won the Game", Toast.LENGTH_LONG).show();
                disableButtons();
                isGameEnded = true;
            } else if (gameEnded(row, col) == 2) {
                Toast.makeText(MainActivity.this, "Player 2 Won the Game", Toast.LENGTH_LONG).show();
                disableButtons();
                isGameEnded = true;
            }
        }
    }

    public boolean isValidMove (int row, int col) {
        return (board[row][col] == 0);
    }

    public int gameEnded (int row, int col) {

        counter++;
        int symbol = board[row][col];
        boolean win = true;

        for (int i = 0; i < 3; i++) {
            if (board[i][col] != symbol) {
                win = false;
                break;
            }
        }

        if (win) {
            return symbol;
        }else {
            win = true;
        }

        for (int i = 0; i < 3; i++) {
            if (board[row][i] != symbol) {
                win = false;
                break;
            }
        }

        if (win) {
            return symbol;
        } else {
            win = true;
        }

        if ((symbol != board[0][1] && symbol != board[1][0] && symbol != board[1][2] && symbol != board[2][1]) &&
                ((board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                        (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol))) {
            return symbol;
        }

        if (counter == 9){
            disableButtons();
            Toast.makeText(MainActivity.this, "It is a draw", Toast.LENGTH_LONG).show();
            isGameEnded = true;
            return 0;
        }


        return -1;
    }

    public void disableButtons () {
        TableLayout tableLayout = findViewById(R.id.board);
        for (int i = 0; i < 3; i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button button = (Button) tableRow.getChildAt(j);
                button.setClickable(false);
            }
        }
        ActionMenuItemView menuItem = findViewById(R.id.menuItem);
        menuItem.setText("Play Again!");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("player1Turn", player1Turn);
        byte[] boardSingle = new byte[9];
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                boardSingle[3*i + j] = board[i][j];
            }
        }
        outState.putByteArray("board", boardSingle);
        outState.putBoolean("Is game ended?", isGameEnded);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isGameEnded = savedInstanceState.getBoolean("Is game ended?");
        if (isGameEnded) {
            disableButtons();
        }
        player1Turn = savedInstanceState.getBoolean("player1Turn");
        byte[] boardSingle = savedInstanceState.getByteArray("board");
        for (int i = 0; i < 9; i++){
            board[i / 3][i % 3] = boardSingle[i];
        }

        TableLayout tableLayout = findViewById(R.id.board);
        for (int i = 0; i < 3; i++){
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < 3; j++){
                Button button = (Button) tableRow.getChildAt(j);
                if (board[i][j] == 1) {
                    button.setText("X");
                } else if (board[i][j] == 2) {
                    button.setText("O");
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuItem:
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}





















