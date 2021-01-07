package com.mehmetfatih.roomdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends Activity implements DepartmentFragment.OnDepartmentListInteractionListener {

    boolean displayingDepartment = false;
    Department selectedDepartment;
    ArrayList<Department> departments;
    UniversityDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(this,UniversityDB.class,"university").build();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                departments = retrieveDepartments();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.add(R.id.container, DepartmentFragment.newInstance(departments), "departments");
                        ft.commit();
                    }
                });
            }
        });
        thread.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("onOptionsItemSelected", item.getTitle().toString());

        switch (item.getItemId()){
            case R.id.action_new:
                displayingDepartment = !displayingDepartment;
                invalidateOptionsMenu();
                selectedDepartment = new Department();
                departments.add(selectedDepartment);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, EditFragment.newInstance(selectedDepartment, new ArrayList<Course>()), "edit_department");
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.action_save:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("onPrpOptMn new visible", menu.findItem(R.id.action_new).isVisible() + "");
        menu.findItem(R.id.action_new).setVisible(!displayingDepartment);
        menu.findItem(R.id.action_save).setVisible(displayingDepartment);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        displayingDepartment = !displayingDepartment;
        invalidateOptionsMenu();
        EditFragment editFragment = (EditFragment) getFragmentManager().findFragmentByTag("edit_department");
        if (editFragment != null) {
            final Department department = editFragment.getDepartment();
            final ArrayList<Course> courses = editFragment.getCourses();
            new Thread() {
                public void run() {
                    saveDepartment(department, courses);
                    departments = retrieveDepartments();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DepartmentFragment departmentFragment = (DepartmentFragment) getFragmentManager().findFragmentByTag("departments");
                            departmentFragment.setDepartments(departments);
                        }
                    });
                }
            }.start();
        }
        super.onBackPressed();
    }



    private void saveDepartment(Department department, ArrayList<Course> courses) {

        UniversityDAO dao = db.getDao();
        long deptId = dao.insertDepartment(department);
        for (Course course : courses) {
            course.deptId = deptId;
        }
        dao.insertCourses(courses.toArray(new Course[0]));

    }

    private ArrayList<Department> retrieveDepartments() {
        UniversityDAO dao = db.getDao();
        return new ArrayList<>(dao.getAllDepartments());
    }

    private ArrayList<Course> getCourses(Department department) {
        UniversityDAO dao = db.getDao();
        return new ArrayList<>(dao.getCourses(department.id));
    }

    @Override
    public void onDepartmentSelected(Department department) {
        selectedDepartment = department;
        new Thread() {
            public void run() {
                final ArrayList<Course> courses = getCourses(selectedDepartment);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container, EditFragment.newInstance(selectedDepartment, courses), "edit_department");
                        ft.addToBackStack(null);
                        ft.commit();
                        displayingDepartment = !displayingDepartment;
                        invalidateOptionsMenu();
                    }
                });
            }
        }.start();
    }
}