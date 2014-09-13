package com.example.admin.labs.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.admin.labs.MainActivity;
import com.example.admin.labs.R;
import com.example.admin.labs.models.sql.data_models.Project;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ProjectsListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static ProjectsListFragment newInstance(int sectionNumber) {
        ProjectsListFragment fragment = new ProjectsListFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public ProjectsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_projects_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView);

        ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        map = new HashMap<String, String>();
        map.put("Name", "project1");
        map.put("About", "asbdargsrgsrgsrg");
        myArrList.add(map);

        map = new HashMap<String, String>();
        map.put("Name", "project2");
        map.put("About", "asbdargsrgadsrgsrg");
        myArrList.add(map);

        map = new HashMap<String, String>();
        map.put("Name", "project3");
        map.put("About", "asbdargsrgsrgsrg45");
        myArrList.add(map);

        map = new HashMap<String, String>();
        map.put("Name", "project3");
        map.put("About", "asbdargsrgsrgsrg45");
        myArrList.add(map);

        map = new HashMap<String, String>();
        map.put("Name", "project3");
        map.put("About", "asbdargsrgsrgsrg45");
        myArrList.add(map);

        map = new HashMap<String, String>();
        map.put("Name", "project3");
        map.put("About", "asbdargsrgsrgsrg45");
        myArrList.add(map);

        map = new HashMap<String, String>();
        map.put("Name", "project3");
        map.put("About", "asbdargsrgsrgsrg45");
        myArrList.add(map);

        map = new HashMap<String, String>();
        map.put("Name", "project3");
        map.put("About", "asbdargsrgsrgsrg45");
        myArrList.add(map);

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), myArrList, R.layout.project_list_item,
                new String[] {"Name", "About"},
                new int[] {R.id.project_name, R.id.project_about});

        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        Button newProjectButton = (Button) view.findViewById(R.id.buttonAddProject);
        newProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProjectDialog(null);
            }
        });

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
//        if (v.getId()==R.id.list) {
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
//            menu.setHeaderTitle(Countries[info.position]);
//            String[] menuItems = getResources().getStringArray(R.array.menu);
//            for (int i = 0; i<menuItems.length; i++) {
//                menu.add(Menu.NONE, i, i, menuItems[i]);
//            }
//        }
        showProjectDialog(null);
    }

    private void showProjectDialog(Project project){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.project_view);

        EditText name = (EditText) dialog.findViewById(R.id.editName);
        EditText about = (EditText) dialog.findViewById(R.id.editAbout);

        if (project != null)
        {
            dialog.setTitle("Редактировать проект");
            name.setText(project.getName());
            about.setText(project.getAbout());
        }
        else
        {
            dialog.setTitle("Новый проект");
        }

        dialog.show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
