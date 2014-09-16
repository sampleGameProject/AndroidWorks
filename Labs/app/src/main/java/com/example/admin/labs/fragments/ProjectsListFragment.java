package com.example.admin.labs.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.admin.labs.MainActivity;
import com.example.admin.labs.ProjectActivity;
import com.example.admin.labs.R;
import com.example.admin.labs.models.IRepoListener;
import com.example.admin.labs.models.ProjectsRepo;
import com.example.admin.labs.models.sql.data_models.Project;

import java.util.ArrayList;
import java.util.Date;
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
public class ProjectsListFragment extends Fragment implements IRepoListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ListView listView;
    private OnFragmentInteractionListener mListener;
    private ProjectsRepo projectsRepo;
    private Project currentProject;

    private static final int MENU_EDIT = 0;
    private static final int MENU_REMOVE = 1;


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
        projectsRepo = new ProjectsRepo(getActivity());
        projectsRepo.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_projects_list, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                Intent intent = new Intent(getActivity(), ProjectActivity.class);
                startActivity(intent);
            }
        });

        Button newProjectButton = (Button) view.findViewById(R.id.buttonAddProject);
        newProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProjectDialog(null);
            }
        });

        projectsRepo.setListener(this);
        return view;
    }

    private void setupListAdapter(){

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), projectsRepo.getProjectsArrayList(), R.layout.project_list_item,
                new String[] {"Name", "About"},
                new int[] { R.id.project_name, R.id.project_about});

        listView.setAdapter(adapter);
        registerForContextMenu(listView);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            currentProject = projectsRepo.getByIndex(info.position);
            menu.setHeaderTitle(currentProject.getName());
            menu.add(Menu.NONE, MENU_EDIT, MENU_EDIT, "Редактировать");
            menu.add(Menu.NONE, MENU_REMOVE, MENU_REMOVE, "Удалить");
        }
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item){
        switch (item.getItemId()) {
            case MENU_EDIT:
                showProjectDialog(currentProject);
                return true;
            case MENU_REMOVE:
                projectsRepo.delete(currentProject);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showProjectDialog(final Project project){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.project_view);

        final EditText name = (EditText) dialog.findViewById(R.id.editName);
        final EditText about = (EditText) dialog.findViewById(R.id.editAbout);
        final EditText start = (EditText) dialog.findViewById(R.id.editStart);
        final EditText deadline = (EditText) dialog.findViewById(R.id.editDeadline);
        final EditText finish = (EditText) dialog.findViewById(R.id.editFinish);

        final Button done = (Button) dialog.findViewById(R.id.buttonDone);

        if (project != null)
        {
            dialog.setTitle("Редактировать проект");
            name.setText(project.getName());
            about.setText(project.getAbout());
            done.setText("Сохранить");
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    project.setName(name.getText().toString());
                    project.setAbout(about.getText().toString());
                    projectsRepo.update(project);
                    //TODO: setup date values

                    dialog.cancel();
                }
            });
        }
        else
        {
            dialog.setTitle("Новый проект");
            done.setText("Добавить");
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Project newProject = new Project();
                    newProject.setName(name.getText().toString());
                    newProject.setAbout(about.getText().toString());

                    //TODO: replace to real input values
                    newProject.setStart(new Date());
                    newProject.setDeadline(new Date());
                    newProject.setFinish(new Date());

                    projectsRepo.add(newProject);

                    dialog.cancel();
                }
            });
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

    @Override
    public void onRepoChanged() {
        setupListAdapter();
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

    @Override
    public void onPause(){
        super.onPause();
        projectsRepo.close();
    }

    @Override
    public void onResume(){
        super.onResume();
        projectsRepo.open();
    }


}
