package com.bagasbest.consumerapp;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bagasbest.consumerapp.adapter.NoteAdapter;
import com.bagasbest.consumerapp.db.DatabaseContract;
import com.bagasbest.consumerapp.entity.Note;
import com.bagasbest.consumerapp.helper.MappingHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoadNotesCallback{

    private ProgressBar progressBar;
    private RecyclerView rvNotes;
    private NoteAdapter noteAdapter;
    private FloatingActionButton fabAdd;


    private static final String EXTRA_STATE = "extra_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Consumer Notes");
        }

        progressBar = findViewById(R.id.progressbar);
        rvNotes = findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);
        noteAdapter = new NoteAdapter(this);
        rvNotes.setAdapter(noteAdapter);

        fabAdd = findViewById(R.id.fab_add);



        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteAddUpdateActivity.class);
                startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD);
            }
        });

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(DatabaseContract.NoteColumns.CONTENT_URI, true, myObserver);

        //proses ambil data
        if(savedInstanceState == null) {
            new LoadNotesAsync(this, this).execute();
        }else {
            ArrayList<Note> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if(list != null) {
                noteAdapter.setListNote(list);
            }
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, noteAdapter.getListNote());
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Note> notes) {
        progressBar.setVisibility(View.INVISIBLE);
        if(notes.size() > 0) {
            noteAdapter.setListNote(notes);
        } else {
            noteAdapter.setListNote(new ArrayList<Note>());
            showSnackBarMessage("Tidak ada data saat ini!");
        }
    }

    private static class LoadNotesAsync extends AsyncTask <Void, Void, ArrayList<Note>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallback;

        private  LoadNotesAsync (Context context, LoadNotesCallback callabck) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callabck);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dateCursor = context.getContentResolver().query(
                    DatabaseContract
                    .NoteColumns
                    .CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            assert dateCursor != null;
            return MappingHelper.mapCursorToArrayList(dateCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);

            weakCallback.get().postExecute(notes);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            //akan dipanggil jika request codenya add
            if(requestCode == NoteAddUpdateActivity.REQUEST_ADD) {
                if(resultCode == NoteAddUpdateActivity.RESULT_ADD) {
                    Note note = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);

                    noteAdapter.addItem(note);
                    rvNotes.smoothScrollToPosition(noteAdapter.getItemCount() - 1);

                    showSnackBarMessage("Satu item berhasil ditambahkan");
                }
            }

            // Update dan Delete memiliki request code sama akan tetapi ewsult codenya berbeda
            else if (requestCode == NoteAddUpdateActivity.REQUEST_UPDATE) {
                if(resultCode == NoteAddUpdateActivity.RESULT_UPDATE) {

                    Note note = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);
                    int position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0);

                    noteAdapter.updateItem(position, note);
                    rvNotes.smoothScrollToPosition(position);

                    showSnackBarMessage("Satu item berhasil diubah");

                } else if(resultCode == NoteAddUpdateActivity.RESULT_DELETE) {

                    int position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0);

                    noteAdapter.removeItem(position);

                    showSnackBarMessage("Satu item berhasil dihapus");
                }
            }
        }

    }

    private void showSnackBarMessage(String output) {
        Snackbar.make(rvNotes, output, Snackbar.LENGTH_SHORT).show();
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadNotesAsync(context, (LoadNotesCallback) context).execute();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

interface LoadNotesCallback {
    void preExecute();
    void postExecute(ArrayList<Note> notes);
}
