package org.code.parentsplashscreen.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.code.parentsplashscreen.repository.ChildRepository;
import org.code.parentsplashscreen.responses.ChildResponse;
import org.code.parentsplashscreen.responses.ChildrensResponse;
import org.code.parentsplashscreen.responses.UpdateChildStatusResponse;

public class ChildViewModel extends ViewModel {
    private ChildRepository childRepository;

    public ChildViewModel() {
        childRepository = new ChildRepository();
    }

    public LiveData<ChildResponse> createChild(String childName, int childAge, String childGender, String childClass, String image_path, String token) {
        return childRepository.createChild(childName, childAge, childGender, childClass, image_path, token);
    }

    public LiveData<ChildrensResponse> getAllChildren(String token) {
        return childRepository.getAllChildren(token);
    }

    public LiveData<UpdateChildStatusResponse> updateChildStatus(int id, String token) {
        return childRepository.updateChildStatus(id, token);
    }

    public LiveData<ChildResponse> updateChild(int id, String childName, int childAge, String childGender, String childClass, String image_path, String token) {
        return childRepository.updateChild(id, childName, childAge, childGender, childClass, image_path, token);
    }

    public LiveData<ChildResponse> deleteChild(int id, String token) {
        return childRepository.deleteChild(id, token);
    }

}
