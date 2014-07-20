package lab3.log530.com.lab3.boards;

import lab3.log530.com.lab3.Board;

/**
 * Creates a new board on demand, for use of board copying.
 */
public final class BoardFactory {

    /**
     * The Gothic lab3 board.
     */
    private static Class gothic = (new Gothic()).getClass();

    /**
     * The standard lab3 board.
     */
    private static Class standard = (new StandardBoard()).getClass();

    /**
     * An empty lab3 board.
     */
    private static Class empty = (new EmptyBoard()).getClass();

    /**
     * Hidden constructor.
     */
    private BoardFactory() {
    }

    /**
     * Create a new lab3 board of the given class.
     *
     * @param board class to be created
     * @return a fresh board
     */
    public static Board create(final Class board) {
        if (board.equals(standard)) {
            return new StandardBoard();
        } else if (board.equals(gothic)) {
            return new Gothic();
        } else if (board.equals(empty)) {
            return new EmptyBoard();
        } else {
            /* Throw exception? */
            return null;
        }
    }
}
