package test.ludovick.com.hamsters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

import test.ludovick.com.hamsters.pojo.Hamster;

public class HamsterFragment extends Fragment {

    private Hamster hamster;
    private ShareActionProvider mShareActionProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        hamster = bundle.getParcelable(MainActivity.HAMSTER_KEY);

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Hamster World");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hamster_full, container, false);
        ImageView photo = view.findViewById(R.id.hamster_full_photo);
        TextView name = view.findViewById(R.id.hamster_full_name);
        TextView description = view.findViewById(R.id.hamster_full_description);

        Picasso.get().load(hamster.getImage()).error(R.drawable.empty_photo).into(photo);
        name.setText(hamster.getTitle());
        description.setText(hamster.getDescription());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_menu, menu);

        MenuItem item = menu.findItem(R.id.share_item);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(doShare());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
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

    private Intent doShare(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String body = hamster.getImage() + "\n"
                + hamster.getTitle() + "\n"
                + hamster.getDescription();
        String subject = hamster.getTitle();
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        return intent;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
