package com.lofidewanto.demo.client.ui.event;


import com.google.gwt.event.shared.GwtEvent;


/**
 * Created by alexadmin on 20.01.2017.
 */
public class FilterEvent  extends GwtEvent<PersonEventHandler> {
    public static Type<PersonEventHandler> TYPE = new Type<PersonEventHandler>();


    public FilterEvent() {

    }

    @Override
    public Type<PersonEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PersonEventHandler personEventHandler) {
        personEventHandler.onEvent(this);

    }
}

