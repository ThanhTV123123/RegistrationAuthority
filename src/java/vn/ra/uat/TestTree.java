/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.ra.object.BRANCH;
import vn.ra.process.ConnectDatabase;

/**
 *
 * @author USER
 */
public class TestTree {

    static ConnectDatabase db = new ConnectDatabase();

    public static void main(String[] args) throws Exception {
        JSONObject min = new JSONObject();
        min.put("week", "1");
        min.put("year", "2014");
        JSONObject max = new JSONObject();
        max.put("week", "14");
        max.put("year", "2017");
        JSONObject json = new JSONObject();
        json.put("min", min);
        json.put("max", max);
        JSONObject jsonss = new JSONObject();
        jsonss.put("json", json);
//        System.out.println(json1.toString());

        int intBranchID = 3;
        BRANCH[][] rsBranch;
        rsBranch = new BRANCH[1][];
        db.S_BO_BRANCH_GET_TREE_BRANCH(intBranchID, 1, rsBranch);
        List<Integer> list = new ArrayList<>();
        if (rsBranch[0].length > 0) {
            for (BRANCH branch : rsBranch[0]) {
                list.add(branch.PARENT_ID);
            }
            int iCountParent = findMax(list);
            JSONArray listJson = new JSONArray();
            if (iCountParent > 0) {
                for (int i = 1; i <= iCountParent; i++) {
                    for (BRANCH branch : rsBranch[0]) {
                        JSONObject json1 = new JSONObject();
                        if (branch.PARENT_ID == i) {
                            json1.put("text", "AAA" + iCountParent);
                            JSONObject json2 = new JSONObject();
                            json2.put("ID", branch.ID);
                            json2.put("NAME", branch.NAME);
                            json2.put("REMARK", branch.REMARK);
                            json1.put("children " + iCountParent, json2);
                        }
                        listJson.add(json1);
                    }
                }

//                JSONObject jsonx = new JSONObject();
//                jsonx.put("minx", minx);
                System.out.println(listJson.toString());
            }
        }
    }

    public static Integer findMax(List<Integer> list) {
        // check list is empty or not 
        if (list == null || list.size() == 0) {
            return Integer.MIN_VALUE;
        }
        // create a new list to avoid modification 
        // in the original list 
        List<Integer> sortedlist = new ArrayList<>(list);
        // sort list in natural order 
        Collections.sort(sortedlist);
        // last element in the sorted list would be maximum 
        return sortedlist.get(sortedlist.size() - 1);
    }
}
