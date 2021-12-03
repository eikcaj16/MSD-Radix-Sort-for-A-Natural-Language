package edu.neu.coe.info6205.team7.RadixSort;

import edu.neu.coe.huskySort.sort.Helper;
import edu.neu.coe.info6205.team7.Benchmark.HelperWIthTesting;
import edu.neu.coe.info6205.team7.NameByLetter.ChsCharToIdxArrByLetter;

/**
 * A class extends from PinyinSort to implement MSD Radix Sort for Chinese characters in Pinyin
 * natural order
 *
 * @author Yiqing Jackie Huang
 */
public final class MsdPinyinLetterSort extends PinyinSort {

  public MsdPinyinLetterSort(Helper<String> helper) {
    super(helper);
    radix = new int[]{24, 10, 8, 8, 6, 2, 5};
  }

  @Override
  void convertStrArrToByteArr(String[] wordArr) {
    final ChsCharToIdxArrByLetter cctiabl = new ChsCharToIdxArrByLetter();

    for (int k = 0; k < wordArr.length; k++) {
      String word = wordArr[k];
      int byteShift = 0;
      int[] pinyinIdxArr = cctiabl.CharAt(word);

      for (int i = 0; i < word.length(); i++) {
        // Since the greatest number of the index is less than 64 and the sign bit is useless in our case,
        // when casting to byte, the missing first byte of the int does not affect the result

        // 1st idx
        compArr[k][byteShift] |= (byte) (pinyinIdxArr[i * 7] - 1);
        // 2nd idx
        compArr[k][byteShift + 1] |= (byte) pinyinIdxArr[i * 7 + 1];
        // 3rd idx
        compArr[k][byteShift + 2] |= (byte) (pinyinIdxArr[i * 7 + 2] << 3);
        // 4th idx
        compArr[k][byteShift + 2] |= (byte) pinyinIdxArr[i * 7 + 3];
        // 5th idx
        compArr[k][byteShift + 3] |= (byte) (pinyinIdxArr[i * 7 + 4] << 4);
        // 6th idx
        compArr[k][byteShift + 3] |= (byte) (pinyinIdxArr[i * 7 + 5] << 3);
        // 7th idx
        compArr[k][byteShift + 3] |= (byte) (pinyinIdxArr[i * 7 + 6] - 1);
        // original word
        copyWordToByteArr(word, i, k, byteShift);
        byteShift += 8;
      }
    }
  }

  @Override
  int getStep(int cirIdx) {
    switch (cirIdx) {
      case 0:
      case 1:
      case 3:
        return 1;
      case 6:
        return 4;
      default:
        return 0;
    }
  }

  @Override
  int charAt(byte[] bArr, int d, int cirIdx) {
    if (d < bArr.length) {
      int loc = (d / 8) * 8;
      switch (cirIdx) {
        case 0:
          return bArr[loc];
        case 1:
          return bArr[loc + 1];
        case 2:
          return (int) bArr[loc + 2] >> 3;
        case 3:
          return (int) bArr[loc + 2] & 0x7;
        case 4:
          return (int) bArr[loc + 3] >> 4;
        case 5:
          return bArr[loc + 3] & 0b100;
        case 6:
          return (int) bArr[loc + 3] & 0x7;
      }
    }
    return -1;
  }


  public static void main(String[] args) {
//    String[] sa = {"老伙计", "救济", "做作", "经济", "坐下", "啊"};
//
//    List<String> names_txt = new ArrayList<>();
//    try{
//      FileReader fr = new FileReader("shuffledChinese3.txt");
//      BufferedReader br = new BufferedReader(fr);
//      System.out.println("\n======== Create file reader success! ========");
//
//      while(br.ready()){
//        names_txt.add(br.readLine());
//      }
//
//      System.out.println("======== Data have been read! ========");
//
//      br.close();
//      fr.close();
//    } catch (Exception e){
//      e.printStackTrace();
//    }
//
//    String[] ssa = new String[names_txt.size()];
//
//    System.out.println(ssa.length);
//    for (int i = 0; i < ssa.length; i++)
//      ssa[i] = names_txt.get(i);
//
//    for (int i = 0; i < ssa.length; i++) {
//      if (ssa[i].contains("\uFEFF")) {
//        System.out.println(ssa[i]);
//      }
//    }
//
//
//    MsdRadixSort mrs = new MsdPinyinLetterSort(new HelperWIthTesting<>("MSD Radix Sort"));
//    mrs.preProcess(ssa);
//    mrs.sort(ssa, 0, ssa.length);
//    mrs.postProcess(ssa);
//    Arrays.stream(ssa).forEach(System.out::println);

    String[] aa = {"讲究", "窗台", "长江"};
    RadixSort mrs = new MsdPinyinLetterSort(new HelperWIthTesting<>("MM"));
    mrs.preProcess(aa);
    mrs.sort(aa, 0, aa.length);
  }
}