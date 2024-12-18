package ar.edu.itba.paw.models;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum ThirtyMinuteBlock {
  BLOCK_00_00("00:00", "00:30"),
  BLOCK_00_30("00:30", "01:00"),
  BLOCK_01_00("01:00", "01:30"),
  BLOCK_01_30("01:30", "02:00"),
  BLOCK_02_00("02:00", "02:30"),
  BLOCK_02_30("02:30", "03:00"),
  BLOCK_03_00("03:00", "03:30"),
  BLOCK_03_30("03:30", "04:00"),
  BLOCK_04_00("04:00", "04:30"),
  BLOCK_04_30("04:30", "05:00"),
  BLOCK_05_00("05:00", "05:30"),
  BLOCK_05_30("05:30", "06:00"),
  BLOCK_06_00("06:00", "06:30"),
  BLOCK_06_30("06:30", "07:00"),
  BLOCK_07_00("07:00", "07:30"),
  BLOCK_07_30("07:30", "08:00"),
  BLOCK_08_00("08:00", "08:30"),
  BLOCK_08_30("08:30", "09:00"),
  BLOCK_09_00("09:00", "09:30"),
  BLOCK_09_30("09:30", "10:00"),
  BLOCK_10_00("10:00", "10:30"),
  BLOCK_10_30("10:30", "11:00"),
  BLOCK_11_00("11:00", "11:30"),
  BLOCK_11_30("11:30", "12:00"),
  BLOCK_12_00("12:00", "12:30"),
  BLOCK_12_30("12:30", "13:00"),
  BLOCK_13_00("13:00", "13:30"),
  BLOCK_13_30("13:30", "14:00"),
  BLOCK_14_00("14:00", "14:30"),
  BLOCK_14_30("14:30", "15:00"),
  BLOCK_15_00("15:00", "15:30"),
  BLOCK_15_30("15:30", "16:00"),
  BLOCK_16_00("16:00", "16:30"),
  BLOCK_16_30("16:30", "17:00"),
  BLOCK_17_00("17:00", "17:30"),
  BLOCK_17_30("17:30", "18:00"),
  BLOCK_18_00("18:00", "18:30"),
  BLOCK_18_30("18:30", "19:00"),
  BLOCK_19_00("19:00", "19:30"),
  BLOCK_19_30("19:30", "20:00"),
  BLOCK_20_00("20:00", "20:30"),
  BLOCK_20_30("20:30", "21:00"),
  BLOCK_21_00("21:00", "21:30"),
  BLOCK_21_30("21:30", "22:00"),
  BLOCK_22_00("22:00", "22:30"),
  BLOCK_22_30("22:30", "23:00"),
  BLOCK_23_00("23:00", "23:30"),
  BLOCK_23_30("23:30", "00:00");

  private final String blockBeginning;
  private final String blockEnd;

  private ThirtyMinuteBlock(String blockBeginning, String blockEnd) {
    this.blockBeginning = blockBeginning;
    this.blockEnd = blockEnd;
  }

  public static ThirtyMinuteBlock fromTime(LocalTime time) {

    // Get which block the time belongs to
    int blockIndex = time.getHour() * 2;
    if (time.getMinute() >= 30) {
      blockIndex++;
    }

    return ThirtyMinuteBlock.values()[blockIndex];
  }

  public static ThirtyMinuteBlock fromBeginning(String block) {
    for (ThirtyMinuteBlock thirtyMinuteBlock : ThirtyMinuteBlock.values()) {
      if (thirtyMinuteBlock.getBlockBeginning().equals(block)) {
        return thirtyMinuteBlock;
      }
    }
    return null;
  }

  public static ThirtyMinuteBlock fromEnd(String block) {
    for (ThirtyMinuteBlock thirtyMinuteBlock : ThirtyMinuteBlock.values()) {
      if (thirtyMinuteBlock.getBlockEnd().equals(block)) {
        return thirtyMinuteBlock;
      }
    }
    return null;
  }

  public static Collection<ThirtyMinuteBlock> fromStrings(String... blocks) {
    Collection<ThirtyMinuteBlock> result = new ArrayList<>();
    for (String block : blocks) {
      for (ThirtyMinuteBlock thirtyMinuteBlock : ThirtyMinuteBlock.values()) {
        if (thirtyMinuteBlock.getBlockBeginning().equals(block)) {
          result.add(thirtyMinuteBlock);
        }
      }
    }
    return result;
  }

  public static List<ThirtyMinuteBlock> fromRange(
      ThirtyMinuteBlock from, ThirtyMinuteBlock to) {
    List<ThirtyMinuteBlock> result = new ArrayList<>();
    for (ThirtyMinuteBlock thirtyMinuteBlock : ThirtyMinuteBlock.values()) {
      if (thirtyMinuteBlock.ordinal() >= from.ordinal()
          && thirtyMinuteBlock.ordinal() <= to.ordinal()) {
        result.add(thirtyMinuteBlock);
      }
    }
    return result;
  }

  public String getBlockBeginning() {
    return this.blockBeginning;
  }

  public String getBlockEnd() {
    return this.blockEnd;
  }

  public boolean isBefore(ThirtyMinuteBlock other) {
    return this.ordinal() < other.ordinal();
  }

  public boolean isAfter(ThirtyMinuteBlock other) {
    return this.ordinal() > other.ordinal();
  }
}
