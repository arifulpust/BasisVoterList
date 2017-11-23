package com.ariful.basisvotarlistapp.interfaces;


import com.ariful.basisvotarlistapp.model.Person;

/**
 * Created by bipulkhan on 4/23/17.
 */

public interface CheckInListener {

    void userInfo(Person list, int value);
    void openPopUp(Person list);

}
