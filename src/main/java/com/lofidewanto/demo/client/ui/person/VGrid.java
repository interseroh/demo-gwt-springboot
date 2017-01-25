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
        for (int i = 0; i < 2; i++) {
            new VPerson(1+(i*10),"John", 12);
            new VPerson(2+(i*10),"Emma", 18);
            new VPerson(3+(i*10),"Jeff", 44);
            new VPerson(4+(i*10),"George", 78);
            new VPerson(5+(i*10),"Abraham", 114);
            new VPerson(6+(i*10),"Henrik", 32);
            new VPerson(7+(i*10),"Paul", 56);
            new VPerson(8+(i*10),"Biff", 34);
            new VPerson(9+(i*10),"Leo", 88);
        }
        setDataSource(new ListDataSource<VPerson>(addons));
    }
}
