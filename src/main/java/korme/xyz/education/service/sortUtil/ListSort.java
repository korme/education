package korme.xyz.education.service.sortUtil;

import korme.xyz.education.model.MainPageModel;

import java.util.List;

public interface ListSort {
    List<MainPageModel> mainPageListSort(List<MainPageModel> aList, List<MainPageModel> bList);
}
