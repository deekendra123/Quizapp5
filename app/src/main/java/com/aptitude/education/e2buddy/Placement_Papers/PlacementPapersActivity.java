package com.aptitude.education.e2buddy.Placement_Papers;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.education.e2buddy.R;

import java.util.ArrayList;
import java.util.List;

public class PlacementPapersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPapers;
    List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_papers);
        recyclerViewPapers = findViewById(R.id.recyclerViewPapers);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewPapers.setLayoutManager(layoutManager);

        loadPapers();

    }

    private void loadPapers() {
        list = new ArrayList<>();
        list.add("Placement Paper 1");
        list.add("Placement Paper 2");
        list.add("Placement Paper 3");
        list.add("Placement Paper 4");
        list.add("Placement Paper 5");
        list.add("Placement Paper 6");
        list.add("Placement Paper 7");
        list.add("Placement Paper 8");

        PlacementPaperAdapter placementPaperAdapter = new PlacementPaperAdapter(this, list);
        recyclerViewPapers.setAdapter(placementPaperAdapter);
    }
}
