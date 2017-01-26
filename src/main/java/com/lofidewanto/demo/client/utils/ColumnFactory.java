package com.lofidewanto.demo.client.utils;

import com.google.gwt.i18n.client.NumberFormat;
import com.lofidewanto.demo.client.ui.person.VPerson;
import com.vaadin.client.renderers.NumberRenderer;
import com.vaadin.client.widget.grid.CellReference;
import com.vaadin.client.widget.grid.CellStyleGenerator;
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
        col.setSortable(true);
        return col;
    }

    public static Grid.Column<Number, VPerson> createNumberColumn(String caption, int maxsize, String numFormat, Function<VPerson, Integer> f) {
        String format = numFormat != null && !numFormat.isEmpty() ? numFormat : "#";
        final NumberRenderer numberRenderer =
                new NumberRenderer(NumberFormat.getFormat(format));
        Grid.Column<Number, VPerson> col = new Grid.Column<Number, VPerson>(caption, numberRenderer) {
            @Override
            public Number getValue(VPerson row) {
                return f.apply(row);
            }
        };
        col.setMaximumWidth(maxsize);
        col.setMinimumWidth(20);
        col.setSortable(true);
        col.setResizable(true);
        return col;
    }

    public static Grid.Column<Boolean, VPerson> createBooleanColumn(String caption, int maxsize, Function<VPerson, Boolean> f) {

        final Grid.Column<Boolean, VPerson> col = new Grid.Column<Boolean, VPerson>(caption) {
            @Override
            public Boolean getValue(VPerson row) {
                return f.apply(row);
            }
        };
        col.setMaximumWidth(maxsize);
        col.setMinimumWidth(20);
        col.setSortable(true);
        return col;
    }


}
