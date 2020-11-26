package com.mehmetfatih.tictactoemvp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements BoardView {
    BoardPresenter presenter;
    TableLayout boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new BoardPresenter(this);
        boardView = findViewById(R.id.board); //register button listeners for (byte row=0; row<3; row++){ TableRow tableRow = (TableRow)boardView.getChildAt(row); for (byte col=0; col<3; col++){ Button button = (Button)tableRow.getChildAt(col); BoardPresenter.CellClickListener clickListener = new BoardPresenter.CellClickListener(presenter,row,col); button.setOnClickListener(clickListener); presenter.addCellClickListener(clickListener); }
    }


    @Override
    public void newGame() {
        TableLayout boardView = findViewById(R.id.board);
        for (int row = 0; row < 3; row++) {
            TableRow tableRow = (TableRow) boardView.getChildAt(row);
            for (int col = 0; col < 3; col++) {
                Button button = (Button) tableRow.getChildAt(col);
                button.setText("");
                button.setEnabled(true);
            }
        }
    }

    @Override
    public void putSymbol(char symbol, byte row, byte col) {
        TableRow tableRow = (TableRow) boardView.getChildAt(row);
        Button button = (Button) tableRow.getChildAt(col);
        button.setText(Character.toString(symbol));
    }

    @Override
    public void gameEnded(byte winner) {
        for (int row = 0; row < 3; row++) {
            TableRow tableRow = (TableRow) boardView.getChildAt(row);
            for (int col = 0; col < 3; col++) {
                Button button = (Button) tableRow.getChildAt(col);
                button.setText("");
                button.setEnabled(false);
            }
        }
        switch (winner) {
            case BoardView.DRAW:
                Toast.makeText(this, "Game is Draw", Toast.LENGTH_LONG).show();
                break;
            case BoardView.PLAYER_1_WINNER:
                Toast.makeText(this, "Player 1 Wins", Toast.LENGTH_LONG).show();
                break;
            case BoardView.PLAYER_2_WINNER:
                Toast.makeText(this, "Player 2 Wins", Toast.LENGTH_LONG).show();
                break;
        }
    }
}

class BoardPresenter implements BoardListener {
    private BoardView boardView;
    private Board board;
    private List<CellClickListener> cellClickListeners = new ArrayList<>();

    public BoardPresenter(BoardView view) {
        this.boardView = view;
        board = new Board(this);
    }

    private void move(byte row, byte col) {
        board.move(row, col);
    }

    public void addCellClickListener(CellClickListener listener) {
        cellClickListeners.add(listener);
    }

    @Override
    public void playedAt(byte player, byte row, byte col) {
        if (player == BoardListener.PLAYER_1) {
            boardView.putSymbol(BoardView.PLAYER_1_SYMBOL, row, col);
        } else if (player == BoardListener.PLAYER_2) {
            boardView.putSymbol(BoardView.PLAYER_2_SYMBOL, row, col);
        }
    }

    @Override
    public void gameEnded(byte winner) {
        switch (winner) {
            case BoardListener.NO_ONE:
                boardView.gameEnded(BoardView.DRAW);
            case BoardListener.PLAYER_1:
                boardView.gameEnded(BoardView.PLAYER_1_WINNER);
            case BoardListener.PLAYER_2:
                boardView.gameEnded(BoardView.PLAYER_2_WINNER);
        }
    }

    static class CellClickListener implements View.OnClickListener {
        BoardPresenter presenter;
        byte row;
        byte col;

        public CellClickListener(BoardPresenter presenter, byte row, byte col) {
            this.row = row;
            this.col = col;
            this.presenter = presenter;
        }

        @Override
        public void onClick(View view) {
            Log.d("CellClickListener", "at" + row + ", " + col);
            presenter.move(row, col);
        }
    }
}