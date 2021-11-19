package com.example.rmanager.ui.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rmanager.adapters.RecyclerViewRecordsAdapter;
import com.example.rmanager.classes.Recording;
import com.example.rmanager.R;
import com.example.rmanager.player.DialogPlayer;
//import com.example.rmanager.save.DBManager;
import com.example.rmanager.utilities.DeleteUtil;
import com.example.rmanager.utilities.RecordUtil;
import com.example.rmanager.utilities.SavedUtil;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements RecyclerViewRecordsAdapter.ItemClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private int tab;

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;

    private PageViewModel pageViewModel;
    private RecyclerViewRecordsAdapter recyclerViewRecordsAdapter;

    private boolean flag_IsSelectMode = false;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        pageViewModel = ViewModelProviders.of(getActivity()).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        tab = index;
        if (index == 1)
            setupInitialization();

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        swipeContainer = (SwipeRefreshLayout) root.findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);

        swipeContainerSetup();
        recyclerViewSetup();
        observerSetup();

        return root;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.delete).setVisible(flag_IsSelectMode);
        menu.findItem(R.id.share).setVisible(flag_IsSelectMode);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.delete:
                delete();
                return true;
            case R.id.share:
                share();
                return true;
            case R.id.settings:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //----------------------------------------------------------------------------------

    //todo needs fixen when new recording added (maybe call all ints & setups)
    public void swipeContainerSetup(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerViewRecordsAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    //----------------------------------------------------------------------------------

    public void recyclerViewSetup(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRecordsAdapter = new RecyclerViewRecordsAdapter(getContext(), pageViewModel.getRecordings(tab).getValue(), pageViewModel.getSelectedRecordings(tab).getValue());
        recyclerViewRecordsAdapter.setSaveClickListener(PlaceholderFragment.this);
        recyclerViewRecordsAdapter.setClickListener(PlaceholderFragment.this);
        recyclerViewRecordsAdapter.setLongClickListener(PlaceholderFragment.this);
        recyclerView.setAdapter(recyclerViewRecordsAdapter);
    }//todo addOnScrollListener extra** https://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview

    public void observerSetup(){
        pageViewModel.getRecordings(tab).observe(getViewLifecycleOwner(), new Observer<ArrayList<Recording>>() {
            @Override
            public void onChanged(ArrayList<Recording> recordings) {
                recyclerViewRecordsAdapter.notifyDataSetChanged();
            }
        });
        pageViewModel.getSelectedRecordings(tab).observe(getViewLifecycleOwner(), new Observer<ArrayList<Recording>>() {
            @Override
            public void onChanged(ArrayList<Recording> recordings) {
                recyclerViewRecordsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSaveClick(View view, int position) {
        final Recording recording = recyclerViewRecordsAdapter.getRecord(position);
        saveFunc(recording);
    }

    @Override
    public void onItemClick(View view, int position) {
        Recording recording = recyclerViewRecordsAdapter.getRecord(position);
        if (flag_IsSelectMode){
            if (!((MaterialCardView)view).isChecked())
                addToSelectedList(recording);
            else
                removeFromSelectedList(recording);
        }
        else {// TODO fix when moving
            DialogPlayer dialogPlayer = new DialogPlayer(getActivity(), recording);
            dialogPlayer.show();
//            Intent intent = new Intent(); // intent to external app
//            intent.setAction(android.content.Intent.ACTION_VIEW);
//            File file = new File(recording.get_path());
//            Uri uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
//            intent.setDataAndType(uri, "audio/*");
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Recording recording = recyclerViewRecordsAdapter.getRecord(position);
        if (!flag_IsSelectMode){
            addToSelectedList(recording);
            menuSwitch();
        }
        else {
            if (((MaterialCardView)view).isChecked()){
                DialogPlayer dialogPlayer = new DialogPlayer(getActivity(), recording);
                dialogPlayer.show();
//                Intent intent = new Intent(); // TODO intent to external app
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//                File file = new File(recording.get_path());
//                Uri uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
//                intent.setDataAndType(uri, "audio/*");
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
            }
        }
    }

    //----------------------------------------------------------------------------------

    public void setupInitialization(){
        ArrayList<Recording> savedRecordings = RecordUtil.getRecordings(getContext(),"SavedCall");
        ArrayList<Recording> recordings = RecordUtil.getRecordings(getContext(),"Call");
        pageViewModel.init(recordings,savedRecordings);
    }

    public void menuSwitch(){
        if (flag_IsSelectMode)
            flag_IsSelectMode = false;
        else
            flag_IsSelectMode = true;
        getActivity().invalidateOptionsMenu();
    }

    //----------------------------------------------------------------------------------

    public void addToSelectedList(Recording recording){
        if (tab == 1)
            pageViewModel.addSAllRecords(recording);
        else
            pageViewModel.addSSavedRecords(recording);
    }

    public void removeFromSelectedList(Recording recording){
        if (tab == 1){
            pageViewModel.removeSAllRecords(recording);
            if (pageViewModel.IsEmptySAllRecords()){
                menuSwitch();
            }
        }
        else{
            pageViewModel.removeSSavedRecords(recording);
            if (pageViewModel.IsEmptySSavedRecords()){
                menuSwitch();
            }
        }
    }

    public void removeDeletedfromList(ArrayList<Recording> deletedRecordings){
        for (Recording deletedRecording :
                deletedRecordings) {
            pageViewModel.removeRecordings(deletedRecording);
        }
    }

    public void delete(){
        final ArrayList<Recording> DRecordings = pageViewModel.getSelectedRecordings(tab).getValue();
        new AlertDialog.Builder(getContext())
                .setTitle("Delete")
                .setMessage("Do you want to delete the (" + DRecordings.size() + ") Recordings?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DeleteUtil deleteUtil = new DeleteUtil();
                        deleteUtil.deleteAllSelectedRecordings(getContext(),DRecordings);
                        removeDeletedfromList(DRecordings);
                        if (tab == 1)
                            pageViewModel.clearSAllRecords();
                        else
                            pageViewModel.clearSSavedRecords();
                        menuSwitch();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();
    }


    public void share(){
        ArrayList<Recording> SRecordings = pageViewModel.getSelectedRecordings(tab).getValue();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);

        ArrayList<Uri> files = new ArrayList<Uri>();

        for(Recording recording : SRecordings) {
            File file = new File(recording.get_path());
            Uri uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
            files.add(uri);
        }

        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setType("audio/mpeg");
        startActivityForResult(sendIntent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 1) {
            if (tab == 1)
                pageViewModel.clearSAllRecords();
            else
                pageViewModel.clearSSavedRecords();
            menuSwitch();
        }
    }

    //----------------------------------------------------------------------------------

    public void saveFunc(final Recording recording){
        if (!recording.is_saved()){
            new AlertDialog.Builder(getContext())
                    .setTitle("Save")
                    .setMessage("Do you want to Save")
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            ProgressDialog loading = getLoadingDialog("Saving");
                            loading.show();
                            if (SavedUtil.move(recording.get_file_name(),"Call", "SavedCall", getContext())) {
                                recording.set_saved(true);
                                pageViewModel.updateAllRecords(recording);
                                pageViewModel.updateSavedRecords(recording);
                                loading.dismiss();
                            }
                            else {
                                Toast.makeText(getContext(), "error moving", Toast.LENGTH_SHORT).show();
                            }
                        }

                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        else {
            new AlertDialog.Builder(getContext())
                    .setTitle("Remove Saved")
                    .setMessage("Do you want to remove saved")
                    .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            ProgressDialog loading = getLoadingDialog("Moving to all");
                            loading.show();
                            if (SavedUtil.move(recording.get_file_name(), "SavedCall", "Call", getContext())){
                                recording.set_saved(false);
                                pageViewModel.updateAllRecords(recording);
                                pageViewModel.updateSavedRecords(recording);
                                loading.dismiss();
                            }
                            else {
                                Toast.makeText(getContext(), "error moving", Toast.LENGTH_SHORT).show();
                            }
                        }

                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    public ProgressDialog getLoadingDialog(String title){
        ProgressDialog loading = new ProgressDialog(getContext());
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setTitle(title);
        loading.setMessage("Please wait...");
        loading.setIndeterminate(true);
        loading.setCanceledOnTouchOutside(false);
        return loading;
    }
}