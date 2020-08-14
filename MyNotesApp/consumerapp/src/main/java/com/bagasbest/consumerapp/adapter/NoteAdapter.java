package com.bagasbest.consumerapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bagasbest.consumerapp.CustomOnClickListener;
import com.bagasbest.consumerapp.NoteAddUpdateActivity;
import com.bagasbest.consumerapp.R;
import com.bagasbest.consumerapp.entity.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> listNote = new ArrayList<>();
    private Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Note> getListNote() {
        return listNote;
    }

    public void setListNote (ArrayList<Note> listNote) {
        if(listNote.size() > 0) {
            this.listNote.clear();
        }
        this.listNote.addAll(listNote);

        notifyDataSetChanged();
    }

    public void addItem (Note note) {
        this.listNote.add(note);
        notifyItemInserted(listNote.size() - 1);
    }

    public  void updateItem (int position, Note note) {
        this.listNote.set(position, note);
        notifyItemChanged(position, note);
    }

    public void removeItem (int position) {
        this.listNote.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listNote.size());
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.tvTitle.setText(listNote.get(position).getTitle());
        holder.tvDate.setText(listNote.get(position).getDate());
        holder.tvDescription.setText(listNote.get(position).getDescription());

        holder.cvNote.setOnClickListener(new CustomOnClickListener(position, new CustomOnClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, NoteAddUpdateActivity.class);
                intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position);
                intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, listNote.get(position));
                activity.startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }

      class NoteViewHolder extends RecyclerView.ViewHolder {

       final TextView tvTitle, tvDescription, tvDate;
        final  CardView cvNote;

         NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_title);
             tvDescription = itemView.findViewById(R.id.tv_text_desc);
             tvDate = itemView.findViewById(R.id.tv_item_date);
            cvNote = itemView.findViewById(R.id.cv_item_note);
        }
    }
}
