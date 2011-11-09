package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.MoveList;

/**
 * The Chess archbishop.
 *
 * This class describes the movement and capture behavior of the Chess
 * archbishop (bishop + knight).
 */
public class Archbishop extends Piece {

    /**
     * Create a new archbishop on the given side.
     *
     * @param side piece owner
     */
    public Archbishop(final Side side) {
        super(side, "Archbishop");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        /* Take advantage of the Bishop and Knight implementations */
        list = Bishop.getMoves(this, list);
        list = Knight.getMoves(this, list);
        return list;
    }
}