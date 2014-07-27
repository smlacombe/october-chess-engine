package lab3.log530.com.lab3.gui;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import lab3.log530.com.lab3.Board;
import lab3.log530.com.lab3.Game;
import lab3.log530.com.lab3.GameEvent;
import lab3.log530.com.lab3.GameListener;
import lab3.log530.com.lab3.Player;
import lab3.log530.com.lab3.R;
import lab3.log530.com.lab3.ai.Minimax;
import lab3.log530.com.lab3.boards.EmptyBoard;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {link GameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GameFragment extends Fragment implements GameListener {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    ViewGroup rootView;

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /** The board display. */
    private BoardView display;

    /** The progress bar on the display. */
    private ProgressBar statusBar;

    /** Status text. */
    private TextView statusText;

    /** The current game. */
    private Game myGame;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static GameFragment create(int pageNumber) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Player getDisplay()
    {
        return display;
    }

    /**
     * Set up a new game.
     */
    public final void newGame(Game game) {
        myGame = game;
        Board board = game.getBoard();
        display.setBoard(board);
        display.invalidate();
        //setSize(getPreferredSize());

        //todo: initialize progress bar and status text
        //progress.setGame(game);
        statusText.setText("Chess game for Android");
        game.addGameListener(this);
        game.addGameListener(display);
        game.begin();
    }

    public GameFragment() {
    }

    @Override
    public final void gameEvent(final GameEvent e) {
        //Todo: reresh progress bar and status text
        if (myGame != null) {
            statusBar.setProgress(Math.round(myGame.getProgress() * 100));

            if (myGame.getStatus() != "" || myGame.getStatus() != null) {
                statusText.setText(myGame.getStatus());
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_game, container, false);

        display =  (BoardView) rootView.findViewById(R.id.boardView);
        display.setBoard(new EmptyBoard());
        statusBar = (ProgressBar) rootView.findViewById(R.id.progressBarStatus);
        statusText = (TextView) rootView.findViewById(R.id.textViewStatus);
        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

}
