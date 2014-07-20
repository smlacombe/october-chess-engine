package lab3.log530.com.lab3.pieces;

import lab3.log530.com.lab3.Move;
import lab3.log530.com.lab3.MoveList;
import lab3.log530.com.lab3.Piece;
import lab3.log530.com.lab3.Position;

/**
 * The Chess knight.
 *
 * This class describes the movement and capture behavior of the Chess
 * knight.
 */
public class Knight extends Piece {

    /** Serialization identifier. */
    private static final long serialVersionUID = -524621034L;

    /**
     * Short segment of movement.
     */
    static final int NEAR = 1;

    /**
     * Long segment of movement.
     */
    static final int FAR = 2;

    /**
     * Create a new knight on the given side.
     *
     * @param side piece owner
     */
    public Knight(final Side side) {
        super(side, "Knight");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        list = getMoves(this, list);
        return list;
    }

    /**
     * Determine knight moves for given situation.
     *
     * This method is here for the purposes of reuse.
     *
     * @param p     the piece being tested
     * @param list  list to be appended to
     * @return      the modified list
     */
    public static MoveList getMoves(final Piece p,
                                    final MoveList list) {
        Position pos = p.getPosition();
        list.addCapture(new Move(pos, new Position(pos,  NEAR,  FAR)));
        list.addCapture(new Move(pos, new Position(pos,  FAR,  NEAR)));
        list.addCapture(new Move(pos, new Position(pos, -FAR,  NEAR)));
        list.addCapture(new Move(pos, new Position(pos, -FAR, -NEAR)));
        list.addCapture(new Move(pos, new Position(pos,  FAR, -NEAR)));
        list.addCapture(new Move(pos, new Position(pos,  NEAR, -FAR)));
        list.addCapture(new Move(pos, new Position(pos, -NEAR, -FAR)));
        list.addCapture(new Move(pos, new Position(pos, -NEAR,  FAR)));
        return list;
    }
}
