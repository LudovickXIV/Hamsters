package test.ludovick.com.hamsters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import test.ludovick.com.hamsters.adapters.HamstersAdapter;
import test.ludovick.com.hamsters.interfaces.OnHamsterDataPass;
import test.ludovick.com.hamsters.interfaces.OnRecyclerClickListener;
import test.ludovick.com.hamsters.pojo.Hamster;

public class HamstersListFragment extends Fragment{

    private List<Hamster> hamsters;
    private HamstersAdapter adapter;
    private OnHamsterDataPass callback;
    private SearchView searchView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnHamsterDataPass)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        hamsters = bundle.getParcelableArrayList(MainActivity.HAMSTER_KEY);
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Hamster World");
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hamster_list_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.mRecycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());

        adapter = new HamstersAdapter(getActivity(), hamsters, new OnRecyclerClickListener() {
            @Override
            public void onHamsterInfoClick(Hamster hamster) {
                searchView.setQuery("", false);
                searchView.clearFocus();
                callback.onHamsterPass(hamster);
            }
        });

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_item);
        searchView = (SearchView)searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(){
        final Dialog dialog = new Dialog(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dalog, null);
        CardView cardView = view.findViewById(R.id.cardView2);
        TextView name = view.findViewById(R.id.name_of_dev);
        TextView desc = view.findViewById(R.id.description_dev);
        final TextView email = view.findViewById(R.id.textView2);
        TextView skype = view.findViewById(R.id.textView);
        Button button = view.findViewById(R.id.contact_me);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email.getText().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hamster app");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Thank you for your incredible application!");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        if (width < 700){
            cardView.getLayoutParams().height = 125;
            cardView.getLayoutParams().width = 125;
            name.setTextSize(20);
            desc.setTextSize(15);
            email.setTextSize(20);
            skype.setTextSize(20);
        }
        dialog.setContentView(view);
        dialog.show();
    }
}
