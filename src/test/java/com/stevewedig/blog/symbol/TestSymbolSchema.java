package com.stevewedig.blog.symbol;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.stevewedig.blog.errors.NotThrown;

public class TestSymbolSchema {

  static Symbol<Integer> $a = SymbolLib.symbol(Integer.class);
  static Symbol<Boolean> $b = SymbolLib.symbol("mybool");
  static Symbol<Boolean> $c = SymbolLib.symbol(Boolean.class);
  static Symbol<Float> d = SymbolLib.symbol(Float.class);

  @Test
  public void testSymbolSchema() {

    SymbolSchema schema = SymbolLib.schema($a, $b).withOptional($c);

    // all symbols
    assertEquals(schema.symbols(), ImmutableSet.<Symbol<?>>of($a, $b, $c));

    // required symbols
    assertEquals(schema.requiredSymbols(), ImmutableSet.<Symbol<?>>of($a, $b));

    // optional symbols
    assertEquals(schema.optionalSymbols(), ImmutableSet.<Symbol<?>>of($c));

    // validate ok
    schema.validate($a, $b, $c); // with opt c
    schema.validate($a, $b); // without opt c

    // validate missing symbol
    try {
      schema.validate($b, $c);
      throw new NotThrown(InvalidSymbols.class);
    } catch (InvalidSymbols e) {
    }

    // validate unexpected symbol
    try {
      schema.validate($a, $b, $c, d);
      throw new NotThrown(InvalidSymbols.class);
    } catch (InvalidSymbols e) {
    }

    // validate a map (just calls map.symbols())
    schema.validate(SymbolLib.map().put($a, 1).put($b, true));

  }


}