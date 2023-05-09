package org.code.parentsplashscreen.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.code.parentsplashscreen.onBoarding.OnBoardingItem;
import org.codeforiraq.parentsplashscreen.R;
import org.codeforiraq.parentsplashscreen.databinding.OnboardingItemBinding;

import java.util.List;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder> {
    private List<OnBoardingItem> onBoardingItemList;
    private LayoutInflater layoutInflater;


    public OnBoardingAdapter(List<OnBoardingItem> onBoardingItemList) {
        this.onBoardingItemList = onBoardingItemList;
    }

    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        return new OnBoardingViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.onboarding_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {
        holder.onboardingItemBinding.textTitle.setText(onBoardingItemList.get(position).getTitle());
        holder.onboardingItemBinding.textDescription.setText(onBoardingItemList.get(position).getDescription());
        holder.onboardingItemBinding.imageOnboarding.setImageResource(onBoardingItemList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return onBoardingItemList.size();
    }

    public class OnBoardingViewHolder extends RecyclerView.ViewHolder {
        private OnboardingItemBinding onboardingItemBinding;

        public OnBoardingViewHolder(@NonNull OnboardingItemBinding onboardingItemBinding) {
            super(onboardingItemBinding.getRoot());
            this.onboardingItemBinding = onboardingItemBinding;
        }

    }
}
