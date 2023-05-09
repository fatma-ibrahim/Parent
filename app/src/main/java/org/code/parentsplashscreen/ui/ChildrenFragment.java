package org.code.parentsplashscreen.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.code.parentsplashscreen.adapters.ChildAdapter;
import org.code.parentsplashscreen.dialogs.ChildDialogInfo;
import org.code.parentsplashscreen.models.Child;
import org.code.parentsplashscreen.responses.ChildResponse;
import org.code.parentsplashscreen.responses.ChildrensResponse;
import org.code.parentsplashscreen.responses.UpdateChildStatusResponse;
import org.code.parentsplashscreen.storage.SharedPreferencesManager;
import org.code.parentsplashscreen.viewmodels.ChildViewModel;
import org.codeforiraq.parentsplashscreen.R;
import org.codeforiraq.parentsplashscreen.databinding.FragmentChildrenBinding;

import java.util.ArrayList;
import java.util.List;

import stream.customalert.CustomAlertDialogue;


public class ChildrenFragment extends Fragment implements ChildDialogInfo.ChildAdded, ChildAdapter.ChildClick {
    private static final String TAG = "ChildrenFragment";
    private FragmentChildrenBinding childrenBinding;
    private ChildAdapter childAdapter;
    private ChildDialogInfo dialogInfo;
    private List<Child> childList;
    private ChildViewModel childViewModel;
    private String token;
    ProgressDialog progressDialog;

