package com.example.myfitnessapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.myfitnessapp.Adapters.SearchPeopleAdapter;
import com.example.myfitnessapp.Classes.SearchPeopleModel;
import com.example.myfitnessapp.R;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchPeopleActivity extends AppCompatActivity {

    private static final String TAG = "TAGSPA";
    FirebaseFirestore fStore;

    EditText editTextSearchBar;
    RecyclerView recyclerViewSearchPeople;
    SearchPeopleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_people);

        fStore = FirebaseFirestore.getInstance();

        editTextSearchBar = findViewById(R.id.etSearchBar);
        recyclerViewSearchPeople = findViewById(R.id.rvSearchPeople);

        Query query = fStore.collection("ToSearch");
        getDataFromFireStore(query);
        editTextSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(editTextSearchBar.getText().toString().isEmpty()){
                    Query query = fStore.collection("ToSearch");
                    getDataFromFireStore(query);
                }
                else{
                    String enteredUserName_smallLetters = s.toString().toLowerCase();

                    Query query = fStore.collection("ToSearch").orderBy("username").startAt(enteredUserName_smallLetters).endAt(enteredUserName_smallLetters + "\uf8ff");
                    getDataFromFireStore(query);
                }
            }
        });

    }

    private void getDataFromFireStore(Query query) {

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(8)
                .build();

        FirestorePagingOptions<SearchPeopleModel> options = new FirestorePagingOptions.Builder<SearchPeopleModel>()
                .setLifecycleOwner(SearchPeopleActivity.this)
                .setQuery(query,config,SearchPeopleModel.class)
                .build();

        adapter = new SearchPeopleAdapter(options);
        recyclerViewSearchPeople.setHasFixedSize(true);
        recyclerViewSearchPeople.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewSearchPeople.setAdapter(adapter);
    }
}
