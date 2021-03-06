package com.stevewedig.blog.symbol;

import static com.stevewedig.blog.symbol.SymbolLib.*;
import static com.stevewedig.blog.translate.FormatLib.*;
import static org.junit.Assert.assertEquals;

import java.util.regex.*;

import org.junit.Test;

import com.google.common.collect.*;
import com.stevewedig.blog.symbol.translate.*;
import com.stevewedig.blog.translate.*;
import com.stevewedig.blog.util.PropLib;
import com.stevewedig.blog.value_objects.ValueMixin;

public class TestSymbolFormat {

  // ===========================================================================
  // Step 1: Create symbols with heterogeneous value types
  // ===========================================================================

  public static Symbol<String> $notUsed = symbol("notUsed");
  public static Symbol<String> $userName = symbol("userName");
  public static Symbol<Integer> $threadCount = symbol("threadCount");
  public static Symbol<Float> $version = symbol("version");
  public static Symbol<Double> $precision = symbol("precision");
  public static Symbol<Boolean> $createTables = symbol("createTables");
  public static Symbol<Boolean> $launchNukes = symbol("launchNukes");
  public static Symbol<LogLevel> $logLevel = symbol("logLevel");
  public static Symbol<Point> $point = symbol("point");
  public static Symbol<ImmutableSet<String>> $adminEmails = symbol("adminEmails");
  public static Symbol<ImmutableList<Integer>> $thresholds = symbol("thresholds");

  // ===========================================================================
  // test
  // ===========================================================================

  @Test
  public void testSymbolFormat() {

    // =================================
    // Step 2: Create translator, associating symbols with formats
    // =================================

    // $userName has default format, which is strFormat, which is a no-op
    // $createTables and $logLevels are both Boolean, but use different formats
    SymbolTranslator translator =
        SymbolFormatLib.translator().add($notUsed, strFormat).add($userName)
            .add($threadCount, intFormat).add($version, floatFormat).add($precision, doubleFormat)
            .add($createTables, boolFlagFormat).add($launchNukes, boolJsonFormat)
            .add($logLevel, logLevelFormat).add($point, Point.format)
            .add($adminEmails, strCommaSetFormat).add($thresholds, intCommaListFormat).build();

    // =================================
    // Step 3: Get a ConfigFormat
    // =================================

    // This one is backed by java.util.Properties
    ConfigFormat configFormat = PropLib.format;

    // =================================
    // Step 4: Chain a ConfigFormat and a SymbolTranslator to get a SymbolFormat
    // =================================

    SymbolFormat format = SymbolFormatLib.format(configFormat, translator);

    // =================================
    // Step 5: Use SymbolFormat to convert between fileContent and symbolMap
    // =================================

    SymbolMap.Solid symbolMap =
        map().put($userName, "bob").put($threadCount, 4).put($version, 2.3f).put($precision, 0.01d)
            .put($createTables, true).put($launchNukes, false).put($logLevel, LogLevel.warning)
            .put($point, new Point(7, 8)).put($adminEmails, ImmutableSet.of("alice@example.com"))
            .put($thresholds, ImmutableList.of(10, 20, 30)).solid();

    String fileContent =
        "userName = bob\n" + "threadCount = 4\n" + "version = 2.3\n" + "precision = 0.01\n"
            + "createTables = 1\n" + "launchNukes = false\n" + "logLevel = warning\n"
            + "point = {\"x\": 7, \"y\": 8}\n" + "adminEmails = alice@example.com\n"
            + "thresholds = 10, 20, 30\n";

    assertEquals(symbolMap, format.parse(fileContent));

    // writing is non-deterministic, so easiest to test it by parsing it back out
    assertEquals(symbolMap, format.parse(format.write(symbolMap)));

    // uncomment this to see the generated file
    // System.out.println(format.write(symbolMap));
  }

  // ===========================================================================
  // LogLevel enum with format using toString/valueOf
  // ===========================================================================

  static enum LogLevel {
    fatal, error, warning, info, debug
  }

  Format<LogLevel> logLevelFormat = new Format<LogLevel>() {
    @Override
    public LogLevel parse(String syntax) throws ParseError {
      return LogLevel.valueOf(syntax);
    }

    @Override
    public String write(LogLevel model) {
      return model.toString();
    }
  };

  // ===========================================================================
  // Point class with format using JSON
  // ===========================================================================

  // ValueMixin makes this behave as a value object:
  // http://stevewedig.com/2014/07/31/value-objects-in-java-and-python/#java
  static class Point extends ValueMixin {

    // =================================
    // state
    // =================================

    private final int x;
    private final int y;

    @Override
    protected Object[] fields() {
      return array("x", x, "y", y);
    }

    // =================================
    // constructor
    // =================================

    public Point(int x, int y) {
      super();
      this.x = x;
      this.y = y;
    }

    // =================================
    // getters
    // =================================

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    // =================================
    // format
    // =================================

    // Obviously in practice you would want to use a real JSON library, but I don't want to
    // introduce a dependency on one here.
    public static Format<Point> format = new Format<Point>() {

      @Override
      public Point parse(String json) throws ParseError {

        Pattern pattern = Pattern.compile("x\":\\s*(\\d+).*y\":\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        matcher.find();

        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));

        return new Point(x, y);
      }

      @Override
      public String write(Point point) {
        return "{\"x\": " + point.getX() + ", \"y\": " + point.getY() + "}";
      }
    };

  }

}
