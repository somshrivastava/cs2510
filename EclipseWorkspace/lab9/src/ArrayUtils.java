import tester.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

// represents the util functions of array lists
class ArrayUtils {

  ArrayUtils() {
  }

  // returns a new list containing only elements that pass the predicate
  <T> ArrayList<T> filter(ArrayList<T> array, Predicate<T> predicate) {
    return customFilter(array, predicate, true);
  }

  // returns a new list containing only elements that fail the predicate
  <T> ArrayList<T> filterNot(ArrayList<T> array, Predicate<T> predicate) {
    return customFilter(array, predicate, false);
  }

  // general filtering method based on boolean input
  <T> ArrayList<T> customFilter(ArrayList<T> array, Predicate<T> predicate, boolean shouldAdd) {
    ArrayList<T> result = new ArrayList<T>();
    for (T element : array) {
      if (predicate.test(element) == shouldAdd) {
        result.add(element);
      }
    }
    return result;
  }

  public void removeBetween(ArrayList<T> given, int a, int b) {
    if (b > a. && a > 0 && a < given.size() & b > 0 && b < given.size()) {
      return given;
    }
    for (int i = a; i <= b; b--) {
        given.remove(i);
    }
  }

  // removes elements that fail the predicate from the given list
  <T> void removeFailing(ArrayList<T> array, Predicate<T> predicate) {
    customRemove(array, predicate, false);
  }

  // removes elements that pass the predicate from the given list
  <T> void removePassing(ArrayList<T> array, Predicate<T> predicate) {
    customRemove(array, predicate, true);
  }

  // removes elements based on the boolean input
  <T> void customRemove(ArrayList<T> array, Predicate<T> predicate, boolean shouldRemove) {
    for (int i = array.size() - 1; i >= 0; i--) {
      if (predicate.test(array.get(i)) == shouldRemove) {
        array.remove(i);
      }
    }
  }

  // returns an combined list from two lists
  <T> ArrayList<T> interweave(ArrayList<T> arr1, ArrayList<T> arr2) {
    return customInterweave(arr1, arr2, 1, 1);
  }

  // returns an combined list using custom lengths
  <T> ArrayList<T> customInterweave(ArrayList<T> arr1, ArrayList<T> arr2, int getFrom1,
      int getFrom2) {
    ArrayList<T> result = new ArrayList<T>();
    for (int i = 0, j = 0; i < arr1.size() || j < arr2.size();) {
      for (int k = 0; k < getFrom1 && i < arr1.size(); k++, i++) {
        result.add(arr1.get(i));
      }
      for (int k = 0; k < getFrom2 && j < arr2.size(); k++, j++) {
        result.add(arr2.get(j));
      }
    }
    return result;
  }

  <T> ArrayList<T> removeBetween(ArrayList<T> given, int a, int b) {
    if (b >= a && a >= 0 && a < given.size() & b >= 0 && b < given.size()) {
      for (int i = a; i <= b; b--) {
        given.remove(i);
      }
      return given;
    }
    return given;
  }
}

// represents the examples and tests for array utils
class ExamplesArrayUtils {
  // array utils initialization
  ArrayUtils arrayUtils = new ArrayUtils();
  // an empty list
  ArrayList<Integer> emptyList;
  // list with even and odd numbers
  ArrayList<Integer> allNumbers;
  // list with odd numbers
  ArrayList<Integer> oddNumbers;
  // list with even numbers
  ArrayList<Integer> evenNumbers;
  // list that interweaves odd and even numbers with step size of 2
  ArrayList<Integer> interwovenNumbers;

  ArrayList<Integer> list1;
  ArrayList<Integer> list2;
  ArrayList<String> list3;
  ArrayList<String> result;

  // initializes the data
  public void initData() {
    emptyList = new ArrayList<Integer>();
    allNumbers = new ArrayList<Integer>();
    allNumbers.add(1);
    allNumbers.add(2);
    allNumbers.add(3);
    allNumbers.add(4);
    allNumbers.add(5);
    allNumbers.add(6);
    oddNumbers = new ArrayList<Integer>();
    oddNumbers.add(1);
    oddNumbers.add(3);
    oddNumbers.add(5);
    evenNumbers = new ArrayList<Integer>();
    evenNumbers.add(2);
    evenNumbers.add(4);
    evenNumbers.add(6);
    interwovenNumbers = new ArrayList<Integer>();
    interwovenNumbers.add(1);
    interwovenNumbers.add(3);
    interwovenNumbers.add(2);
    interwovenNumbers.add(4);
    interwovenNumbers.add(5);
    interwovenNumbers.add(6);
    list1 = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
    list2 = new ArrayList<>(Arrays.asList(10, 20, 30));
    list3 = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
    result = new ArrayList<>();
  }

