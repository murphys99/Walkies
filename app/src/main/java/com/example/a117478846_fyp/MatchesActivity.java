package com.example.a117478846_fyp;
//Adapted codefromthisvideohttps://www.youtube.com/watch?v=xIwQUq3yNYk&list=PLxabZQCAe5fio9dm1Vd0peIY6HLfo5MCf&index=14&ab_channel=SimCoder
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.os.Bundle;

        import java.util.ArrayList;
        import java.util.List;

        public class MatchesActivity extends AppCompatActivity{
        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mMatchesAdapter;
        private RecyclerView.LayoutManager mMatchesLayoutManager;

@Override
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager=new LinearLayoutManager(MatchesActivity.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter=new MatchesAdapter(getDataSetMatches(),MatchesActivity.this);
        mRecyclerView.setAdapter(mMatchesAdapter);

        for(int i=0;i<100;i++){
        MatchesObject obj=new MatchesObject(Integer.toString(i));
        resultsMatches.add(obj);
        }
        mMatchesAdapter.notifyDataSetChanged();

        }
        private ArrayList<MatchesObject>resultsMatches= new ArrayList<MatchesObject>();
        private List<MatchesObject> getDataSetMatches(){
        return resultsMatches;
        }
        }
