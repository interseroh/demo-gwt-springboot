package com.lofidewanto.demo.client.ui.person;

import java.util.ArrayList;

import com.vaadin.client.widget.grid.datasources.ListDataSource;
import com.vaadin.client.widgets.Grid;

public class VGrid extends Grid<VPerson> {

    public VGrid() {
        super();
        setSelectionMode(SelectionMode.SINGLE);
        addColumn(new Column<String, VPerson>("Name") {
            @Override
            public String getValue(VPerson row) {
                return row.getName();
            }
        });

        // A simple String column for the add-on summary/description
        addColumn(new Column<Integer, VPerson>("Age") {
            @Override
            public Integer getValue(VPerson row) {
                return row.getAge();
            }
        });

        // Some dummy data
        ArrayList<VPerson> addons = new ArrayList<VPerson>();
        for (int i = 0; i < 10; i++) {
            addons.add(new VPerson("John", 12));
            addons.add(new VPerson("Emma", 18));
            addons.add(new VPerson("Jeff", 44));
            addons.add(new VPerson("George", 78));
            addons.add(new VPerson("Abraham", 114));
            addons.add(new VPerson("Henrik", 32));
            addons.add(new VPerson("Paul", 56));
            addons.add(new VPerson("Biff", 34));
            addons.add(new VPerson("Leo", 88));
        }
        setDataSource(new ListDataSource<VPerson>(addons));
    }
}
