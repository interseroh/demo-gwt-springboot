package com.lofidewanto.demo.client.ui.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.binder.GenericEvent;
import com.lofidewanto.demo.client.ui.person.PersonPanelView;
import org.gwtbootstrap3.extras.bootbox.client.Bootbox;

import java.lang.annotation.Annotation;
import java.util.logging.Logger;

/**
 * Created by alexadmin on 20.01.2017.
 */

public class PersonEventHandler implements EventHandler {
    private static Logger logger = Logger
            .getLogger(PersonPanelView.class.getName());

    public void onEvent(FilterEvent event){
        logger.info("Get Event:"+event);
        Bootbox.alert("FilterEvent is fired!!!");
    }


}
