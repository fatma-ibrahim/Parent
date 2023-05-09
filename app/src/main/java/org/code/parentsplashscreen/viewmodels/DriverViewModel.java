package org.code.parentsplashscreen.viewmodels;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import org.code.parentsplashscreen.repository.DriverRepository;
import org.code.parentsplashscreen.responses.DriverInfoResponse;

public class DriverViewModel extends ViewModel  {

    private DriverRepository driverRepository;
    public DriverViewModel() {
        driverRepository = new DriverRepository();
    }
    public LiveData<DriverInfoResponse> getDriverInfo(String token) {
        return driverRepository.getDriverInfo(token);
    }
}
