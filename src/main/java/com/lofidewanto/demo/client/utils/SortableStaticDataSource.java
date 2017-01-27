/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.lofidewanto.demo.client.utils;

import com.lofidewanto.demo.client.ui.person.VPerson;
import com.vaadin.shared.data.sort.SortDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.lofidewanto.demo.client.ui.person.PersonPanelView.Cols;

/**
 * Created by ahumphr on 25/01/17.
 */
public class SortableStaticDataSource {

    private static ArrayList<VPerson> generateData(Comparator<VPerson> c) {
        // Some dummy data
        ArrayList<VPerson> people = new ArrayList<VPerson>();
        for (int i = 0; i < 2; i++) {
            people.add(new VPerson(0+(i*10),"Kevin", 82));
            people.add(new VPerson(1+(i*10),"John", 12));
            people.add(new VPerson(2+(i*10),"Emma", 18));
            people.add(new VPerson(3+(i*10),"Jeff", 44));
            people.add(new VPerson(4+(i*10),"George", 78));
            people.add(new VPerson(5+(i*10),"Abraham", 114));
            people.add(new VPerson(6+(i*10),"Henrik", 32));
            people.add(new VPerson(7+(i*10),"Paul", 56));
            people.add(new VPerson(8+(i*10),"Biff", 34));
            people.add(new VPerson(9+(i*10),"Leo", 88));
        }
        people.sort(c);
        return people;
    }


    public static List<VPerson> getSortedData(final Cols colHeader, SortDirection direction) {
        List<VPerson> list = new ArrayList<>();
        switch (colHeader) {
            case ID:
                list = generateData((v1, v2) -> {
                    return v1.getId() - v2.getId();
                });
                break;
            case AGE:
                list = generateData((v1, v2) -> {
                    return v1.getAge() - v2.getAge();
                });
                break;
            case NAME:
                list = generateData((v1, v2) -> {
                    return v1.getName().compareTo(v2.getName());
                });
                break;
            case NICKNAME:
                list = generateData((v1, v2) -> {
                    return v1.getNickName().compareTo(v2.getNickName());
                });
                break;
            case RETIRED:
                list = generateData((v1, v2) -> {
                    return Boolean.compare(v1.isRetired(), v2.isRetired());
                });
                break;
        }
        if (SortDirection.DESCENDING == direction) {
            Collections.reverse(list);
        }
        return list;
    }

}
