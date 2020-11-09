package com.concordia.smarthomesimulator.adapters;

import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.interfaces.IInhabitant;

public class InhabitantAdapter implements IInhabitant {

    private final User user;

    public InhabitantAdapter(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public boolean isIntruder() {
        return false;
    }
}
