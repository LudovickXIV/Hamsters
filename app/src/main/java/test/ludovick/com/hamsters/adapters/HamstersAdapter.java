package test.ludovick.com.hamsters.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import test.ludovick.com.hamsters.interfaces.OnRecyclerClickListener;
import test.ludovick.com.hamsters.R;
import test.ludovick.com.hamsters.pojo.Hamster;

public class HamstersAdapter extends RecyclerView.Adapter<HamstersAdapter.mViewHolder> implements Filterable {

    interface OnPositionClick{
        void currentPosition(int pos);
    }

    private Context context;
    private List<Hamster> list;
    private List<Hamster> listFull;
    private OnRecyclerClickListener mListener;

    public HamstersAdapter(Context context, List<Hamster> list, OnRecyclerClickListener listener){
        this.context = context;
        this.list = list;
        this.listFull = new ArrayList<>(list);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hamster_list, viewGroup, false);
        mViewHolder vh = new mViewHolder(v, new OnPositionClick() {
            @Override
            public void currentPosition(int pos) {
                mListener.onHamsterInfoClick(list.get(pos));
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder mViewHolder, int position) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        Log.d("TAG", width + "");
        if (width < 700){
            mViewHolder.photo.getLayoutParams().height = 125;
            mViewHolder.photo.getLayoutParams().width = 125;
            mViewHolder.cardView.getLayoutParams().height = 125;
            mViewHolder.cardView.getLayoutParams().width = 125;
            mViewHolder.name.setTextSize(20);
            mViewHolder.description.setTextSize(15);
        }

        if (list.get(position).getImage() == null){
            Picasso.get()
                    .load(list.get(position).getImage())
                    .error(R.drawable.empty_photo)
                    .into(mViewHolder.photo);
        } else {
            Picasso.get()
                    .load(list.get(position).getImage())
                    .error(R.drawable.empty_photo)
                    .into(mViewHolder.photo);
        }



        mViewHolder.name.setText(list.get(position).getTitle());

        String description = list.get(position).getDescription();
        if (description.length() > 75) {
            if (description.charAt(74) == ' ') {
                description = description.substring(0, 75) + ". . .";
            } else {
                int index = 75;
                while (description.charAt(index) != ' ' && description.charAt(index) != '.'){
                    index++;
                }
                description = description.substring(0, index) + "...";
            }
        }
        mViewHolder.description.setText(description);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView photo;
        public TextView name;
        public TextView description;
        public CardView cardView;
        private OnPositionClick listener;

        public mViewHolder(@NonNull View itemView, OnPositionClick listener) {
            super(itemView);

            this.listener = listener;
            itemView.setOnClickListener(this);
            photo = itemView.findViewById(R.id.hamster_list_photo);
            name = itemView.findViewById(R.id.hamster_list_name);
            description = itemView.findViewById(R.id.hamster_list_description);
            cardView = itemView.findViewById(R.id.cardList);
        }

        @Override
        public void onClick(View v) {
            listener.currentPosition(getAdapterPosition());
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Hamster> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(listFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Hamster item : listFull) {
                        if (item.getTitle().toLowerCase().trim().contains(filterPattern) ||
                                item.getDescription().toLowerCase().trim().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list.clear();
                list.addAll((List)results.values);
                notifyDataSetChanged();
            }
        };
    }

}
