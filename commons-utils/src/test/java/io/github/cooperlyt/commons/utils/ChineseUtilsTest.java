package io.github.cooperlyt.commons.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChineseUtilsTest {

  @Test
  public void testNameSplit() {
    ChineseUtils.PersonName name = ChineseUtils.nameSplit("张三三");

    assertEquals("张", name.getLastName());
    assertEquals("三三", name.getFirstName());
  }

}