package edu.neu.coe.info6205.team7.RadixSort;

import edu.neu.coe.huskySort.sort.Helper;
import edu.neu.coe.info6205.team7.Benchmark.HelperWIthTesting;
import edu.neu.coe.info6205.team7.NameBySyllabification.ChsCharToIdxArrBySylla;
import java.util.Arrays;


/**
 * A class extends from PinyinSyllabificationSort to implement LSD Radix Sort for Chinese characters
 * in Pinyin Syllabification order
 *
 * @author Yiqing Jackie Huang
 */
public final class LsdPinyinSyllabificationSort extends PinyinSyllabificationSort {

  public LsdPinyinSyllabificationSort(Helper<String> helper) {
    super(helper);
    radix = new int[]{37, 24, 24, 6};
  }

  public LsdPinyinSyllabificationSort(Helper<String> helper, int maxWordLength) {
    super(helper);
    radix = new int[]{37, 24, 24, 6};
    maxWordLen = maxWordLength;
  }

  @Override
  /**
   * Sort from xs[lo] to xs[hi] (exclusive) xs is an array of strings using LsdRadixSort.
   *
   * ===IMPORTANT===
   * Call PinyinSort.preProcess before calling MsdPinyinSort.sort
   * Call PinyinSort.postProcess after sorting to get the result in a string array
   *
   * @param xs the array to be sorted
   * @param lo the low index
   * @param hi the high index (exclusive)
   */
  public void sort(String[] xs, int lo, int hi) {
    aux = new byte[xs.length][];
    int maxLen = findMaxLength(xs) * 8;
    sort(lo, hi, maxLen, 0);
  }

  @Override
  void sort(int lo, int hi, int maxLen, int cirIdx) {
    int period = radix.length;
    int step;
    for (int i = maxLen - 5; i >= 0; i -= step) {
      int[] count = new int[radix[period - 1 - cirIdx] + 2];

      for (int j = lo; j < hi; j++) {
        count[charAt(compArr[j], i, period - 1 - cirIdx) + 2]++;
      }

      for (int r = 0; r < radix[period - 1 - cirIdx] + 1; r++)      // Transform counts to indices.
      {
        count[r + 1] += count[r];
      }

      for (int j = lo; j < hi; j++) {
        aux[count[charAt(compArr[j], i, period - 1 - cirIdx) + 1]++] = compArr[j];
      }

      if (hi - lo >= 0) {
        System.arraycopy(aux, 0, compArr, lo, hi - lo);
      }

      step = getStep(cirIdx);
      cirIdx = (cirIdx + 1) % period;
    }
  }

  /**
   * findMaxLength method returns maximum length of all available strings in an array
   *
   * @param strArr It contains an array of String from which maximum length needs to be found
   * @return int Returns maximum length value
   */
  private int findMaxLength(String[] strArr) {
    int maxLength = strArr[0].length();
    for (String str : strArr) {
      maxLength = Math.max(maxLength, str.length());
    }
    return maxLength;
  }

  @Override
  void convertStrArrToByteArr(String[] wordArr) {
    final ChsCharToIdxArrBySylla cctiabs = new ChsCharToIdxArrBySylla();

    for (int k = 0; k < wordArr.length; k++) {
      String word = wordArr[k];
      int byteShift = 0;
      int[] pinyinIdxArr = cctiabs.CharAt(word);

      for (int i = 0; i < word.length(); i++) {
        // Since the greatest number of the index is less than 64 and the sign bit is useless in our case,
        // when casting to byte, the missing first byte of the int does not affect the result

        // 1st idx
        compArr[k][byteShift] |= (byte) (pinyinIdxArr[i * 4]);
        // 2nd idx
        compArr[k][byteShift + 1] |= (byte) pinyinIdxArr[i * 4 + 1];
        // 3rd idx
        compArr[k][byteShift + 2] |= (byte) pinyinIdxArr[i * 4 + 2];
        // 4th idx
        compArr[k][byteShift + 3] |= (byte) (pinyinIdxArr[i * 4 + 3]);
        // original word
        copyWordToByteArr(word, i, k, byteShift);
        byteShift += 8;
      }
    }
  }

  public static void main(String[] args) {
    String[] sa = {"老伙计", "救济", "做作", "经济", "坐下", "啊"};
    RadixSort lrs = new LsdPinyinSyllabificationSort(new HelperWIthTesting<>("LSD Radix Sort"));
    lrs.preProcess(sa);
    lrs.sort(sa, 0, sa.length);
    lrs.postProcess(sa);
    Arrays.stream(sa).forEach(System.out::println);
  }
}
