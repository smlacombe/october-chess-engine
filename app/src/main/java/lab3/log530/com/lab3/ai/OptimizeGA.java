package lab3.log530.com.lab3.ai;

import android.util.Log;

import lab3.log530.com.lab3.Board;
import lab3.log530.com.lab3.Game;
import lab3.log530.com.lab3.GameEvent;
import lab3.log530.com.lab3.GameListener;
import lab3.log530.com.lab3.Piece;
import lab3.log530.com.lab3.Player;
import lab3.log530.com.lab3.boards.StandardBoard;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Alternate main class for optimizing AI parameters via genetic algorithm.
 */
public class OptimizeGA implements GameListener {

    private static final String LOG_TAG = "OptimizeGA";

    /** Random number generator.  */
    private static Random rng;

    /** Maximum range for piece values. */
    static final double PIECE_RANGE = 10.0;

    /** Locked-in dpeth: we don't change this in the population. */
    static final int DEPTH = 4;

    /** Maximum number of moves. */
    static final int MAX_MOVES = 75;

    /** Mutation rate. */
    static final double MUTATION_RATE = 0.02;

    /** Mutation variance. */
    static final double MUTATION_VAR = 2.0;

    /** Size of the gene pool. */
    static final int POOL_SIZE = 20;

    /** The gene pool. */
    private Config[] genes;

    /** Current gene scores. */
    private int[] geneScores;

    /** Current index into the gene pool. */
    private int gene;

    /**
     * Hidden constructor.
     */
    protected OptimizeGA() {
        newPool();
        launch(genes[0], genes[1]);
    }

    /**
     * Rather than start the GUI, run undisplayed games.
     *
     * @param args input arguments
     */
    public static void main(final String[] args) {
        rng = new Random();
        new OptimizeGA();
    }

    /**
     * Create a fresh gene pool.
     */
    private void newPool() {
        genes = new Config[POOL_SIZE];
        geneScores = new int[POOL_SIZE];
        gene = 0;
        /* Seed the pool with something reasonable. */
        genes[0] = new Config(Minimax.getConfig("default"));
        for (int i = 1; i < POOL_SIZE; i++) {
            genes[i] = create();
        }
    }

    /**
     * Launch the next experiment.
     */
    private void launchNext() {
        gene++;
        if (gene >= POOL_SIZE) {
            /* Complete */
            nextPool();
            launch(genes[0], genes[1]);
        } else  if (gene == POOL_SIZE - 1) {
            /* Last one. */
            launch(genes[gene], genes[0]);
        } else {
            launch(genes[gene], genes[gene + 1]);
        }
    }

    /**
     * Breed the winners, preparing the new pool.
     */
    private void nextPool() {
        Config[] pool = new Config[POOL_SIZE];
        int poolSize = 0;
        for (int i = 0; i < POOL_SIZE; i++) {
            if (geneScores[i] > 0) {
                pool[poolSize] = genes[i];
                poolSize++;
            }
        }
        if (poolSize == 0) {
            /* No clear winners. */
            newPool();
        } else {
            for (int i = poolSize; i < POOL_SIZE; i++) {
                int a = rng.nextInt(poolSize);
                int b = rng.nextInt(poolSize);
                genes[i] = breed(genes[a], genes[b]);
            }
            Collections.shuffle(Arrays.asList(pool));
        }
        geneScores = new int[POOL_SIZE];
        gene = 0;
        genes = pool;
    }

    /**
     * Launch a game with players using the given configurations.
     *
     * @param whiteConf config for the white player
     * @param blackConf config for the black player
     */
    private void launch(final Config whiteConf, final Config blackConf) {
        Log.i(LOG_TAG, whiteConf.toString());
        Log.i(LOG_TAG, blackConf.toString());
        Board board = new StandardBoard();
        Game game = new Game(board);
        Player white = new Minimax(game, whiteConf.getProperties());
        Player black = new Minimax(game, blackConf.getProperties());
        game.seat(white, black);
        game.addGameListener(this);
        game.begin();
    }

    @Override
    public final void gameEvent(final GameEvent e) {
        boolean doScore = false;
        int adir = 0;
        int bdir = 0;
        Game game = e.getGame();
        if (game.isDone()) {
            if (game.getWinner() == Piece.Side.WHITE) {
                Log.i(LOG_TAG, "White wins.");
                adir = 1;
                bdir = -1;
            } else if (game.getWinner() == Piece.Side.BLACK) {
                Log.i(LOG_TAG, "Black wins.");
                adir = -1;
                bdir = 1;
            } else {
                Log.i(LOG_TAG, "Stalemate.");
                adir = 0;
                bdir = 0;
            }
            doScore = true;
        } else if (game.getBoard().moveCount() > MAX_MOVES) {
            Log.i(LOG_TAG, "Game timeout!");
            game.end();
            doScore = true;
            adir = -1;
            bdir = -1;
        }
        if (doScore) {
            int a = gene;
            int b = gene + 1;
            if (b == POOL_SIZE) {
                b = 0;
            }
            geneScores[a] += adir;
            geneScores[b] += bdir;
            launchNext();
        }
    }

    /**
     * Randomly create a new configuration.
     *
     * @return a randomly generated config
     */
    private static Config create() {
        Config conf = new Config();
        conf.put("depth", (double) DEPTH);
        String[] pieces = {"Pawn", "Knight", "Bishop", "Rook", "Queen",
                           "King", "Chancellor", "Archbishop"
                          };
        for (String piece : pieces) {
            Double v = rng.nextDouble() * PIECE_RANGE;
            if ("King".equals(piece)) {
                /* The king has a much larger value range. */
                v *= PIECE_RANGE * PIECE_RANGE;
            }
            conf.put(piece, v);
        }
        conf.put("material", rng.nextDouble());
        conf.put("safety", rng.nextDouble());
        conf.put("mobility", rng.nextDouble());
        return conf;
    }

    /**
     * Breed two configurations to make a child.
     *
     * @param a first parent
     * @param b second parent
     * @return child config
     */
    private static Config breed(final Config a, final Config b) {
        Config child = new Config();
        for (String prop : Config.PLIST) {
            double ave = (a.get(prop) + b.get(prop)) / 2;
            if ("depth".equals(prop)) {
                ave = DEPTH;
            } else if (rng.nextDouble() < MUTATION_RATE) {
                ave += (rng.nextDouble() * 1.0 / 2.0) * MUTATION_VAR;
                double max = PIECE_RANGE;
                if ("King".equals(prop)) {
                    max = PIECE_RANGE * PIECE_RANGE * PIECE_RANGE;
                }
                if (ave > max) {
                    ave = max;
                } else if (ave < -max) {
                    ave = -max;
                }
            }
            child.put(prop, ave);
        }
        return child;
    }
}
