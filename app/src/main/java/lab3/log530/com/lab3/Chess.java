package lab3.log530.com.lab3;

//TODO
//import lab3.log530.com.lab3.gui.ChessFrame;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Logger;
//TODO
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class for the Chess game application.
 */
public final class Chess {

    private static final String LOG_TAG = "Chess";

    /** The program's running title, prefix only. */
    private static final String TITLE_PREFIX = "October Chess";

    /**
     * Hidden constructor.
     */
    private Chess() {
    }

    /**
     * The main method of the Chess game application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        //TODO
        /*
        try {
            String lnf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lnf);
        } catch (IllegalAccessException e) {
            Log.w("Failed to access 'Look and Feel'");
        } catch (InstantiationException e) {
            Log.w("Failed to instantiate 'Look and Feel'");
        } catch (ClassNotFoundException e) {
            Log.w("Failed to find 'Look and Feel'");
        } catch (UnsupportedLookAndFeelException e) {
            Log.w("Failed to set 'Look and Feel'");
        }
        new ChessFrame();*/
    }

    /**
     * Returns the full title for the program, including version number.
     *
     * @return the title of the program
     */
    public static String getTitle() {
        String version = "";
        try {
            InputStream s = Chess.class.getResourceAsStream("/version.txt");
            Reader isr = new InputStreamReader(s, "UTF-8");
            BufferedReader in = new BufferedReader(isr);
            version = in.readLine();
            in.close();
        } catch (java.io.IOException e) {
            Log.w(LOG_TAG, "failed to read version info");
            version = "";
        }
        return TITLE_PREFIX + " " + version;
    }
}
