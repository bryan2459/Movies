package com.go2themovies.example.bryanpc.movies;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import android.content.Intent;
import android.widget.Toast;


/**
 * A fragment representing a single Golfcourse detail screen.
 * This fragment is either contained in a {@link GolfcourseListActivity}
 * in two-pane mode (on tablets) or a {@link GolfcourseDetailActivity}
 * on handsets.
 */
public class GolfcourseDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private ImageView mIconView;
    ImageButton trailerButton,reviewButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GolfcourseDetailFragment() {
    }

    private Golfcourse course;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If intent arguments have a course object, get it
        if (getArguments().containsKey("course")) {
            course = getArguments().getParcelable("course");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.fragment_golfcourse_detail, container, false);

        // Display the selected golfcourse, or just a welcome message
        final Context c = getActivity().getApplicationContext();
        //http://stackoverflow.com/questions/25676479/cant-load-image-with-picasso-into-fragment-view

        if (course != null) {
            ((TextView) rootView.findViewById(R.id.title)).setText(course.name);
            ((TextView) rootView.findViewById(R.id.overview)).setText(course.overview);
          //  ((ImageView) rootView.findViewById(R.id.moviepicture));

            ImageButton trailer = (ImageButton) rootView.findViewById(R.id.reviewer);

            trailer.setVisibility(View.GONE);
            mIconView = (ImageView) rootView.findViewById(R.id.moviepicture);
          //  Picasso.with(c).load(getIntent().getStringExtra(course.address)).into(mIconView);
            Picasso.with(c).load(course.address).into(mIconView);
            trailerButton = (ImageButton) rootView.findViewById(R.id.trailer);
            trailerButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(c, "Trailer button pressed: "+course.id ,
                            Toast.LENGTH_LONG).show();
                    Intent k = new Intent(c,MovieTrailer.class);
                    k.putExtra("idTrailer",course.id);
                    k.putExtra("nameTrailer",course.name);
                    k.putExtra("movieTitle",course.name);



                    startActivity(k);

                }

            });


        }
        else {
            ((TextView) rootView.findViewById(R.id.title)).setText("No data internet down");
        }

        return rootView;
    }
}
