package com.example.a117478846_fyp;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.example.a117478846_fyp.Chat.ChatObject;
        import com.example.a117478846_fyp.Chat.ChatViewHolders;

        import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders> {
        private List<ChatObject> chatList;
        private Context context;

        public ChatAdapter(List<ChatObject> chatList, Context context) {
                this.chatList = chatList;
                this.context = context;
        }


        @Override
        public ChatViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);
                RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutView.setLayoutParams(lp);
                ChatViewHolders rcv = new ChatViewHolders((layoutView));


                return rcv;

        }

        @Override
        public void onBindViewHolder(ChatViewHolders holder, int position) {

        }

        @Override
        public int getItemCount() { return this.chatList.size();

        }
}