  public void testRemoveBetween(Tester t) {
    initData();

    // removing from index 2 to 4
    ArrayList<Integer> case1 = new ArrayList<>(list1);
    arrayUtils.removeBetween(case1, 2, 4);
    t.checkExpect(case1, new ArrayList<>(Arrays.asList(0, 1, 5)));

    // removing from index 4 to 2 (reverse order)
    ArrayList<String> case2 = new ArrayList<>(list3);
    arrayUtils.removeBetween(case2, 4, 2);
    t.checkExpect(case2, new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e")));

    // removing a single element
    ArrayList<Integer> case3 = new ArrayList<>(list2);
    arrayUtils.removeBetween(case3, 1, 1);
    t.checkExpect(case3, new ArrayList<>(Arrays.asList(10, 30)));

    // invalid bounds (negative index)
    ArrayList<Integer> case4 = new ArrayList<>(list2);
    arrayUtils.removeBetween(case4, -1, 2);
    t.checkExpect(case4, list2);

    // invalid bounds (out of range)
    ArrayList<Integer> case5 = new ArrayList<>(list2);
    arrayUtils.removeBetween(case5, 0, 5);
    t.checkExpect(case5, list2);

    // remove entire list
    ArrayList<String> case6 = new ArrayList<>(list3);
    arrayUtils.removeBetween(case6, 0, 4);
    t.checkExpect(case6, result); // result is empty list
  }

  // tests the filter method
  public void testFilter(Tester t) {
    initData();
    // filtering an empty list
    t.checkExpect(arrayUtils.filter(new ArrayList<Integer>(), (i) -> i % 2 == 0), emptyList);
    // filtering a list with all numbers to have only even numbers
    t.checkExpect(arrayUtils.filter(allNumbers, (i) -> i % 2 == 0), evenNumbers);
    // filtering a list with even numbers to return the same list
    t.checkExpect(arrayUtils.filter(evenNumbers, (i) -> i % 2 == 0), evenNumbers);
    // filtering a list with odd numbers to return an empty list
    t.checkExpect(arrayUtils.filter(oddNumbers, (i) -> i % 2 == 0), emptyList);
  }

  // tests the filter not method
  public void testFilterNot(Tester t) {
    initData();
    // filtering an empty list
    t.checkExpect(arrayUtils.filterNot(new ArrayList<Integer>(), (i) -> i % 2 == 0), emptyList);
    // filtering a list with all numbers to have only odd numbers numbers
    t.checkExpect(arrayUtils.filterNot(allNumbers, (i) -> i % 2 == 0), oddNumbers);
    // filtering a list with even numbers to return an empty list
    t.checkExpect(arrayUtils.filterNot(evenNumbers, (i) -> i % 2 == 0), emptyList);
    // filtering a list with odd numbers to return the same list
    t.checkExpect(arrayUtils.filterNot(oddNumbers, (i) -> i % 2 == 0), oddNumbers);
  }

  // tests the customFilter method
  public void testCustomFilter(Tester t) {
    initData();
    // filtering an empty list
    t.checkExpect(arrayUtils.customFilter(new ArrayList<Integer>(), (i) -> i % 2 == 0, true),
        emptyList);
    // filtering a list with all numbers to have only even numbers
    t.checkExpect(arrayUtils.customFilter(allNumbers, (i) -> i % 2 == 0, true), evenNumbers);
    // filtering a list with even numbers to return the same list
    t.checkExpect(arrayUtils.customFilter(evenNumbers, (i) -> i % 2 == 0, true), evenNumbers);
    // filtering a list with odd numbers to return an empty list
    t.checkExpect(arrayUtils.customFilter(oddNumbers, (i) -> i % 2 == 0, true), emptyList);
    // filtering an empty list
    t.checkExpect(arrayUtils.customFilter(new ArrayList<Integer>(), (i) -> i % 2 == 0, false),
        emptyList);
    // filtering a list with all numbers to have only odd numbers numbers
    t.checkExpect(arrayUtils.customFilter(allNumbers, (i) -> i % 2 == 0, false), oddNumbers);
    // filtering a list with even numbers to return an empty list
    t.checkExpect(arrayUtils.customFilter(evenNumbers, (i) -> i % 2 == 0, false), emptyList);
    // filtering a list with odd numbers to return the same list
    t.checkExpect(arrayUtils.customFilter(oddNumbers, (i) -> i % 2 == 0, false), oddNumbers);
  }

  // tests the interweave method
  public void testInterweave(Tester t) {
    initData();
    // tests interweaving two empty lists
    t.checkExpect(arrayUtils.interweave(emptyList, emptyList), emptyList);
    // tests interweaving an empty list and non empty list
    t.checkExpect(arrayUtils.interweave(emptyList, allNumbers), allNumbers);
    // tests interweaving two non empty list
    t.checkExpect(arrayUtils.interweave(oddNumbers, evenNumbers), allNumbers);
  }

  // tests the customInterweave method
  public void testCustomInterweave(Tester t) {
    initData();
    // tests interweaving two empty lists with step size of 0
    t.checkExpect(arrayUtils.customInterweave(emptyList, emptyList, 0, 0), emptyList);
    // tests interweaving two empty lists with step size of 1
    t.checkExpect(arrayUtils.customInterweave(emptyList, emptyList, 1, 1), emptyList);
    // tests interweaving an empty list and non empty list
    t.checkExpect(arrayUtils.customInterweave(emptyList, allNumbers, 1, 1), allNumbers);
    // tests interweaving two non empty list with step size of 1
    t.checkExpect(arrayUtils.customInterweave(oddNumbers, evenNumbers, 1, 1), allNumbers);
    // tests interweaving two non empty list with step size of 2
    t.checkExpect(arrayUtils.customInterweave(oddNumbers, evenNumbers, 2, 2), interwovenNumbers);
  }

  // tests the removeFailing method
  public void testRemoveFailing(Tester t) {
    initData();
    // test for an empty list
    arrayUtils.removeFailing(emptyList, (i) -> true);
    t.checkExpect(emptyList, emptyList);
    // test for a list with all numbers to only have even numbers
    arrayUtils.removeFailing(allNumbers, (i) -> i % 2 == 0);
    t.checkExpect(allNumbers, evenNumbers);
    // test for a list with even numbers to only have even numbers
    arrayUtils.removeFailing(evenNumbers, (i) -> i % 2 == 0);
    t.checkExpect(evenNumbers, evenNumbers);
  }

  // tests the removePassing method
  public void testRemovePassing(Tester t) {
    initData();
    // test for an empty list
    arrayUtils.removePassing(emptyList, (i) -> false);
    t.checkExpect(emptyList, emptyList);
    // test for a list with all numbers to only have odd numbers
    arrayUtils.removePassing(allNumbers, (i) -> i % 2 == 0);
    t.checkExpect(allNumbers, oddNumbers);
    // test for a list with even numbers to have same list
    arrayUtils.removePassing(evenNumbers, (i) -> i % 2 != 0);
    t.checkExpect(evenNumbers, evenNumbers);
  }

  // tests the customRemove method
  public void testCustomRemove(Tester t) {
    initData();
    // test for an empty list
    arrayUtils.customRemove(emptyList, (i) -> true, false);
    t.checkExpect(emptyList, emptyList);
    // test for a list with all numbers to only have even numbers
    arrayUtils.customRemove(allNumbers, (i) -> i % 2 == 0, false);
    t.checkExpect(allNumbers, evenNumbers);
    // test for a list with even numbers to only have even numbers
    arrayUtils.customRemove(evenNumbers, (i) -> i % 2 == 0, false);
    t.checkExpect(evenNumbers, evenNumbers);

    initData();
    // test for an empty list
    arrayUtils.customRemove(emptyList, (i) -> false, true);
    t.checkExpect(emptyList, emptyList);
    // test for a list with all numbers to only have odd numbers
    arrayUtils.customRemove(allNumbers, (i) -> i % 2 == 0, true);
    t.checkExpect(allNumbers, oddNumbers);
    // test for a list with even numbers to have same list
    arrayUtils.customRemove(evenNumbers, (i) -> i % 2 != 0, true);
    t.checkExpect(evenNumbers, evenNumbers);
  }
}