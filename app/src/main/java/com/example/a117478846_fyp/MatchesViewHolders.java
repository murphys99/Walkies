package com.example.a117478846_fyp;
//implementing a chat feature using https://www.youtube.com/watch?v=9dC4w04AuOs&list=PLxabZQCAe5fio9dm1Vd0peIY6HLfo5MCf&index=16&ab_channel=SimCoder
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.a117478846_fyp.Chat.ChatActivity;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mMatchId, mMatchName;
    public ImageView mMatchImage;
    public MatchesViewHolders(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = (TextView) itemView.findViewById(R.id.Matchid);
        mMatchName = (TextView) itemView.findViewById(R.id.MatchName);

        mMatchImage = (ImageView) itemView.findViewById(R.id.MatchImage);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        intent.putExtras(b);
        itemView.getContext().startActivity(intent);
    }
}
