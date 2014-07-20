package lab3.log530.com.lab3;

import lab3.log530.com.lab3.ai.Minimax;
import lab3.log530.com.lab3.boards.StandardBoard;
import java.util.logging.Logger;
//TODO
/*import lab3.log530.com.lab3.gui.BoardPanel;
import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;*/

/**
 * Applet that runs a game versus the computer, no other options.
 */
//TODO
public final class ChessApplet /*extends JApplet*/ implements GameListener {

    /** This class's Logger. */
    private static final Logger LOG =
        Logger.getLogger("com.nullprogram.lab3.ChessApplet");

    /** Version for object serialization. */
    private static final long serialVersionUID = 34863129470926196L;

    //@Override
    public void init() {
        //TODO
        /*try {
            String lnf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lnf);
        } catch (IllegalAccessException e) {
            LOG.warning("Failed to access 'Look and Feel'");
        } catch (InstantiationException e) {
            LOG.warning("Failed to instantiate 'Look and Feel'");
        } catch (ClassNotFoundException e) {
            LOG.warning("Failed to find 'Look and Feel'");
        } catch (UnsupportedLookAndFeelException e) {
            LOG.warning("Failed to set 'Look and Feel'");
        }*/

        StandardBoard board = new StandardBoard();
        //TODO
        /*BoardPanel panel = new BoardPanel(board);
        add(panel);
        Game game = new Game(board);
        game.seat(panel, new Minimax(game));
        game.addGameListener(this);
        game.addGameListener(panel);
        game.begin();*/
    }

    @Override
    public void gameEvent(final GameEvent e) {
        if (e.getGame().isDone()) {
            String message;
            Piece.Side winner = e.getGame().getWinner();
            if (winner == Piece.Side.WHITE) {
                message = "White wins";
            } else if (winner == Piece.Side.BLACK) {
                message = "Black wins";
            } else {
                message = "Stalemate";
            }
            //TODO
            //JOptionPane.showMessageDialog(null, message);
        }
    }
}
