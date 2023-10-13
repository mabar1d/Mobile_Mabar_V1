package com.circle.circle_games.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.circle.circle_games.R;
import com.circle.circle_games.retrofit.model.ChatMessageModel;
import com.circle.circle_games.utility.SessionUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatMessageModel> chatMessages;
    private Context context;
    private String username;

    public ChatAdapter(List<ChatMessageModel> chatMessages,Context context,String username) {
        this.chatMessages = chatMessages;
        this.context = context;
        this.username = username;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessageModel chatMessage = chatMessages.get(position);
        if (chatMessage.getUserName().equals(username)) {
            return 1; // Pesan yang Anda kirim
        } else {
            return 2; // Pesan dari pengguna lain
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_sent, parent, false);
            return new ChatViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_received, parent, false);
            return new ChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessageModel chatMessage = chatMessages.get(position);
        holder.bind(chatMessage);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMsg,tvUsername,tvDateTime;
        private LinearLayout msgLayout;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMsg = itemView.findViewById(R.id.tv_message);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            msgLayout = itemView.findViewById(R.id.message_layout);
        }

        void bind(ChatMessageModel chatMessage) {
            String timeOnly = "";


            try {
                // Original date string in the format "dd-MM HH:mm"
                String originalDateString = chatMessage.getDateTime();

                // Parse the original date string
                SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM HH:mm");
                Date date = originalFormat.parse(originalDateString);

                // Create a new SimpleDateFormat to format time as "HH:MM"
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                // Format the date to get time in "HH:MM" format
                timeOnly = timeFormat.format(date);

                System.out.println("Time (HH:MM): " + timeOnly);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            tvMsg.setText(chatMessage.getMessage());
            tvUsername.setText(chatMessage.getUserName());
            tvDateTime.setText(timeOnly);
            // Anda dapat menyesuaikan tampilan berdasarkan senderId atau aspek lainnya

            // Set the layout gravity based on sender (current user or others)
            /*if (chatMessage.getSentByMe()) {
                msgLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // Right-to-left for sent messages
            } else {
                msgLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // Left-to-right for received messages
            }*/
        }
    }
}

