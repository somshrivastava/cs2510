import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

class ArrayUtils {
  <T, U> ArrayList<U> map(ArrayList<T> arr, Function<T, U> func) {
    ArrayList<U> result = new ArrayList<U>();
    for (T item : arr) {
      result.add(func.apply(item));
    }
    return result;
  }
  <T> void swap(ArrayList<T> alist, int index1, int index2) {
    alist.set(index2, alist.set(index1, alist.get(index2)));
  }
  <X, Y> Y foldl(ArrayList<X> alist, BiFunction<X, Y, Y> fun, Y base) {
    Y result = base;
    for (X item : alist) {
      result = fun.apply(item, result);
    }
    return result;
  }
  <X, Y> Y foldr(ArrayList<X> alist, BiFunction<X, Y, Y> fun, Y base) {
    ArrayList<X> reversed = new ArrayList<X>();
    for (X x : alist) {
      reversed.add(0, x);
    }
    return this.foldl(alist, fun, base);
  }
  <T> int find(ArrayList<T> alist, Comparator<T> comp, T target) {
    return this.findHelp(alist, comp, target, 0);
  }
  <T> int findHelp(ArrayList<T> alist, Comparator<T> comp, T target, int current) {
    if (current >= alist.size()) {
      return -1;
    } else if (comp.compare(target, alist.get(current)) == 0) {
      return current;
    } else {
      return this.findHelp(alist, comp, target, current + 1);
    }
  }
  <T> int binarySearch(ArrayList<T> alist, Comparator<T> comp, T target) {
    return this.binaryHelp(alist, comp, target, 0, alist.size() - 1);
  }
  <T> int binaryHelp(ArrayList<T> alist, Comparator<T> comp, T target, int lo, int hi) {
    if (lo > hi) {
      return -1;
    }
    int mid = (lo + hi) / 2;
    if (comp.compare(target, alist.get(mid)) == 0) {
      return mid;
    } else if (comp.compare(target, alist.get(mid)) > 0) {
      return this.binaryHelp(alist, comp, target, mid + 1, hi);
    } else {
      return this.binaryHelp(alist, comp, target, lo, mid - 1);
    }
  }
  <T> int binarySearchWhile(ArrayList<T> alist, Comparator<T> comp, T target) {
    int lo = 0;
    int hi = alist.size() - 1;
    int mid;
    while (lo <= hi) {
      mid = (lo + hi) / 2;
      if (comp.compare(target, alist.get(mid)) == 0) {
        return mid;
      } else if (comp.compare(target, alist.get(mid)) < 0) {
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return -1;
  }
  <T> int findIndexOfMin(ArrayList<T> alist, Comparator<T> comp) {
    if (alist.size() == 0) {
      return -1;
    }
    int minSoFar = 0;
    for (int j = 0; j < alist.size(); j = j + 1) {
      if (comp.compare(alist.get(j), alist.get(minSoFar)) < 0) {
        minSoFar = j;
      }
    }
    return minSoFar;
  }
  <T> ArrayList<T> filter(ArrayList<T> array, Predicate<T> predicate) {
    return customFilter(array, predicate, true);
  }
  <T> ArrayList<T> filterNot(ArrayList<T> array, Predicate<T> predicate) {
    return customFilter(array, predicate, false);
  }
  <T> ArrayList<T> customFilter(ArrayList<T> array, Predicate<T> predicate, boolean shouldAdd) {
    ArrayList<T> result = new ArrayList<T>();
    for (T element : array) {
      if (predicate.test(element) == shouldAdd) {
        result.add(element);
      }
    }
    return result;
  }
  <T> void removeFailing(ArrayList<T> array, Predicate<T> predicate) {
    customRemove(array, predicate, false);
  }
  <T> void removePassing(ArrayList<T> array, Predicate<T> predicate) {
    customRemove(array, predicate, true);
  }
  <T> void customRemove(ArrayList<T> array, Predicate<T> predicate, boolean shouldRemove) {
    for (int i = array.size() - 1; i >= 0; i--) {
      if (predicate.test(array.get(i)) == shouldRemove) {
        array.remove(i);
      }
    }
  }
  <T> ArrayList<T> interweave(ArrayList<T> arr1, ArrayList<T> arr2) {
    return customInterweave(arr1, arr2, 1, 1);
  }
  <T> ArrayList<T> customInterweave(ArrayList<T> arr1, ArrayList<T> arr2, int getFrom1, int getFrom2) {
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
}