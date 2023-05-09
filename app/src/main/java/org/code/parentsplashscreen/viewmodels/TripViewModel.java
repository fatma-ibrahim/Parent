package org.code.parentsplashscreen.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.code.parentsplashscreen.repository.DriverRepository;
import org.code.parentsplashscreen.repository.TripRepository;
import org.code.parentsplashscreen.responses.DriverInfoResponse;
import org.code.parentsplashscreen.responses.ShowTripResponse;

public class TripViewModel extends ViewModel {
    private TripRepository tripRepository;

    public TripViewModel() {
        tripRepository = new TripRepository();
    }

    public LiveData<ShowTripResponse> showTrip(String token) {
        return tripRepository.showTrip(token);
    }
}
