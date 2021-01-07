package com.mehmetfatih.roomdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class EditFragment extends Fragment {

    private static final String ARG_DEPARTMENT = "department";
    private static final String ARG_COURSES = "courses";
    private Department department;
    private ArrayList<Course> courses;
    private EditText txtName;
    private RecyclerView recyclerView;
    private CourseAdapter mAdapter;
    Button btnNewCourse;

    public EditFragment() {

    }

    public static EditFragment newInstance(Department department, ArrayList<Course> courses) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DEPARTMENT, department);
        args.putSerializable(ARG_COURSES, courses);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            department = (Department) getArguments().getSerializable(ARG_DEPARTMENT);
            courses = (ArrayList<Course>) getArguments().getSerializable(ARG_COURSES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.courses);
        txtName = view.findViewById(R.id.name);
        btnNewCourse = view.findViewById(R.id.new_course);
        txtName.setText(department.name);
        mAdapter = new CourseAdapter(courses);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        btnNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditFragment.this.getActivity());
                builder.setTitle("New Course");

                final EditText txtCode = new EditText(EditFragment.this.getActivity());
                txtCode.setHint("Course Code");
                txtCode.setInputType(InputType.TYPE_CLASS_TEXT);

                final EditText txtName = new EditText(EditFragment.this.getActivity());
                txtName.setHint("Course Name");
                txtName.setInputType(InputType.TYPE_CLASS_TEXT);

                LinearLayout viewGroup = new LinearLayout(EditFragment.this.getActivity());
                viewGroup.setOrientation(LinearLayout.VERTICAL);
                viewGroup.addView(txtCode);
                viewGroup.addView(txtName);

                builder.setView(viewGroup);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String code = txtCode.getText().toString();
                        Log.d("Dialog", code);
                        String name = txtName.getText().toString();
                        Log.d("Dialog", name);
                        Course course = new Course();
                        course.code = code;
                        course.name = name;
                        addCourse(course);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

    }

    private void addCourse(Course course) {

        courses.add(course);
        mAdapter.notifyDataSetChanged();

    }

    public Department getDepartment(){

        department.name = txtName.getText().toString();
        return department;

    }

    public ArrayList<Course> getCourses(){return courses;}

}