package com.lofidewanto.demo.client.utils;

import com.lofidewanto.demo.client.ui.person.VPerson;
import com.vaadin.client.widgets.Grid;

import java.util.function.Function;

/**
 * Created by ahumphr on 25/01/17.
 */
public class ColumnFactory{

    public static Grid.Column<String, VPerson> createStringColumn(String caption, int maxsize, Function<VPerson, String> f) {
        Grid.Column<String, VPerson> col = new Grid.Column<String, VPerson>(caption) {
            @Override
            public String getValue(VPerson row) {
                return f.apply(row);
            }
        };
        col.setMaximumWidth(maxsize);
        col.setMinimumWidth(20);
        return col;
    }

    public static Grid.Column<Integer, VPerson> createIntColumn(String caption, int maxsize, Function<VPerson, Integer> f) {
        Grid.Column<Integer, VPerson> col = new Grid.Column<Integer, VPerson>(caption) {
            @Override
            public Integer getValue(VPerson row) {
                return f.apply(row);
            }
        };
        col.setMaximumWidth(maxsize);
        col.setMinimumWidth(20);
        return col;
    }




}
