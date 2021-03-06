package com.stevewedig.blog.symbol;

import static com.stevewedig.blog.symbol.SymbolLib.*;
import static com.stevewedig.blog.value_objects.ObjectHelperLib.assertStateEquals;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.stevewedig.blog.errors.*;

public class TestSymbolMap {

  // symbols
  static Symbol<Boolean> $bool = symbol("bool");
  static Symbol<Integer> $int = symbol("int");
  static Symbol<Integer> $unused = symbol("unused");
  static Symbol<String> $null = symbol("null");

  // values
  static Boolean boolValue = true;
  static Integer intValue = 1;

  // ===========================================================================
  // verify behavior
  // ===========================================================================

  @Test
  public void testSymbolMapExample() {

    // create fluid1 using one put() per line
    SymbolMap.Fluid fluid1 = map();
    fluid1.put($bool, boolValue);
    fluid1.put($int, intValue);
    fluid1.put($null, null); // decided to support this use case, unlike ImmutableMap
    verifyExampleMap(fluid1);

    // create fluid2 using putAll()
    SymbolMap.Fluid fluid2 = map();
    fluid2.putAll(fluid1);
    verifyExampleMap(fluid2);

    // create solid1 by copying an existing fluid
    SymbolMap solid1 = fluid1.solid();
    verifyExampleMap(solid1);

    // create solid2 using a fluid as a fluent builder
    SymbolMap solid2 = map().put($bool, boolValue).put($int, intValue).put($null, null).solid();
    verifyExampleMap(solid2);

    // verify solids behave as values
    assertEquals(solid1, solid2);

    // verify fluids behave as entities
    assertNotEquals(fluid1, fluid2);

  }

  private void verifyExampleMap(SymbolMap map) {

    // =================================
    // symbols
    // =================================

    assertEquals(map.symbols(), ImmutableSet.<Symbol<?>>of($int, $bool, $null));

    assertTrue(map.contains($bool));
    assertTrue(map.contains($int));
    assertTrue(map.contains($null));
    assertFalse(map.contains($unused));

    // =================================
    // get
    // =================================

    // get hit
    assertEquals(boolValue, map.get($bool));
    assertEquals(intValue, map.get($int));
    assertNull(map.get($null));

    // get miss
    try {
      map.get($unused);
      throw new NotThrown(NotContained.class);
    } catch (NotContained e) {
    }

    // =================================
    // getDefault
    // =================================

    // value is present
    assertEquals(intValue, map.getDefault($int, 999));

    // value is absent
    assertEquals((Integer) 999, map.getDefault($unused, 999));

    // value is null, adaptNull = true
    assertEquals("default", map.getDefault($null, "default"));

    // value is null, adaptNull = false
    assertNull(map.getDefault($null, "default", false));

    // =================================
    // getOptional
    // =================================

    // value is present
    assertEquals(Optional.of(intValue), map.getOptional($int));

    // value is absent
    assertEquals(Optional.absent(), map.getOptional($unused));

    // value is null, adaptNull = true
    assertEquals(Optional.absent(), map.getOptional($null));

    // value is null, adaptNull = false
    try {
      map.getOptional($null, false);
      throw new NotThrown(NullPointerException.class);
    } catch (NullPointerException e) {
    }

    // =================================
    // getNullable
    // =================================

    // value is present
    assertEquals(intValue, map.getNullable($int));

    // value is absent
    assertNull(map.getNullable($unused));

    // value is null
    assertNull(map.getNullable($null));

    // =================================
    // solid/fluid copies
    // =================================

    assertEquals(map.fluid().solid(), map.fluid().solid());

    assertStateEquals(map, map.fluid());

    // =================================
    // stateCopy
    // =================================

    Map<Symbol<?>, Object> state = new HashMap<>();
    state.put($bool, boolValue);
    state.put($int, intValue);
    state.put($null, null);

    assertEquals(state, map.stateCopy());
  }

}
