package korme.xyz.education.service.sortUtil;

import korme.xyz.education.model.MainPageModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListSortMPL implements ListSort{
    @Override
    public List<MainPageModel> mainPageListSort(List<MainPageModel> aList,
                                                       List<MainPageModel> bList) {
        int aLength = aList.size(), bLength = bList.size();
        List<MainPageModel> mergeList = new ArrayList();
        int i = 0, j = 0;
        while (aLength > i && bLength > j) {
            if (aList.get(i).getCreateTime().compareTo(bList.get(j).getCreateTime()) >=0) {
                mergeList.add(i + j, bList.get(j));
                j++;
            } else {
                mergeList.add(i + j, aList.get(i));
                i++;
            }
        }
        // blist元素已排好序， alist还有剩余元素
        while (aLength > i) {
            mergeList.add(i + j, aList.get(i));
            i++;
        }
        // alist元素已排好序， blist还有剩余元素
        while (bLength > j) {
            mergeList.add(i + j, bList.get(j));
            j++;
        }
        return mergeList;

    }

}
