package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.Collection;

public enum ThirtyMinuteBlock {
  BLOCK_00_00("00:00"),
  BLOCK_00_30("00:30"),
  BLOCK_01_00("01:00"),
  BLOCK_01_30("01:30"),
  BLOCK_02_00("02:00"),
  BLOCK_02_30("02:30"),
  BLOCK_03_00("03:00"),
  BLOCK_03_30("03:30"),
  BLOCK_04_00("04:00"),
  BLOCK_04_30("04:30"),
  BLOCK_05_00("05:00"),
  BLOCK_05_30("05:30"),
  BLOCK_06_00("06:00"),
  BLOCK_06_30("06:30"),
  BLOCK_07_00("07:00"),
  BLOCK_07_30("07:30"),
  BLOCK_08_00("08:00"),
  BLOCK_08_30("08:30"),
  BLOCK_09_00("09:00"),
  BLOCK_09_30("09:30"),
  BLOCK_10_00("10:00"),
  BLOCK_10_30("10:30"),
  BLOCK_11_00("11:00"),
  BLOCK_11_30("11:30"),
  BLOCK_12_00("12:00"),
  BLOCK_12_30("12:30"),
  BLOCK_13_00("13:00"),
  BLOCK_13_30("13:30"),
  BLOCK_14_00("14:00"),
  BLOCK_14_30("14:30"),
  BLOCK_15_00("15:00"),
  BLOCK_15_30("15:30"),
  BLOCK_16_00("16:00"),
  BLOCK_16_30("16:30"),
  BLOCK_17_00("17:00"),
  BLOCK_17_30("17:30"),
  BLOCK_18_00("18:00"),
  BLOCK_18_30("18:30"),
  BLOCK_19_00("19:00"),
  BLOCK_19_30("19:30"),
  BLOCK_20_00("20:00"),
  BLOCK_20_30("20:30"),
  BLOCK_21_00("21:00"),
  BLOCK_21_30("21:30"),
  BLOCK_22_00("22:00"),
  BLOCK_22_30("22:30"),
  BLOCK_23_00("23:00"),
  BLOCK_23_30("23:30");

  private final String blockBeginning;

  // The bit set to 1 encodes the corresponding block
  private final long blockAsBit;

  private ThirtyMinuteBlock(String blockBeginning) {
    this.blockBeginning = blockBeginning;
    this.blockAsBit = 1L << this.ordinal();
  }

  public String getBlockBeginning() {
    return this.blockBeginning;
  }

  public long getBlockAsBit() {
    return this.blockAsBit;
  }

  public boolean isBlockSet(long attendingHours) {
    return (attendingHours & this.blockAsBit) != 0;
  }

  // Most significant 16 bits dont encode anything
  // The 48 least significant bits encode 30 minute blocks
  // If the bit is 1 the doctor is available, otherwise it is not
  // The least significant bit encodes the (0:00, 0:30) block
  // The 48th bit encodes the (23:3, 0:00) block
  public static Collection<ThirtyMinuteBlock> fromBits(long bits) {

    Collection<ThirtyMinuteBlock> blocks = new ArrayList<>();
    ThirtyMinuteBlock[] values = ThirtyMinuteBlock.values();

    for (int i = 0; i < values.length; i++) {
      if ((bits & (1L << i)) != 0) {
        blocks.add(values[i]);
      }
    }

    return blocks;
  }


  public static long toBits(Collection<ThirtyMinuteBlock> blocks) {
    
    long bits = 0;

    for (ThirtyMinuteBlock block : blocks) {
      bits |= block.getBlockAsBit();
    }

    return bits;
  }

  public static ThirtyMinuteBlock fromString(String block) {
    for (ThirtyMinuteBlock thirtyMinuteBlock : ThirtyMinuteBlock.values()) {
      if (thirtyMinuteBlock.getBlockBeginning().equals(block)) {
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

  public static Collection<ThirtyMinuteBlock> fromRange(ThirtyMinuteBlock from, ThirtyMinuteBlock to) {
    Collection<ThirtyMinuteBlock> result = new ArrayList<>();
    for (ThirtyMinuteBlock thirtyMinuteBlock : ThirtyMinuteBlock.values()) {
      if (thirtyMinuteBlock.ordinal() >= from.ordinal() && thirtyMinuteBlock.ordinal() <= to.ordinal()) {
        result.add(thirtyMinuteBlock);
      }
    }
    return result;
  }
}
