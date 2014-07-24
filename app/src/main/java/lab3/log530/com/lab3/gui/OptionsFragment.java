package lab3.log530.com.lab3.gui;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import lab3.log530.com.lab3.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {link OptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {link OptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class OptionsFragment extends Fragment {

    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    private Spinner levelSpinnerB;
    private Spinner levelSpinnerW;
    private RadioButton rButtonComputerW;
    private RadioButton rButtonComputerB;
    private RadioGroup radioGroupW;
    private RadioGroup radioGroupB;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static OptionsFragment create(int pageNumber) {
        OptionsFragment fragment = new OptionsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public OptionsFragment() {
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
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_options, container, false);

        levelSpinnerB = (Spinner) rootView.findViewById(R.id.spinnerIAB);
        levelSpinnerW = (Spinner) rootView.findViewById(R.id.spinnerIAW);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.ia_choice, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        levelSpinnerW.setAdapter(adapter);
        levelSpinnerB.setAdapter(adapter);

        radioGroupW = (RadioGroup) rootView.findViewById(R.id.rGroupW);
        radioGroupB = (RadioGroup) rootView.findViewById(R.id.rGroupB);
        rButtonComputerW = (RadioButton) rootView.findViewById(R.id.rBComputerW);
        rButtonComputerB = (RadioButton) rootView.findViewById(R.id.rBComputerB);
        enableOrDisableRadioButton();

        radioGroupW.setOnCheckedChangeListener(new LevelRadioGroupListener());
        radioGroupB.setOnCheckedChangeListener(new LevelRadioGroupListener());

        return rootView;
    }

    private void enableOrDisableRadioButton()
    {

        if (rButtonComputerB.isChecked())
            levelSpinnerB.setEnabled(true);
        else
            levelSpinnerB.setEnabled(false);

        if (rButtonComputerW.isChecked())
            levelSpinnerW.setEnabled(true);
        else
            levelSpinnerW.setEnabled(false);
    }

    class LevelRadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            enableOrDisableRadioButton();
        }
    };

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }


}
