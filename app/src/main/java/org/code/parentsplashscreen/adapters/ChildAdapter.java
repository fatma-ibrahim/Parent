package org.code.parentsplashscreen.adapters;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.code.parentsplashscreen.models.Child;
import org.codeforiraq.parentsplashscreen.R;
import org.codeforiraq.parentsplashscreen.databinding.ChildItemViewBinding;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {
    private List<Child> childList;
    private LayoutInflater layoutInflater;
    private ChildClick childClick;

    public ChildAdapter(List<Child> childList, ChildClick childClick) {
        this.childList = childList;
        this.childClick = childClick;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        return new ChildViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.child_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        holder.bind(childList.get(position));
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        private ChildItemViewBinding childItemViewBinding;
        public ChildViewHolder(@NonNull ChildItemViewBinding childItemViewBinding) {
            super(childItemViewBinding.getRoot());
            this.childItemViewBinding = childItemViewBinding;
            childItemViewBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Child child = childList.get(getAdapterPosition());
                    childClick.onChildClickListener(child);
                }
            });
            /*childItemViewBinding.switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    childClick.onSwitcherListener(b,childList.get(getAdapterPosition()),getAdapterPosition());
                }
            });*/
            childItemViewBinding.switcher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    childClick.onSwitcherListener(childList.get(getAdapterPosition()), getAdapterPosition());

                }
            });
        }
        void bind(Child child){
            childItemViewBinding.setChild(child);
        }
    }
    public interface ChildClick{
        void onChildClickListener(Child child);

        void onSwitcherListener(Child child, int position);
    }
}