    public ChildrenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        childrenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_children, container, false);
        childViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ChildViewModel.class);
        token = SharedPreferencesManager.getInstance(getActivity()).getToken();
        // progress bar
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Adding Child Is Preparing");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        return childrenBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // when user click on floating action button
        try {
            childrenBinding.FAB.setOnClickListener(view1 -> {
                dialogInfo = new ChildDialogInfo(this);
                dialogInfo.show(getActivity().getSupportFragmentManager(), "dialog_created");
            });
        } catch (Exception e) {
            Log.d("hossam", e.getMessage().toString());
        }
        /** Getting All Children From Server **/
        getAllChildren();

        /** Recycler View **/
        childList = new ArrayList<>();
        childAdapter = new ChildAdapter(childList, this);
        childrenBinding.recyclerView.setAdapter(childAdapter);


        //DELETING NOTE BY SWIPE IT
        // adding here swipe to two directions , we can swipe it to only one direction
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                dialogDelete(viewHolder.getAdapterPosition());
            }
            // we should attach this to our recycler view object if we don't this won't work
        }).attachToRecyclerView(childrenBinding.recyclerView);


    }

    /**
     * Method That Return The Added Child
     **/
    @Override
    public void onChildAdded(Child child) {
        addChild(child);
    }

    /**
     * When Parent Want To Update Child
     **/
    @Override
    public void onChildUpdated(Child child) {
        updateChild(child);
    }

    /**
     * When User Click To Update Child
     **/
    @Override
    public void onChildClickListener(Child child) {
        dialogInfo = new ChildDialogInfo(this, child);
        dialogInfo.show(getActivity().getSupportFragmentManager(), "dialog_created");
    }

    /**
     * When User Switch The Status
     **/
    @Override
    public void onSwitcherListener(Child child, int position) {
        updateChildStatus(child);
    }


    /**
     * Add Child
     **/
    private void addChild(Child child) {
        dialogInfo.dismiss();
        progressDialog.show();
        childViewModel.createChild(child.getName(), child.getAge(), child.getGender(), child.getJsonMemberClass(), child.getImagePath(), "Bearer " + token).
                observe(getActivity(), new Observer<ChildResponse>() {
                    @Override
                    public void onChanged(ChildResponse childResponse) {
                        progressDialog.dismiss();
                        if (childResponse.isSuccess() && childResponse.getData() != null) {
                            Toast.makeText(getActivity(), "child added successfully", Toast.LENGTH_SHORT).show();
                            childList.add(child);
                            childAdapter.notifyDataSetChanged();
                        } else {
                            errorDialog(childResponse.getMessage());
                            Log.d(TAG, childResponse.getMessage());
                        }
                    }
                });
    }

    /**
     * Update Child Status
     **/
    private void updateChildStatus(Child child) {
        if (child.isConfirmed() == 0) {
            showDialog("Could Not Update Your Child " + child.getName() + " Status", "Please Contact One Of The Admins Of School To Confirm Your Child");
        } else {
            progressDialog.setTitle("Updating Child Status");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            childViewModel.updateChildStatus(child.getId(), "Bearer " + SharedPreferencesManager.getInstance(getActivity()).getToken()).observe(getActivity(), new Observer<UpdateChildStatusResponse>() {
                @Override
                public void onChanged(UpdateChildStatusResponse updateChildStatusResponse) {
                    if (updateChildStatusResponse != null) {
                        if (updateChildStatusResponse.isSuccess() && updateChildStatusResponse.getData() != null) {
                            if (updateChildStatusResponse.getData().isStatus()) {
                                showDialog("Status Updated Successfully", "Your Child " + updateChildStatusResponse.getData().getName() + " Is Going To School");
                            } else {
                                showDialog("Status Updated Successfully", "Your Child " + updateChildStatusResponse.getData().getName() + " Is Not Going To School");
                            }
                            progressDialog.dismiss();
                            Log.d(TAG, updateChildStatusResponse.getMessage());
                        } else {
                            showDialog("Cannot Update Child Status", updateChildStatusResponse.getMessage());
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    /**
     * Update Child
     **/
    private void updateChild(Child child) {
        dialogInfo.dismiss();
        progressDialog.setTitle("Updating Child");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        childViewModel.updateChild(child.getId(),
                child.getName(),
                child.getAge(),
                child.getGender(),
                child.getJsonMemberClass(),
                child.getImagePath(), "Bearer " + token).observe(getActivity(), new Observer<ChildResponse>() {
            @Override
            public void onChanged(ChildResponse childResponse) {
                if (childResponse != null) {
                    if (childResponse.isSuccess() && childResponse.getData() != null) {
                        progressDialog.dismiss();
                        showDialog("Child Updated Successfully", "Your Child " + child.getName() + " Has Been Updated Successfully");
                        getAllChildren();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), childResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Child Did Not Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * Get All Data Of Children
     **/
    private void getAllChildren() {
        childrenBinding.progressCircular.setVisibility(View.VISIBLE);
        childViewModel.getAllChildren("Bearer " + SharedPreferencesManager.getInstance(getActivity()).getToken()).observe(getActivity(),
                new Observer<ChildrensResponse>() {
                    @Override
                    public void onChanged(ChildrensResponse childrensResponse) {
                        childrenBinding.progressCircular.setVisibility(View.GONE);
                        if (childrensResponse != null) {
                            if (childrensResponse.isSuccess() && childrensResponse.getData() != null) {
                                try {
                                    Toast.makeText(getActivity(), childrensResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    if (childList != null) {
                                        LayoutAnimationController animationController =
                                                AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
                                        childrenBinding.recyclerView.setLayoutAnimation(animationController);
                                        childList.clear();
                                        childList.addAll(childrensResponse.getData());
                                        childAdapter.notifyDataSetChanged();
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, e.getMessage());
                                }
                            } else {
                                Toast.makeText(getActivity(), childrensResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    private void showDialog(String title, String message) {
        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(getActivity())
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setTitle(title)
                .setMessage(message)
                .setNegativeText("OK")
                .setNegativeColor(R.color.positive)
                .setNegativeTypeface(Typeface.DEFAULT_BOLD)
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setDecorView(getActivity().getWindow().getDecorView())
                .build();
        alert.show();
    }

    private void errorDialog(String errorMessage) {
        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(getActivity())
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setTitle("Could Not Add Your Child")
                .setMessage(errorMessage)
                .setNegativeText("OK")
                .setNegativeColor(R.color.negative)
                .setNegativeTypeface(Typeface.DEFAULT_BOLD)
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setDecorView(getActivity().getWindow().getDecorView())
                .build();
        alert.show();
    }

    private void dialogDelete(int position) {
        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(getActivity())
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setCancelable(false)
                .setTitle("Delete Child")
                .setMessage("Do You Want To Delete Your Child?")
                .setPositiveText("Yes")
                .setPositiveColor(R.color.positive)
                .setPositiveTypeface(Typeface.DEFAULT_BOLD)
                .setOnPositiveClicked(new CustomAlertDialogue.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        progressDialog.setTitle("Deleting Child");
                        progressDialog.setMessage("Please Wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        childViewModel
                                .deleteChild(childList.get(position).getId(),
                                        "Bearer " + token).observe(getActivity(), new Observer<ChildResponse>() {
                            @Override
                            public void onChanged(ChildResponse childResponse) {
                                if (childResponse != null) {
                                    if (childResponse.isSuccess() && childResponse.getData() != null) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), childResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        childList.remove(position);
                                        childAdapter.notifyDataSetChanged();
                                    } else {
                                        progressDialog.dismiss();
                                        childAdapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), "Child Did Not Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    childAdapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Child Did Not Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeText("No")
                .setNegativeColor(R.color.negative)
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        childAdapter.notifyDataSetChanged();
                    }
                })
                .setDecorView(getActivity().getWindow().getDecorView())
                .build();
        alert.show();
    }

    ;

}
